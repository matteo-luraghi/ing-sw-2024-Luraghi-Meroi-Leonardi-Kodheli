package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * YourTurn class
 * used to tell the player that it's its turn
 * @author Matteo Leonardo Luraghi
 */
public class YourTurn extends ServerMessage {
    @Serial
    private static final long serialVersionUID = -532540375599572593L;

    /**
     * Constructor
     */
    public YourTurn() {
    }

    /**
     * Sets the view's private variable yourTurn to true
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        new Thread(() -> {
            try {
                view.showMessage("It's your turn!");
            } catch (RemoteException e) {
                System.err.println("Error sending message");
            }
            try {
                view.setMyTurn(true);
            } catch (RemoteException e) {
                System.err.println("Error setting my turn");
            }
            try {
                view.setPlayPhase(true);
            } catch (RemoteException e) {
                System.err.println("Error setting play phase");
            }
        }).start();
    }
}
