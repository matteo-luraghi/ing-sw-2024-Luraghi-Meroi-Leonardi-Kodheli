package it.polimi.ingsw.model.card;

/**
 * class GoalCard
 * @author Lorenzo Meroi
 */
abstract public class GoalCard {
    private final int Points;

    private final boolean IsResourceGoal;

    /**
     * Constructor
     * @param Points points given when the requirements are satisfied
     * @param IsResourceGoal type of GoalCard
     */
    public GoalCard(int Points, boolean IsResourceGoal) {
        this.Points = Points;
        this.IsResourceGoal = IsResourceGoal;
    }

    /**
     * Points getter
     * @return Points
     */
    public int getPoints() {
        return Points;
    }

    /**
     * IsResourceGoal getter
     * @return boolean describing the card type
     */
    public boolean isResourceGoal() {
        return IsResourceGoal;
    }
}
