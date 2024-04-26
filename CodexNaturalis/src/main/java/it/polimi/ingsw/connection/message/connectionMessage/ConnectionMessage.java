package it.polimi.ingsw.connection.message.connectionMessage;

import it.polimi.ingsw.connection.message.Message;

import java.io.Serializable;

/**
 * ConnectionMessage class
 * @author Matteo Leonardo Luraghi
 */
public class ConnectionMessage implements Serializable {
    private static final long serialVersionUID = 8885896690210036828L;
    private final Message type;

    /**
     * constructor
     * @param type type of the message
     */
    public ConnectionMessage(Message type) {
        this.type = type;
    }

    /**
     * type getter
     * @return message type
     */
    public Message getType() {
        return this.type;
    }
}
