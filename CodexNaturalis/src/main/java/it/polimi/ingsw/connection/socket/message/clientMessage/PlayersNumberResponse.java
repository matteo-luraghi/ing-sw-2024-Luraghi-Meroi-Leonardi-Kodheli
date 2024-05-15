package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.Server;

import java.io.Serial;

/**
 * PlayersNumberResponse class
 * used to send to the server the number of players for a new game
 * @author Matteo Leonardo Luraghi
 */
public class PlayersNumberResponse extends ClientMessage{
    @Serial
    private static final long serialVersionUID = -4677442179211411089L;
    private final int numPlayers;
    private final String gameName;

    /**
     * Constructor
     * @param numPlayers number of players
     */
    public PlayersNumberResponse(int numPlayers, String gameName) {
        this.numPlayers = numPlayers;
        this.gameName = gameName;
    }

    /**
     * Calls the server method to add the connectionHandler to a new game
     * and sends the client a WaitingForPlayer message
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        server.createGame(connectionHandler, this.numPlayers, this.gameName);
    }
}
