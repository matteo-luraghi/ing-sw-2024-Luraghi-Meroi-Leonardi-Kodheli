package it.polimi.ingsw.connection.rmi;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.connection.RemoteServer;
import it.polimi.ingsw.controller.RemoteController;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.Coordinates;
import it.polimi.ingsw.view.mainview.View;

import java.net.InetAddress;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * RMIClient class
 * @author Matteo Leonardo Luraghi
 */
public class RMIClient extends Client {
    private final Registry registry;
    private Registry viewRegistry;
    private RemoteController controller = null;
    private RMIConnectionHandler connectionHandler;
    private String gameName = null;

    /**
     * Constructor
     * @param ip the server ip address
     * @param port the server port
     * @param view the view interface
     * @throws RemoteException if errors in exposing/getting the RMI registry
     * @throws NotBoundException if errors in RMI connection
     * @throws IPNotFoundException if unable to find the machine's ip address
     */
    public RMIClient(String ip, int port, View view) throws RemoteException, NotBoundException, IPNotFoundException {
        super(view);
        // get the server registry
        this.registry = LocateRegistry.getRegistry(ip, port);

        // find the client's ip address
        String clientIP = null;
        try {
            clientIP = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception ignored) {
        }

        if (clientIP == null || clientIP.isEmpty() || clientIP.startsWith("127.0.")) {
            clientIP = IPAddresses.getAddress();
            if (clientIP == null) {
                throw new IPNotFoundException("Error getting client ip address");
            }
        }

        System.setProperty("java.rmi.server.hostname", clientIP);

        // create the client's registry
        boolean valid = false;
        int viewPort = 1100;
        while(!valid) {
            try {
                this.viewRegistry = LocateRegistry.createRegistry(viewPort);
                valid = true;
            } catch (RemoteException e) {
                viewPort++;
            }
        }
    }

    /**
     * Registry getter
     * @return the server registry
     */
    public Registry getRegistry() {
        return this.registry;
    }

    /**
     * Make the user choose to join or create a game
     * @param isJoin true if the user wants to join a game
     * @param gameName game name
     */
    @Override
    public void gameChoice(boolean isJoin, String gameName, String nickname) throws Exception {

        this.gameName = gameName;

        this.connectionHandler = new RMIConnectionHandler(registry, viewRegistry);
        this.connectionHandler.setClientNickname(nickname);

        try {
            // connect to the server
            RemoteServer server = (RemoteServer) registry.lookup("server");

            // check if the nickname is unique
            if(!server.checkUniqueNickname(nickname)) {
                throw new NicknameAlreadyPresentException("User already present");
            }

            try {
                // export view
                View stubView = (View) UnicastRemoteObject.exportObject(getView(), 0);
                this.viewRegistry.rebind("view" + nickname, stubView);
            } catch (Exception e) {
                System.out.println("Error exporting view");
            }

            this.connectionHandler.setView();

            // join a game or create a new one
            new Thread(() -> {
                try {
                    if (isJoin) {
                        server.joinGame(this.connectionHandler, gameName);
                    } else {
                        connectionHandler.playersNumberRequest();
                    }
                } catch (RemoteException e) {
                    System.err.println("Error adding player to game");
                    disconnect();
                }
            }).start();

            // get the game's controller
            try {
                this.controller = (RemoteController) registry.lookup("controller"+gameName);
            } catch (NotBoundException ignored) {
            }

        } catch (NicknameAlreadyPresentException e) {
            throw new NicknameAlreadyPresentException(e.getMessage());
        } catch (Exception ignored) {
        }
    }

    /**
     * Send the selected color to the server
     * @param color the color
     */
    @Override
    public void colorResponse(Color color) {
        if (this.controller == null) {
            try {
                this.controller = (RemoteController) registry.lookup("controller"+this.gameName);
            } catch (Exception e) {
                System.err.println("Controller not found");
                disconnect();
            }
        }

        this.connectionHandler.setClientColor(color);
        try {
            this.controller.setColor(this.connectionHandler, color);
        } catch (Exception e) {
            System.err.println("Unable to set the color");
            disconnect();
        }
    }

    /**
     * Send the number of players to the server
     * @param number the number of players
     */
    @Override
    public void playersNumberResponse(int number, String gameName) {
        try {
            // connect to the server
            RemoteServer server = (RemoteServer) registry.lookup("server");

            new Thread(() -> {
                try {
                    server.createGame(this.connectionHandler, number, gameName);
                } catch (RemoteException e) {
                    System.err.println("No controller found");
                    disconnect();
                }
            }).start();

        } catch (Exception e) {
            System.err.println("Error connecting to the server");
            disconnect();
        }
    }

    /**
     * Send the starting card on the correct side
     * @param card the starting card
     * @param isFront the side
     */
    @Override
    public void playStartingCardResponse(StartingCard card, boolean isFront) {
        try {
            this.controller.setStartingCard(card, isFront, this.connectionHandler);
        } catch (Exception e) {
            System.err.println("Error playing the starting card");
            disconnect();
        }
    }

    /**
     * Send the selected goal card to the server
     * @param card the goal card
     */
    @Override
    public void goalCardResponse(GoalCard card) {
        try {
            this.controller.setPrivateGoalCard(card, this.connectionHandler);
        } catch (Exception e) {
            System.err.println("Error choosing the goal card");
            disconnect();
        }
    }

    /**
     * Send the server a message to ensure the client is aware it's its player's turn
     */
    @Override
    public void yourTurnOk() {
        try {
            this.controller.playCardState();
        } catch (Exception e) {
            System.err.println("Error setting the play card state");
            disconnect();
        }
    }

    /**
     * Send the selected card to be played, the coordinates and the side
     * @param card the card
     * @param where the coordinates
     * @param isFront the side
     */
    @Override
    public void playCardResponse(ResourceCard card, Coordinates where, boolean isFront) {
        try {
            this.controller.playCard(this.connectionHandler, card, where, isFront);
        } catch (Exception e) {
            System.err.println("Error playing a card");
            disconnect();
        }
    }

    /**
     * Send the reference of which card to draw
     * @param which the card
     * @param isGold which deck to draw from
     */
    @Override
    public void drawCardResponse(int which, boolean isGold) {
        try {
            this.controller.drawCard(this.connectionHandler, which, isGold);
        } catch (Exception e) {
            System.err.println("Error drawing a card");
            disconnect();
        }
    }

    /**
     * Get the available games from the server
     */
    @Override
    public void refreshGamesNames() {
        ArrayList<String> gamesNames;
        try {
            RemoteServer server = (RemoteServer) this.registry.lookup("server");
            gamesNames = server.getGamesNames();
        } catch (Exception e) {
            gamesNames = null;
        }
        try {
            this.getView().setGameNames(gamesNames);
        } catch (RemoteException e) {
            System.err.println("Error sending games' names");
        }
    }

    /**
     * Disconnect the client
     */
    @Override
    public void disconnect() {
        this.setConnected(false);
    }

}
