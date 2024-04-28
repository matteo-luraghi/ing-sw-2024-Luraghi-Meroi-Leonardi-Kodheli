package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ClientHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.connection.message.serverMessage.DrawCardRequest;
import it.polimi.ingsw.connection.message.serverMessage.PlayCardRequest;
import it.polimi.ingsw.connection.message.serverMessage.TextMessage;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.gamelogic.Player;

public class PlayCardResponse extends ClientMessage {
    private static final long serialVersionUID = -7671795239367074793L;
    private ResourceCard card;

    public PlayCardResponse(ResourceCard card) {
        super(Message.PLAY_CARD_RESPONSE);
        this.card = card;
    }

    @Override
    public void execute(Server server, ClientHandler clientHandler) {
        if (clientHandler.getController().cardPlayed(clientHandler, card)) {
            clientHandler.getController().setTurnState(DRAW);
            clientHandler.sendMessageClient(new DrawCardRequest());
        } else {
            clientHandler.sendMessageClient(new TextMessage("Impossible to play the card, choose another one to play"));
            Player player = clientHandler.getController().getGame()
                    .getPlayers().stream().filter(p -> p.getNickname().equals(clientHandler.getClientNickname())).findFirst().get();
            clientHandler.sendMessageClient(new PlayCardRequest(player));
        }
    }
}
