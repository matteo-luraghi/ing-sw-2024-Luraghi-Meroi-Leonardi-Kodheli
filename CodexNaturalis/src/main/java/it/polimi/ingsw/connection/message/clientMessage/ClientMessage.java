package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ClientHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.Message;

import java.io.Serializable;

/**
 * ClientMessage class
 * @author Matteo Leonardo Luraghi
 */
public abstract class ClientMessage implements Serializable {
    private static final long serialVersionUID = 6277215629364450602L;
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
     * @param clientHandler client handler
     */
    public abstract void execute(Server server, ClientHandler clientHandler);
}
