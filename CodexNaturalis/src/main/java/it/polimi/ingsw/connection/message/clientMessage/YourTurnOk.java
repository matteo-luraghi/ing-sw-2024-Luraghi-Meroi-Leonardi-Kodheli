package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;

import java.io.Serial;

public class YourTurnOk extends ClientMessage {
    @Serial
    private static final long serialVersionUID = -1379604143056042514L;

    public YourTurnOk() {}

    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        connectionHandler.getController().playCardState();
    }
}
