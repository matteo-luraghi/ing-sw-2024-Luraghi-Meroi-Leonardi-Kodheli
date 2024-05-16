package it.polimi.ingsw.connection.rmi;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.RemoteServer;
import it.polimi.ingsw.connection.socket.message.connectionMessage.Disconnection;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.ScoreBoard;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * RMIConnectionHandler class
 * @author Matteo Leonardo Luraghi
 */
public class RMIConnectionHandler extends ConnectionHandler {
    @Serial
    private static final long serialVersionUID = 9202804208069477313L;
    private View view;
    private final Registry registry;
    private final Registry viewRegistry;

    public RMIConnectionHandler(Registry registry, Registry viewRegistry) {
        this.registry = registry;
        this.viewRegistry = viewRegistry;
    }

    /**
     * View setter
     */
    public void setView() {
        try {
            this.view = (View) this.viewRegistry.lookup("view" + getClientNickname());
        } catch (Exception e) {
            System.out.println("Error connecting to client");
            e.printStackTrace();
        }
    }

    /**
     * Ask the player to choose a game to join or create a new game
     * @param gameNames the names of not started games
     */
    @Override
    public void joinGameRequest(ArrayList<String> gameNames) {
        try {
            this.view.showJoinOrCreate(gameNames);
        } catch (RemoteException e) {
            System.err.println("Error asking to join game");
        }
    }

    /**
     * Ask the player for their color
     * @param availableColors the possible colors
     */
    @Override
    public void colorRequest(ArrayList<Color> availableColors) {
        try {
            this.view.insertColor(availableColors);
        } catch (RemoteException e) {
            System.err.println("Error asking for color");
            disconnect();
        }
    }

    /**
     * Ask the player for the number of players in the new game
     */
    @Override
    public void playersNumberRequest() {
        try {
            this.view.askForPlayersNumber();
        } catch (RemoteException e) {
            System.err.println("Error asking for players number");
            disconnect();
        }
    }

    /**
     * Send the player a message
     * @param message the message
     */
    @Override
    public void sendTextMessage(String message) {
        try {
            this.view.showMessage(message);
        } catch (RemoteException e) {
            System.err.println("Error showing message");
            disconnect();
        }
    }

    /**
     * Tell the player that the game will start when there are enough players
     */
    @Override
    public void waitingForPlayers() {
        try {
            this.view.ShowWaitingForPlayers();
        } catch (RemoteException e) {
            System.err.println("Error waiting for players");
            disconnect();
        }
    }

    /**
     * Set the player on the client
     * @param player the player
     */
    @Override
    public void setPlayer(Player player) {
        try {
            this.view.setUser(player);
        } catch (RemoteException e) {
            System.err.println("Error setting player");
            disconnect();
        }
    }

    /**
     * Ask the player to place the starting card (front or back)
     * @param startingCard the starting card
     */
    @Override
    public void playStartingCardRequest(StartingCard startingCard) {
        try {
            this.view.ChooseStartingCardSide(startingCard);
        } catch (RemoteException e) {
            System.err.println("Error selecting starting card");
            disconnect();
        }
    }

    /**
     * Ask the player to choose one goal card
     * @param goalCards the goal cards
     */
    @Override
    public void goalCardRequest(GoalCard[] goalCards) {
        try {
            this.view.ShowChoosePrivateGoal(goalCards);
        } catch (RemoteException e) {
            System.err.println("Error asking for goal card");
            disconnect();
        }
    }

    /**
     * Update the game when a card is played or drawn
     * @param game the current game
     */
    @Override
    public void updateGame(GameState game) {
        new Thread(() -> {
            try {
                this.view.setGame(game);
            } catch (RemoteException e) {
                System.err.println("Error updating game");
                disconnect();
            }
        }).start();
    }

    /**
     * Tell the player that it's not their turn
     * @param message the message
     */
    @Override
    public void notYourTurn(String message) {
        try {
            this.view.showMessage(message);
        } catch (RemoteException e) {
            System.err.println("Error sending message");
            disconnect();
        }
        new Thread(() -> {
            try {
                this.view.setMyTurn(false);
            } catch (RemoteException e) {
                System.err.println("Error setting my turn");
                disconnect();
            }
        }).start();
    }

    /**
     * Tell the player that it's their turn to play
     */
    @Override
    public void yourTurn() {
        new Thread(() -> {
            try {
                this.view.showMessage("It's your turn!");
            } catch (RemoteException e) {
                System.err.println("Error sending message");
                disconnect();
            }
            try {
                this.view.setMyTurn(true);
            } catch (RemoteException e) {
                System.err.println("Error setting my turn");
                disconnect();
            }
            try {
                this.view.setPlayPhase(true);
            } catch (RemoteException e) {
                System.err.println("Error setting play phase");
                disconnect();
            }
        }).start();
    }

    /**
     * Wait for user commands
     */
    @Override
    public void listenForCommands() {
        new Thread(() -> {
            try {
                this.view.getCommands();
            } catch (RemoteException e) {
                System.err.println("Error listening for commands");
                disconnect();
            }
        }).start();
    }

    /**
     * Ask the player to play a card
     * @param player the player
     */
    @Override
    public void playCardRequest(Player player) {
        new Thread(() -> {
            try {
                this.view.showMessage("Play a card!");
            } catch (RemoteException e) {
                System.err.println("Error sending message");
                disconnect();
            }
            try {
                this.view.setPlayPhase(true);
            } catch (RemoteException e) {
                System.err.println("Error setting play phase");
                disconnect();
            }
        }).start();
    }

    /**
     * Ask the player to draw a card
     * @param player the player
     */
    @Override
    public void drawCardRequest(Player player) {
        try {
            this.view.showMessage("You now have to draw a card!");
        } catch (RemoteException e) {
            System.err.println("Error sending message");
            disconnect();
        }
        new Thread(() -> {
            try {
                this.view.setPlayPhase(false);
            } catch (RemoteException e) {
                System.err.println("Error setting play phase");
                disconnect();
            }
        }).start();
    }

    /**
     * Show the winner
     * @param game the current game
     */
    @Override
    public void showWinner(GameState game) {
        try {
            this.view.ShowWinner(game);
        } catch (RemoteException e) {
            System.err.println("Error showing winner");
            disconnect();
        }
    }

    /**
     * Show the scoreboard of the game
     * @param scoreBoard the scoreboard
     */
    @Override
    public void showScoreBoard(ScoreBoard scoreBoard) {
        try {
            this.view.ShowScoreBoard(scoreBoard);
        } catch (RemoteException e) {
            System.err.println("Error showing scoreboard");
            disconnect();
        }
    }

    /**
     * Send a message to the client
     * @param msg the message
     */
    @Override
    public void sendMessage(Serializable msg) {
        if (msg instanceof Disconnection) {
            ((Disconnection) msg).show(this.view);
            disconnect();
        }
    }

    /**
     * Disconnects the connection handler removing the user
     */
    @Override
    public void disconnect() {
        try {
            this.view.disconnectClient();
        } catch (Exception ignored) {
        }

        try {
            View stubView = (View) viewRegistry.lookup("view" + getClientNickname());
            UnicastRemoteObject.unexportObject(stubView, true);
        } catch (Exception ignored) {}

        try {
            viewRegistry.unbind("view" + getClientNickname());
        } catch (Exception ignored) {}

        try {
            RemoteServer server = (RemoteServer) registry.lookup("server");
            server.removeClient(this);
        } catch (Exception e) {
            System.err.println("Error disconnecting");
            e.printStackTrace();
        }
    }
}
