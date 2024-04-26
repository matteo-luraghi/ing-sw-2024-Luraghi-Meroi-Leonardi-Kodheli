package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ClientHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.connection.message.serverMessage.ColorRequest;

/**
 * PlayersNumberResponse class
 * used to send to the server the number of players for a new game
 * @author Matteo Leonardo Luraghi
 */
public class PlayersNumberResponse extends ClientMessage{
    private static final long serialVersionUID = -4677442179211411089L;
    private final int numPlayers;

    /**
     * Constructor, sets the message type as PLAYER_NUMBER_RESPONSE and sets the number of players
     * @param numPlayers number of players
     */
    public PlayersNumberResponse(int numPlayers) {
        super(Message.PLAYERS_NUMBER_RESPONSE);
        this.numPlayers = numPlayers;
    }

    /**
     * Calls the server method to add the clientHandler to a new game
     * and sends the client a WaitingForPlayer message
     * @param server server to use
     * @param clientHandler client handler
     */
    @Override
    public void execute(Server server, ClientHandler clientHandler) {
        server.addToGame(clientHandler, this.numPlayers);
    }
}
