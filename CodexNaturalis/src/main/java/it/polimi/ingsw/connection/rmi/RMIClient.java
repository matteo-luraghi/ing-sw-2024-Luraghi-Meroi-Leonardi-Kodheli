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

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.ExportException;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMIClient class
 * @author Matteo Leonardo Luraghi
 */
public class RMIClient extends Client {
    private final Registry registry;
    private RemoteController controller = null;
    private RMIConnectionHandler connectionHandler;

    public RMIClient(String ip, int port, View view) throws RemoteException, NotBoundException {
        super(view);
        this.registry = LocateRegistry.getRegistry(ip, port);
    }

    /**
     * Send the selected nickname to the server
     * @param nickname the nickname
     */
    @Override
    public void loginResponse(boolean isJoin, String gameName, String nickname) throws Exception {

        this.connectionHandler = new RMIConnectionHandler(nickname, registry);

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
                this.registry.rebind("view" + nickname, stubView);
            } catch (ExportException ignored) {
            } catch (Exception e) {
                System.out.println("Error exporting view");
            }

            this.connectionHandler.setView();

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

            try {
                this.controller = (RemoteController) registry.lookup("controller");
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
                this.controller = (RemoteController) registry.lookup("controller");
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
     * Disconnect the client
     */
    @Override
    public void disconnect() {
        this.setConnected(false);
    }

}
