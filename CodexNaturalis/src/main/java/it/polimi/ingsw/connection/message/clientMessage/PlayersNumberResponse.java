package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
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

    /**
     * Constructor
     * @param numPlayers number of players
     */
    public PlayersNumberResponse(int numPlayers) {
        this.numPlayers = numPlayers;
    }

    /**
     * Calls the server method to add the connectionHandler to a new game
     * and sends the client a WaitingForPlayer message
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        server.addToGame(connectionHandler, this.numPlayers);
    }
}
