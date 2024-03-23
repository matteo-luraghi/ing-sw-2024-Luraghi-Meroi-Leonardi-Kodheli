package it.polimi.ingsw.model.card;

import java.util.ArrayList;

/**
 * class ResourceGoalCard
 * @author Lorenzo Meroi
 */
public class ResourceGoalCard extends GoalCard{
    private int Points;

    private boolean IsResourceGoal;

    private ArrayList<Resource> Requirements;

    /**
     * ResourceGoalCard constructor
     * @param Points given when the card is satisfied
     * @param requirements ArrayList of the Resources required to satisfy the goal
     * IsResourceGoal is always true for this class
     */
    public ResourceGoalCard (int Points, ArrayList<Resource> requirements) {
        super(Points, true);
        this.Requirements.addAll(requirements);
    }

    /**
     * Points getter
     * @return Points
     */
    @Override
    public int getPoints() {
        return Points;
    }

    /**
     * isResourceGoal getter
     * @return true
     */
    @Override
    public boolean isResourceGoal() {
        return IsResourceGoal;
    }

    /**
     * Requirements getter
     * @return Requirements
     */
    public ArrayList<Resource> getRequirements() {
        return Requirements;
    }
}
