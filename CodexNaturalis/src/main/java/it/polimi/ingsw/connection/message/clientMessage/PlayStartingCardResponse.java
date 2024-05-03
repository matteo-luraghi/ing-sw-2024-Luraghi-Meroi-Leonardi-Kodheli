package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Coordinates;

import java.io.Serial;

/**
 * PlayCardResponse class
 * used to tell the controller which card to play
 * @author Matteo Leonardo Luraghi
 */
public class PlayStartingCardResponse extends ClientMessage {
    @Serial
    private static final long serialVersionUID = -7671795239367074793L;
    private final StartingCard card;

    private boolean isFront;
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
    public void execute(Server server, ConnectionHandler connectionHandler) {
        connectionHandler.getController().setStartingCard(card, isFront, connectionHandler);
    }
}
