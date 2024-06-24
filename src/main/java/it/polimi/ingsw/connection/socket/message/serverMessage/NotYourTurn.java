package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * NotYourTurn class
 * used to end the current player's turn
 * @author Matteo Leonardo Luraghi
 */
public class NotYourTurn extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 2741292733883597798L;
    private final String message;

    /**
     * Constructor
     * @param message the message to be displayed
     */
    public NotYourTurn(String message) {
        this.message = message;
    }

    /**
     * Show the turn ended message
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        try {
            view.showMessage(this.message);
        } catch (RemoteException e) {
            System.err.println("Error sending mesage");
        }
        if (view.getClass() == CLI.class) {
            new Thread(() -> ((CLI) view).setMyTurn(false)).start();
        }
    }
}
