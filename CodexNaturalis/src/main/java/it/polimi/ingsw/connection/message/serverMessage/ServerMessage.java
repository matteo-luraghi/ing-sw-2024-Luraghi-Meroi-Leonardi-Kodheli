package it.polimi.ingsw.connection.message.serverMessage;

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
     * Constructor, sets the message type
     */
    public ServerMessage() {
    }

    /**
     * show the message in the CLI or GUI
     * @param view the view interface
     */
    public abstract void show(View view);
}
