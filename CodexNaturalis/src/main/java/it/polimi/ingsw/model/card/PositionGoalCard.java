package it.polimi.ingsw.model.card;

import java.io.Serial;
import java.util.ArrayList;

/**
 * class PositionGoalCard
 * @author Lorenzo Meroi
 */
public class PositionGoalCard extends GoalCard{
    @Serial
    private static final long serialVersionUID = 7206421742827841283L;
    private final ArrayList<Direction> positionsFromBase;

    private final ArrayList<Kingdom> resourceFromBase;

    /**
     * PositionGoalCard constructor
     * @param Points given when the requirements are satisfied
     * @param PositionsFromBase relative position of the cards from the lowest one on the game zone
     * @param ResourceFromBase kingdom of the cards required, corresponding to the PositionFromBase ArrayList
     * isResourceGoalCard is always false for this class
     */
    public PositionGoalCard (int Points, ArrayList<Direction> PositionsFromBase, ArrayList<Kingdom> ResourceFromBase) {
        super(Points, false);
        positionsFromBase = new ArrayList<>();
        this.positionsFromBase.addAll(PositionsFromBase);
        resourceFromBase = new ArrayList<>();
        this.resourceFromBase.addAll(ResourceFromBase);
    }

    /**
     * getPositionsFromBase getter
     * @return getPositionsFromBase
     */
    public ArrayList<Direction> getPositionsFromBase() {
        return positionsFromBase;
    }

    /**
     * getResourceFromBase getter
     * @return getResourceFromBase
     */
    public ArrayList<Kingdom> getResourceFromBase() {
        return resourceFromBase;
    }
}
