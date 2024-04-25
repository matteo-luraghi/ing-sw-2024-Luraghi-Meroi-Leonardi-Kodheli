package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serializable;

/**
 * ServerMessage abstract class
 * @author Matteo Leonardo Luraghi
 */
public abstract class ServerMessage implements Serializable {
    private static final long serialVersionUID = -4496975448812853270L;
    private final Message type;

    /**
     * Constructor, sets the message type
     * @param type type of the message
     */
    public ServerMessage(Message type) {
        this.type = type;
    }

    /**
     * type getter
     * @return type of the message
     */
    public Message getType() {
        return this.type;
    }

    /**
     * show the message in the CLI or GUI
     * @param view the view interface
     */
    public abstract void show(View view);
}
