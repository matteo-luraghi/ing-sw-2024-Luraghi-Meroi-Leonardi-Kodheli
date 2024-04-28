package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.Message;

import java.io.Serializable;

/**
 * ClientMessage class
 * @author Matteo Leonardo Luraghi
 */
public abstract class ClientMessage implements Serializable {
    private static final long serialVersionUID = -748320518806449772L;
    private final Message type;

    /**
     * constructor
     * @param type type of the message
     */
    public ClientMessage(Message type) {
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
     * Execute the specific action based on message
     * @param server server to use
     * @param connectionHandler client handler
     */
    public abstract void execute(Server server, ConnectionHandler connectionHandler);
}
