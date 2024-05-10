package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.Server;

import java.io.Serial;

public class YourTurnOk extends ClientMessage {
    @Serial
    private static final long serialVersionUID = -1379604143056042514L;

    public YourTurnOk() {}

    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        connectionHandler.getController().playCardState();
    }
}
