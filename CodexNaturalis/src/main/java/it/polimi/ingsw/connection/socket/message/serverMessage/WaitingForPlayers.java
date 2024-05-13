package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * WaitingForPlayers class
 * used to notify the players that more players are needed to start the game
 * @author Matteo Leonardo Luraghi
 */
public class WaitingForPlayers extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 4830636966703229889L;

    /**
     * Constructor
     */
    public WaitingForPlayers() {
    }

    /**
     * Shows the waiting for players window in CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        try {
            view.ShowWaitingForPlayers();
        } catch (RemoteException e) {
            System.err.println("Error waiting for players");
        }
    }
}
