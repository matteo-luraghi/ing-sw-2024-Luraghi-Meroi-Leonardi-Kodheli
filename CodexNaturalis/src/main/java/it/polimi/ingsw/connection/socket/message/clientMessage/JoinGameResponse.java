package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.socket.message.serverMessage.JoinGameRequest;

import java.io.Serial;

public class JoinGameResponse extends ClientMessage{
    @Serial
    private static final long serialVersionUID = 3108231749238138153L;
    private final boolean isJoin;
    private final String gameName;

    public JoinGameResponse(boolean isJoin, String gameName) {
        this.isJoin = isJoin;
        this.gameName = gameName;
    }

    /**
     * Execute the specific action based on message
     *
     * @param server            server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        connectionHandler.nicknameRequest(this.isJoin, this.gameName);
    }
}
