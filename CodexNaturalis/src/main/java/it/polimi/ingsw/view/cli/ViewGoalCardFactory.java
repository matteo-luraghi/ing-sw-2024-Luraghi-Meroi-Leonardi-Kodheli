package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.view.mainview.AnsiColors;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * ViewGoalCardFactory class
 * @author Lorenzo Meroi
 */
public class ViewGoalCardFactory implements Serializable {
    @Serial
    private static final long serialVersionUID = -7303179025211742813L;
    protected GoalCard card;

    /**
     * method to set which card to show
     * @param goalCard is the card you want to be shown
     */
    public void SetCard(GoalCard goalCard) {
        this.card = goalCard;
    }

    /**
     * abstract method to show on screen a goal card
     */
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
            ArrayList<Resource> requirements = ((ResourceGoalCard) this.card).getRequirements();

            System.out.print(AnsiColors.ANSI_WHITE);
            System.out.println(" _____"+AnsiColors.ANSI_WHITE+this.card.getPoints()+AnsiColors.ANSI_WHITE+"_____ ");
            System.out.println("|           |");
            switch (requirements.size()) {
                case 2 -> {System.out.println("|    "+requirements.get(0).toString()+AnsiColors.ANSI_WHITE+" "+requirements.get(1).toString()+AnsiColors.ANSI_WHITE+"    | Satisfy the goal for every pair of this resources in your field");}
                case 3 -> {System.out.println("|   "+requirements.get(0).toString()+AnsiColors.ANSI_WHITE+" "+requirements.get(1).toString()+AnsiColors.ANSI_WHITE+" "+requirements.get(2).toString()+AnsiColors.ANSI_WHITE+"   | Satisfy the goal for every three of this resources in your field");}
            }
            System.out.println("|           |");
            System.out.println("|___________|");
            System.out.print(AnsiColors.ANSI_RESET);
        } else {
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
                        System.out.println("|       " + resources.get(2).toString() + AnsiColors.ANSI_WHITE + "   |");
                    } else {
                        //It is facing westward
                        System.out.println("|   " + resources.get(2).toString() + AnsiColors.ANSI_WHITE + "       |");
                    }
                    System.out.println("|     " + resources.get(1).toString() + AnsiColors.ANSI_WHITE + "     | Have this specific L-shape in your field to satisfy the goal");
                    System.out.println("|     " + resources.get(0).toString() + AnsiColors.ANSI_WHITE + "     |");
                } else {
                    //It is an L
                    System.out.println("|     " + resources.get(2).toString() + AnsiColors.ANSI_WHITE + "     |");
                    System.out.println("|     " + resources.get(1).toString() + AnsiColors.ANSI_WHITE + "     | Have this specific L-shape in your field to satisfy the goal");
                    if (directions.get(0).equals(Direction.TOP_RIGHT)) {
                        //it is facing westward
                        System.out.println("|   " + resources.get(0).toString() + AnsiColors.ANSI_WHITE + "       |");
                    } else {
                        //it is a normal L, facing eastward
                        System.out.println("|       " + resources.get(0).toString() + AnsiColors.ANSI_WHITE + "   |");
                    }
                }
            } else {
                //It is a stair-type goal card
                if (directions.contains(Direction.TOP_RIGHT)) {
                    //it is a right stair
                    System.out.println("|       "+resources.get(2).toString()+AnsiColors.ANSI_WHITE+"   |");
                    System.out.println("|     "+resources.get(1).toString()+AnsiColors.ANSI_WHITE+"     | Have this specific stair-shape in your field to satisfy the goal");
                    System.out.println("|   "+resources.get(0).toString()+AnsiColors.ANSI_WHITE+"       |");
                } else {
                    //it is a left stair
                    System.out.println("|   "+resources.get(2).toString()+AnsiColors.ANSI_WHITE+"       |");
                    System.out.println("|     "+resources.get(1).toString()+AnsiColors.ANSI_WHITE+"     | Have this specific stair-shape in your field to satisfy the goal");
                    System.out.println("|       "+resources.get(0).toString()+AnsiColors.ANSI_WHITE+"   |");
                }
            }

            System.out.println("|___________|");
            System.out.print(AnsiColors.ANSI_RESET);
        }

    }

    /**
     * method to print the first line of a card
     */
    public void printFirstCardLine() {
        System.out.print(AnsiColors.ANSI_WHITE);
        System.out.print(" _____"+this.card.getPoints()+"_____ ");
    }

    /**
     * method to print the second line of a card
     */
    public void printSecondCardLine() {
        System.out.print(AnsiColors.ANSI_WHITE);
        if (this.card.isResourceGoal()) {
            System.out.print("|           |");
        } else {
            ArrayList<Direction> directions = ((PositionGoalCard) this.card).getPositionsFromBase();
            ArrayList<Kingdom> resources = ((PositionGoalCard) this.card).getResourceFromBase();

            if (directions.contains(Direction.TOP)) {
                if (directions.get(0).equals(Direction.TOP)) {
                    if (directions.get(1).equals(Direction.TOP_RIGHT)) {
                        System.out.print("|       " + resources.get(2).toString() + AnsiColors.ANSI_WHITE + "   |" + AnsiColors.ANSI_RESET);
                    } else {
                        System.out.print("|   " + resources.get(2).toString() + AnsiColors.ANSI_WHITE + "       |" + AnsiColors.ANSI_RESET);
                    }
                } else {
                    System.out.print("|     " + resources.get(2).toString() + AnsiColors.ANSI_WHITE + "     |" + AnsiColors.ANSI_RESET);
                }
            } else {
                if (directions.contains(Direction.TOP_RIGHT)) {
                    System.out.print("|       "+resources.get(2).toString()+AnsiColors.ANSI_WHITE+"   |" + AnsiColors.ANSI_RESET);
                } else {
                    System.out.print("|   "+resources.get(2).toString()+AnsiColors.ANSI_WHITE+"       |" + AnsiColors.ANSI_RESET);
                }
            }
        }
    }

    /**
     * method to print the third line of a card
     */
    public void printThirdCardLine() {
        System.out.print(AnsiColors.ANSI_WHITE);
        if (this.card.isResourceGoal()) {
            ArrayList<Resource> requirements = ((ResourceGoalCard) this.card).getRequirements();
            switch (requirements.size()) {
                case 2 -> {System.out.print("|    "+requirements.get(0).toString()+AnsiColors.ANSI_WHITE+" "+requirements.get(1).toString()+AnsiColors.ANSI_WHITE+"    |");}
                case 3 -> {System.out.print("|   "+requirements.get(0).toString()+AnsiColors.ANSI_WHITE+" "+requirements.get(1).toString()+AnsiColors.ANSI_WHITE+" "+requirements.get(2).toString()+AnsiColors.ANSI_WHITE+"   |");}
            }
        } else {
            ArrayList<Direction> directions = ((PositionGoalCard) this.card).getPositionsFromBase();
            ArrayList<Kingdom> resources = ((PositionGoalCard) this.card).getResourceFromBase();
            if (directions.contains(Direction.TOP)) {
                if (directions.get(0).equals(Direction.TOP)) {
                    System.out.print("|     " + resources.get(1).toString() + AnsiColors.ANSI_WHITE + "     |" + AnsiColors.ANSI_RESET);
                } else {
                    System.out.print("|     " + resources.get(1).toString() + AnsiColors.ANSI_WHITE + "     |" + AnsiColors.ANSI_RESET);
                }
            } else {
                if (directions.contains(Direction.TOP_RIGHT)) {
                    System.out.print("|     " + resources.get(1).toString() + AnsiColors.ANSI_WHITE + "     |" + AnsiColors.ANSI_RESET);
                } else {
                    System.out.print("|     " + resources.get(1).toString() + AnsiColors.ANSI_WHITE + "     |" + AnsiColors.ANSI_RESET);
                }
            }
        }
    }

    /**
     * method to print the fourth line of a card
     */
    public void printFourthCardLine() {
        System.out.print(AnsiColors.ANSI_WHITE);
        if (this.card.isResourceGoal()) {
            System.out.print("|           |");
        } else {
            ArrayList<Direction> directions = ((PositionGoalCard) this.card).getPositionsFromBase();
            ArrayList<Kingdom> resources = ((PositionGoalCard) this.card).getResourceFromBase();
            if (directions.contains(Direction.TOP)) {
                if (directions.get(0).equals(Direction.TOP)) {
                    System.out.print("|     " + resources.get(0).toString() + AnsiColors.ANSI_WHITE + "     |" + AnsiColors.ANSI_RESET);
                } else {
                    if (directions.get(0).equals(Direction.TOP_RIGHT)) {
                        System.out.print("|   " + resources.get(0).toString() + AnsiColors.ANSI_WHITE + "       |" + AnsiColors.ANSI_RESET);
                    } else {
                        System.out.print("|       " + resources.get(0).toString() + AnsiColors.ANSI_WHITE + "   |" + AnsiColors.ANSI_RESET);
                    }
                }
            } else {
                if (directions.contains(Direction.TOP_RIGHT)) {
                    System.out.print("|   "+resources.get(0).toString()+AnsiColors.ANSI_WHITE+"       |" + AnsiColors.ANSI_RESET);
                } else {
                    System.out.print("|       "+resources.get(0).toString()+AnsiColors.ANSI_WHITE+"   |" + AnsiColors.ANSI_RESET);
                }
            }
        }
    }

    /**
     * method to print the fifth line of a card
     */
    public void printFifthCardLine() {
        System.out.print(AnsiColors.ANSI_WHITE);
        System.out.print("|___________|" + AnsiColors.ANSI_RESET);
    }

    /**
     * method to print a single goal card
     * @param one is the first card
     *
     */
    public void printGoalCard(GoalCard one) {
        SetCard(one);
        printFirstCardLine();
        System.out.println();

        SetCard(one);
        printSecondCardLine();
        System.out.println();

        SetCard(one);
        printThirdCardLine();

        System.out.println();

        SetCard(one);
        printFourthCardLine();
        System.out.println();

        SetCard(one);
        printFifthCardLine();
        System.out.println();
    }
    /**
     * method to print two goal card next to one another
     * @param one is the first card
     * @param two is the second card
     */
    public void printTwoCards(GoalCard one, GoalCard two) {
        SetCard(one);
        printFirstCardLine();
        System.out.print("   ");
        SetCard(two);
        printFirstCardLine();
        System.out.println();

        SetCard(one);
        printSecondCardLine();
        System.out.print("   ");
        SetCard(two);
        printSecondCardLine();
        System.out.println();

        SetCard(one);
        printThirdCardLine();
        System.out.print("   ");
        SetCard(two);
        printThirdCardLine();
        System.out.println();

        SetCard(one);
        printFourthCardLine();
        System.out.print("   ");
        SetCard(two);
        printFourthCardLine();
        System.out.println();

        SetCard(one);
        printFifthCardLine();
        System.out.print("   ");
        SetCard(two);
        printFifthCardLine();
        System.out.println();
    }
}
