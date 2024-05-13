package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.card.GameCard;
import it.polimi.ingsw.model.card.GoalCard;

import java.io.Serial;
import java.io.Serializable;

/**
 * viewGoalCardFactory abstract class
 * @author Lorenzo Meroi
 */
public abstract class ViewGoalCardFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = -7303179025211742813L;
    protected GoalCard card;

    /**
     * abstract method to show on screen a goal card
     */
    public abstract void Show();

    /**
     * method to set which card to show
     * @param goalCard is the card you want to be shown
     */
    public void SetCard(GoalCard goalCard) {
        this.card = goalCard;
    }
}
