package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.model.card.StartingCard;

import java.io.Serial;

/**
 * PlayStartingCardResponse class
 * used to tell the controller which side to play the starting card
 * @author Gabriel Leonardi
 */
public class PlayStartingCardResponse extends ClientMessage {
    @Serial
    private static final long serialVersionUID = -2792935142620727036L;
    private final StartingCard card;
    private final boolean isFront;

    /**
     * Constructor
     * @param card the card to play
     * @param isFront true if the card has to be played front side up, false otherwise
     */
    public PlayStartingCardResponse(StartingCard card, boolean isFront) {
        this.card = card;
        this.isFront = isFront;
    }

    /**
     * Set the starting card facing the way the player asked
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        connectionHandler.getController().setStartingCard(card, isFront, connectionHandler);
    }
}
