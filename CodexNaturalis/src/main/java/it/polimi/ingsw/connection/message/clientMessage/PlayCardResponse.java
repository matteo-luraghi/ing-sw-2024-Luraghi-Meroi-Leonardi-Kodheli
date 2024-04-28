package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.serverMessage.DrawCardRequest;
import it.polimi.ingsw.connection.message.serverMessage.PlayCardRequest;
import it.polimi.ingsw.connection.message.serverMessage.TextMessage;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.gamelogic.Player;

/**
 * PlayCardResponse class
 * used to tell the controller which card to play
 * @author Matteo Leonardo Luraghi
 */
public class PlayCardResponse extends ClientMessage {
    private static final long serialVersionUID = -7671795239367074793L;
    private final ResourceCard card;

    /**
     * Constructor
     * @param card the card to play
     */
    public PlayCardResponse(ResourceCard card) {
        this.card = card;
    }

    /**
     * Try to play the card, if success go to the next state, otherwise ask for another card to play
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        if (connectionHandler.getController().cardPlayed(connectionHandler, card)) {
            connectionHandler.getController().setTurnState(DRAW);
            connectionHandler.sendMessageClient(new DrawCardRequest(connectionHandler.getController().getGame()));
        } else {
            connectionHandler.sendMessageClient(new TextMessage("Impossible to play the card, choose another one to play"));
            Player player = connectionHandler.getController().getGame()
                    .getPlayers().stream().filter(p -> p.getNickname().equals(connectionHandler.getClientNickname())).findFirst().get();
            connectionHandler.sendMessageClient(new PlayCardRequest(player, connectionHandler.getController().getGame()));
        }
    }
}
