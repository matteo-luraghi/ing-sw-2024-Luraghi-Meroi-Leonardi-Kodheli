package it.polimi.ingsw.model.card;

import java.util.ArrayList;

/**
 * class ResourceGoalCard
 * @author Lorenzo Meroi
 */
public class ResourceGoalCard extends GoalCard{
    private ArrayList<Resource> requirements;

    /**
     * ResourceGoalCard constructor
     * @param Points given when the card is satisfied
     * @param requirements ArrayList of the Resources required to satisfy the goal
     * IsResourceGoal is always true for this class
     */
    public ResourceGoalCard (int Points, ArrayList<Resource> requirements) {
        super(Points, true);
        requirements = new ArrayList<>();
        this.requirements.addAll(requirements);
    }

    /**
     * Requirements getter
     * @return Requirements
     */
    public ArrayList<Resource> getRequirements() {
        return requirements;
    }
}
