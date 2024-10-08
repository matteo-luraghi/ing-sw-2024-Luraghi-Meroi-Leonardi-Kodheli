package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.gamelogic.Coordinates;

import java.io.Serial;

/**
 * PlayCardResponse class
 * used to tell the controller which card to play
 * @author Matteo Leonardo Luraghi
 */
public class PlayCardResponse extends ClientMessage {
    @Serial
    private static final long serialVersionUID = -7671795239367074793L;
    private final ResourceCard card;
    private final Coordinates where;
    private final boolean isFront;

    /**
     * Constructor
     * @param card the card to play
     * @param where the coordinates
     * @param isFront specify the side of the card
     */
    public PlayCardResponse(ResourceCard card, Coordinates where, boolean isFront) {
        this.card = card;
        this.where = where;
        this.isFront = isFront;
    }

    /**
     * Try to play the card, if success go to the next state, otherwise ask for another card to play
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        connectionHandler.getController().playCard(connectionHandler, card, where, isFront);
    }
}
