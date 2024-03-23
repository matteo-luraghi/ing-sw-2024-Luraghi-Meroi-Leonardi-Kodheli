package it.polimi.ingsw.model.card;

import java.util.ArrayList;

/**
 * class PositionGoalCard
 * @author Lorenzo Meroi
 */
public class PositionGoalCard extends GoalCard{
    private ArrayList<Direction> PositionsFromBase;

    private ArrayList<Kingdom> ResourceFromBase;

    /**
     * PositionGoalCard constructor
     * @param Points given when the requirements are satisfied
     * @param PositionsFromBase relative position of the cards from the lowest one on the game zone
     * @param ResourceFromBase kingdom of the cards required, corresponding to the PositionFromBase ArrayList
     * isResourceGoalCard is always false for this class
     */
    public PositionGoalCard (int Points, ArrayList<Direction> PositionsFromBase, ArrayList<Kingdom> ResourceFromBase) {
        super(Points, false);
        this.PositionsFromBase.addAll(PositionsFromBase);
        this.ResourceFromBase.addAll(ResourceFromBase);
    }

    /**
     * getPositionsFromBase getter
     * @return getPositionsFromBase
     */
    public ArrayList<Direction> getPositionsFromBase() {
        return PositionsFromBase;
    }

    /**
     * getResourceFromBase getter
     * @return getResourceFromBase
     */
    public ArrayList<Kingdom> getResourceFromBase() {
        return ResourceFromBase;
    }
}
