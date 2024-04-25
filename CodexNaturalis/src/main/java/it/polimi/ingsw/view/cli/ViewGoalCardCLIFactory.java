package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.Direction;
import it.polimi.ingsw.model.card.PositionGoalCard;
import it.polimi.ingsw.model.card.Resource;
import it.polimi.ingsw.model.card.ResourceGoalCard;
import it.polimi.ingsw.view.mainview.ViewGoalCardFactory;

import java.util.ArrayList;

public class ViewGoalCardCLIFactory extends ViewGoalCardFactory {

    /**
     * abstract method to show on screen a goal card
     */
    @Override
    public void Show() {
        /*
        Example of how a goalcard is shown
         _____3_____
        |   A       |
        |     I     |  L-type goal card
        |     I     |
        |___________|

         _____2_____
        |   I       |
        |     I     |  Stair-type goal card
        |       I   |
        |___________|

         _____2_____
        |           |
        |   I I I   | Natural Resource-type goal card
        |           |
        |___________|

         _____3_____
        |           |
        |    S S    | Gold Resource-type goal card
        |   R T S   |
        |___________|
         */

        if (this.card.isResourceGoal()) {
            //TODO show a resource goal card
            ArrayList<Resource> requirements = ((ResourceGoalCard) this.card).getRequirements();
            if (requirements.get(0).equals(Resource.FEATHER) || requirements.get(0).equals(Resource.SCROLL) || requirements.get(0).equals(Resource.POTION)) {
                //It is a Gold Resource-type goal card
            } else {
                //it is a Natural Resource-type goal card
            }
        } else {
            //TODO show a position goal card
            ArrayList<Direction> directions = ((PositionGoalCard) this.card).getPositionsFromBase();
            if (directions.contains(Direction.TOP)) {
                //It is an L-type goal card
            } else {
                //It is a stair-type goal card
            }
        }

    }
}
