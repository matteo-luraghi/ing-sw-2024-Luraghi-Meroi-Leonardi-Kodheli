package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ClientHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.PlayerField;

import java.util.Map;

/**
 * GoalCardResponse class
 * used to send to the server the chosen goal card
 * @author Matteo Leonardo Luraghi
 */
public class GoalCardResponse extends ClientMessage{
    private static final long serialVersionUID = 8737646869053799287L;
    private final GoalCard goalCard;

    /**
     * Constructor, sets the message type as GOAL_CARD_RESPONSE and the goal card
     * @param goalCard the chosen goal card
     */
    public GoalCardResponse(GoalCard goalCard) {
        super(Message.GOAL_CARD_RESPONSE);
        this.goalCard = goalCard;
    }

    /**
     * Sets the chosen goal card in the player field corresponding to the client handler
     * @param server server to use
     * @param clientHandler client handler
     */
    @Override
    public void execute(Server server, ClientHandler clientHandler) {
        Map<Player, PlayerField> playerZones = clientHandler.getController().getGame().getGameTable().getPlayerZones();
        PlayerField field = playerZones.get(
                playerZones.keySet().stream()
                        .filter(player ->
                                player.getNickname().equals(clientHandler.getClientNickname()))
                        .findFirst().get());
        field.setPrivateGoal(this.goalCard);
    }
}
