package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.SocketConnectionHandler;
import it.polimi.ingsw.connection.Server;

import java.io.Serial;
import java.io.Serializable;

/**
 * ClientMessage class
 * @author Matteo Leonardo Luraghi
 */
public abstract class ClientMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = -748320518806449772L;

    /**
     * constructor
     */
    public ClientMessage() {
    }

    /**
     * Execute the specific action based on message
     * @param server server to use
     * @param connectionHandler client handler
     */
    public abstract void execute(Server server, SocketConnectionHandler connectionHandler);
}
