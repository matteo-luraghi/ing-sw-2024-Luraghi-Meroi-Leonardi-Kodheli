package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
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
     * Constructor
     * @param goalCard the chosen goal card
     */
    public GoalCardResponse(GoalCard goalCard) {
        this.goalCard = goalCard;
    }

    /**
     * Sets the chosen goal card in the player field corresponding to the client handler
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        connectionHandler.getController().setPrivateGoalCard(this.goalCard, connectionHandler);
    }
}
