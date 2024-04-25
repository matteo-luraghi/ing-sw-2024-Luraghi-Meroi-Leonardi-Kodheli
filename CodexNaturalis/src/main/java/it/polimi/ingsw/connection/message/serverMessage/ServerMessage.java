package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.MegaView;

import java.io.Serializable;

/**
 * ServerMessage class
 * @author Matteo Leonardo Luraghi
 */
public abstract class ServerMessage implements Serializable {
    private static final long serialVersionUID = -4496975448812853270L;
    private final Message type;

    /**
     * constructor
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
     * show the message
     * @param view the view interface
     */
    public abstract void show(MegaView view);
}
