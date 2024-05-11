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
import java.rmi.server.UnicastRemoteObject;

/**
 * RMIClient class
 * @author Matteo Leonardo Luraghi
 */
public class RMIClient extends Client {
    private final Registry registry;
    private RemoteController controller;
    private RMIConnectionHandler connectionHandler;

    public RMIClient(String ip, int port, View view) throws RemoteException, NotBoundException {
        super(view);
        this.registry = LocateRegistry.getRegistry(ip, port);
    }

    /**
     * Send the selected nickname to the server
     * @param nickname the nickname
     */
    public void loginResponse(String nickname) {

        try {
            View stubView = (View) UnicastRemoteObject.exportObject(getView(), 0);
            this.registry.rebind("view", stubView);
        } catch (Exception e) {
            System.out.println("Error exporting view");
            e.printStackTrace();
        }

        this.connectionHandler = new RMIConnectionHandler();
        this.connectionHandler.setClientNickname(nickname);

        try {
            // connect to the server
            RemoteServer server = (RemoteServer) registry.lookup("server");

            new Thread(() -> {
                try {
                    server.addToGame(this.connectionHandler);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }).start();

            try {
                this.controller = (RemoteController) registry.lookup("controller");
            } catch (RemoteException ignored) {
                System.err.println("Unable to find controller");
            }

        } catch (Exception e) {
            System.err.println("Error connecting to the server");
            e.printStackTrace();
        }
    }

    /**
     * Send the selected color to the server
     * @param color the color
     */
    public void colorResponse(Color color) {
        this.connectionHandler.setClientColor(color);
        try {
            this.controller.setColor(this.connectionHandler, color);
        } catch (RemoteException e) {
            System.err.println("Unable to set the color");
        }
    }

    /**
     * Send the number of players to the server
     * @param number the number of players
     */
    public void playersNumberResponse(int number) {
        try {
            // connect to the server
            RemoteServer server = (RemoteServer) registry.lookup("server");

            new Thread(() -> {
                try {
                    server.addToGame(this.connectionHandler, number);
                } catch (RemoteException e) {
                    System.err.println("No controller found");
                }
            }).start();

            this.controller = (RemoteController) registry.lookup("controller");
        } catch (Exception e) {
            System.err.println("Error connecting to the server");
            e.printStackTrace();
        }
    }

    /**
     * Send the starting card on the correct side
     * @param card the starting card
     * @param isFront the side
     */
    public void playStartingCardResponse(StartingCard card, boolean isFront) {
        try {
            this.controller.setStartingCard(card, isFront, this.connectionHandler);
        } catch (RemoteException e) {
            System.err.println("Error playing the starting card");
        }
    }

    /**
     * Send the selected goal card to the server
     * @param card the goal card
     */
    public void goalCardResponse(GoalCard card) {
        try {
            this.controller.setPrivateGoalCard(card, this.connectionHandler);
        } catch (RemoteException e) {
            System.err.println("Error choosing the goal card");
        }
    }

    /**
     * Send the server a message to ensure the client is aware it's its player's turn
     */
    public void yourTurnOk() {
        try {
            this.controller.playCardState();
        } catch (RemoteException e) {
            System.err.println("Error setting the play card state");
        }
    }

    /**
     * Send the selected card to be played, the coordinates and the side
     * @param card the card
     * @param where the coordinates
     * @param isFront the side
     */
    public void playCardResponse(ResourceCard card, Coordinates where, boolean isFront) {
        try {
            this.controller.playCard(this.connectionHandler, card, where, isFront);
        } catch (RemoteException e) {
            System.err.println("Error playing a card");
        }
    }

    /**
     * Send the reference of which card to draw
     * @param which the card
     * @param isGold which deck to draw from
     */
    public void drawCardResponse(int which, boolean isGold) {
        try {
            this.controller.drawCard(this.connectionHandler, which, isGold);
        } catch (RemoteException e) {
            System.err.println("Error drawing a card");
        }
    }

}
