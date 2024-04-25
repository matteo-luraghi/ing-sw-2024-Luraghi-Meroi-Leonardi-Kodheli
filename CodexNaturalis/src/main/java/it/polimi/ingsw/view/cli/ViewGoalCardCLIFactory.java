package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.view.mainview.AnsiColors;
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
        |     I     |  left Stair-type goal card
        |       I   |
        |___________|

         _____2_____
        |       I   |
        |     I     |  right Stair-type goal card
        |   I       |
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

            System.out.print(AnsiColors.ANSI_WHITE);
            System.out.println(" _____"+this.card.getPoints()+"_____ ");
            System.out.println("|           |");
            switch (requirements.size()) {
                case 2 -> {System.out.println("|    "+requirements.get(0).toString()+AnsiColors.ANSI_WHITE+" "+requirements.get(1).toString()+AnsiColors.ANSI_WHITE+"    |");}
                case 3 -> {System.out.println("|   "+requirements.get(0).toString()+AnsiColors.ANSI_WHITE+" "+requirements.get(1).toString()+AnsiColors.ANSI_WHITE+" "+requirements.get(2).toString()+AnsiColors.ANSI_WHITE+"   |");}
            }
            System.out.println("|           |");
            System.out.println("|___________|");
            System.out.print(AnsiColors.ANSI_RESET);
        } else {
            //TODO show a position goal card
            ArrayList<Direction> directions = ((PositionGoalCard) this.card).getPositionsFromBase();
            ArrayList<Kingdom> resources = ((PositionGoalCard) this.card).getResourceFromBase();

            System.out.print(AnsiColors.ANSI_WHITE);
            System.out.println(" _____"+this.card.getPoints()+"_____ ");

            if (directions.contains(Direction.TOP)) {
                //It is an L-type goal card
                if (directions.get(0).equals(Direction.TOP)) {
                    //It is an upside down L
                    if (directions.get(1).equals(Direction.TOP_RIGHT)) {
                        //it is facing eastward
                        System.out.println("|   " + resources.get(2) + AnsiColors.ANSI_WHITE + "       |");
                    } else {
                        //It is facing westward
                        System.out.println("|       " + resources.get(2) + AnsiColors.ANSI_WHITE + "   |");
                    }
                    System.out.println("|     " + resources.get(1) + AnsiColors.ANSI_WHITE + "     |");
                    System.out.println("|     " + resources.get(0) + AnsiColors.ANSI_WHITE + "     |");
                } else {
                    //It is an L
                    System.out.println("|     " + resources.get(2) + AnsiColors.ANSI_WHITE + "     |");
                    System.out.println("|     " + resources.get(1) + AnsiColors.ANSI_WHITE + "     |");
                    if (directions.get(0).equals(Direction.TOP_RIGHT)) {
                        //it is facing westward
                        System.out.println("|   " + resources.get(0) + AnsiColors.ANSI_WHITE + "       |");
                    } else {
                        //it is a normal L, facing eastward
                        System.out.println("|       " + resources.get(0) + AnsiColors.ANSI_WHITE + "   |");
                    }
                }
            } else {
                //It is a stair-type goal card
                if (directions.contains(Direction.TOP_RIGHT)) {
                    //it is a right stair
                    System.out.println("|       "+resources.get(2)+AnsiColors.ANSI_WHITE+"   |");
                    System.out.println("|     "+resources.get(1)+AnsiColors.ANSI_WHITE+"     |");
                    System.out.println("|   "+resources.get(0)+AnsiColors.ANSI_WHITE+"       |");
                } else {
                    //it is a left stair
                    System.out.println("|   "+resources.get(2)+AnsiColors.ANSI_WHITE+"       |");
                    System.out.println("|     "+resources.get(1)+AnsiColors.ANSI_WHITE+"     |");
                    System.out.println("|       "+resources.get(0)+AnsiColors.ANSI_WHITE+"   |");
                }
            }

            System.out.println("|___________|");
            System.out.print(AnsiColors.ANSI_RESET);
        }

    }
}
