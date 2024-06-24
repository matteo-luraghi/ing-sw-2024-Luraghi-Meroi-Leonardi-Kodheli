package it.polimi.ingsw.model.card;

import java.io.Serial;
import java.util.ArrayList;

/**
 * class ResourceGoalCard
 * @author Lorenzo Meroi
 */
public class ResourceGoalCard extends GoalCard{
    @Serial
    private static final long serialVersionUID = 7547895155229358446L;
    private final ArrayList<Resource> requirements;

    /**
     * ResourceGoalCard constructor
     * @param Points given when the card is satisfied
     * @param requirements ArrayList of the Resources required to satisfy the goal
     * @param id the card's id
     * IsResourceGoal is always true for this class
     */
    public ResourceGoalCard (int Points, ArrayList<Resource> requirements, int id) {
        super(Points, true, id);
        this.requirements = new ArrayList<>();
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
