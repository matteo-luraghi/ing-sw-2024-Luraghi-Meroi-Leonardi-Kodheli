package it.polimi.ingsw.connection.socket.message.connectionMessage;

import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Disconnection class
 * @author Matteo Leonardo Luraghi
 */
public class Disconnection implements Serializable {
    @Serial
    private static final long serialVersionUID = 6000054069559657434L;
    private final String clientNickname;

    /**
     * Constructor
     * @param clientNickname nickname of the disconnected player
     */
    public Disconnection(String clientNickname) {
        this.clientNickname = clientNickname;
    }

    /**
     * show the message
     * @param view view interface
     */
    public void show(View view) {
        try {
            view.showMessage(clientNickname + " was disconnected, ending game.");
        } catch (RemoteException e) {
            System.err.println("Error sending message");
        }
    }
}
