package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.serverMessage.TextMessage;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.gamelogic.Coordinates;

/**
 * PlayCardResponse class
 * used to tell the controller which card to play
 * @author Matteo Leonardo Luraghi
 */
public class PlayCardResponse extends ClientMessage {
    private static final long serialVersionUID = -7671795239367074793L;
    private final ResourceCard card;
    private final Coordinates where;

    /**
     * Constructor
     * @param card the card to play
     */
    public PlayCardResponse(ResourceCard card, Coordinates where) {
        this.card = card;
        this.where = where;
    }

    /**
     * Try to play the card, if success go to the next state, otherwise ask for another card to play
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        if (connectionHandler.getController().cardPlayed(connectionHandler, card, where)) {
            // set the state of the controller to draw card
            connectionHandler.getController().drawCardState();
        } else {
            connectionHandler.sendMessageClient(new TextMessage("Impossible to play the card, choose another one to play"));
            // set the state of the controller to play a card
            connectionHandler.getController().playCardState();
        }
    }
}
