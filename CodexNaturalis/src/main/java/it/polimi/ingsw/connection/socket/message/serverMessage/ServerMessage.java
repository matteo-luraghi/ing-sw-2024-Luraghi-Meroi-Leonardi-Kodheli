package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.io.Serializable;

/**
 * ServerMessage abstract class
 * @author Matteo Leonardo Luraghi
 */
public abstract class ServerMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -5503253453512925709L;

    /**
     * Constructor
     */
    public ServerMessage() {
    }

    /**
     * show the message
     * @param view the view interface
     */
    public abstract void show(View view);
}
