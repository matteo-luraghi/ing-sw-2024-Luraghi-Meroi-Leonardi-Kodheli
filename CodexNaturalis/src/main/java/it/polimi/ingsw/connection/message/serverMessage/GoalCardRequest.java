package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.view.mainview.View;

/**
 * GoalCardRequest class
 * used to ask the player to choose a goal card
 * @author Matteo Leonardo Luraghi
 */
public class GoalCardRequest extends ServerMessage  {
    private static final long serialVersionUID = -338428875046753414L;
    private final GoalCard[] goalCards;

    /**
     * Constructor, sets the message type as GOAL_CARD_REQUEST
     * @param goalCards the possible goal cards the player has to choose from
     */
    public GoalCardRequest(GoalCard[] goalCards) {
        super(Message.GOAL_CARD_REQUEST);
        this.goalCards = goalCards;
    }

    /**
     * Show the possible options via TUI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowChoosePrivateGoal(goalCards);
    }
}
