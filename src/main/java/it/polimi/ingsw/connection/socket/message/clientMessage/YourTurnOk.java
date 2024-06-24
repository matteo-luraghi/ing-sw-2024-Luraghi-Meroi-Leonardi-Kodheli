package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.Server;

import java.io.Serial;

/**
 * YourTurnOk class
 * used to confirm the arrival of the YourTurn server message
 * @author Matteo Leonardo Luraghi
 */
public class YourTurnOk extends ClientMessage {
    @Serial
    private static final long serialVersionUID = -1379604143056042514L;

    /**
     * Constructor
     */
    public YourTurnOk() {}

    /**
     * Set the play card state in the controller
     * @param server            server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        connectionHandler.getController().playCardState();
    }
}
