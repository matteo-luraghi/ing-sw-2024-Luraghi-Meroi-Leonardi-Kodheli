package it.polimi.ingsw.model.card;

/**
 * class GoalCard
 * @author Lorenzo Meroi
 */
abstract public class GoalCard {
    private final int points;

    private final boolean isResourceGoal;

    /**
     * Constructor
     * @param Points points given when the requirements are satisfied
     * @param IsResourceGoal type of GoalCard
     */
    public GoalCard(int Points, boolean IsResourceGoal) {
        this.points = Points;
        this.isResourceGoal = IsResourceGoal;
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
