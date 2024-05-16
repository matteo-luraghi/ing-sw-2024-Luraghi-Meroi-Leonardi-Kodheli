package it.polimi.ingsw.model.card;

import java.io.Serial;
import java.io.Serializable;

/**
 * class GoalCard
 * @author Lorenzo Meroi
 */
abstract public class GoalCard implements Serializable {
    @Serial
    private static final long serialVersionUID = -7316931387701617984L;
    private final int points;

    private final boolean isResourceGoal;
    private final int id;
    /**
     * Constructor
     * @param Points points given when the requirements are satisfied
     * @param IsResourceGoal type of GoalCard
     */
    public GoalCard(int Points, boolean IsResourceGoal, int id) {
        this.points = Points;
        this.isResourceGoal = IsResourceGoal;
        this.id=id;
    }

    /**
     * card id getter
     * @return int id of the card
     */
    public int getId() {
        return id;
    }

    /**
     * Points getter
     * @return Points
     */
    public int getPoints() {
        return points;
    }

    /**
     * IsResourceGoal getter
     * @return boolean describing the card type
     */
    public boolean isResourceGoal() {
        return isResourceGoal;
    }
}
