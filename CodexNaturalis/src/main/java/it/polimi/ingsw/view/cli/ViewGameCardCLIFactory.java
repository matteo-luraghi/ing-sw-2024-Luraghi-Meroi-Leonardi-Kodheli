package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.view.mainview.ViewGameCardFactory;
import it.polimi.ingsw.view.mainview.AnsiColors;

import java.util.ArrayList;

/**
 * ViewGameCardCLIFactory class
 * @author Lorenzo Meroi
 */
public class ViewGameCardCLIFactory extends ViewGameCardFactory {
    /**
     * method to show on screen a game card
     */
    @Override
    public void Show() {
        /*
         Gold      A
         ____2_S____
        |I         A|
        |    AAA    |
        |P         F|
        |___FFFFF___|

        Example of how a card is shown*/


        if(this.card instanceof StartingCard) {
            System.out.print(AnsiColors.ANSI_WHITE);
            System.out.println("  Starting  ");
            System.out.println(" ___________");
            System.out.println("|" + this.card.getCorner(0).toString()+ AnsiColors.ANSI_WHITE + "         " + this.card.getCorner(1).toString()+ AnsiColors.ANSI_WHITE + "|");

            if (((StartingCard) card).getIsFront()) {
                Resource[] PermanentResources = ((StartingCard) this.card).getPermanentResources();
                switch (((StartingCard) this.card).getPermanentResources().length) {
                    case 1 -> {System.out.println("|     " + PermanentResources[0].toString() + AnsiColors.ANSI_WHITE + "     |");}
                    case 2 -> {System.out.println("|    " + PermanentResources[0].toString() + AnsiColors.ANSI_WHITE + " " + PermanentResources[1].toString() + AnsiColors.ANSI_WHITE + "    |");}
                    case 3 -> {System.out.println("|    " + PermanentResources[0].toString() + AnsiColors.ANSI_WHITE + PermanentResources[1].toString() + AnsiColors.ANSI_WHITE + PermanentResources[2].toString() + AnsiColors.ANSI_WHITE + "    |");}
                }
            } else {
                System.out.println("|           |");
            }

            System.out.println("|"+this.card.getCorner(2).toString()+ AnsiColors.ANSI_WHITE + "         " + this.card.getCorner(3).toString()+ AnsiColors.ANSI_WHITE+"|");
            System.out.println("|___________|"+AnsiColors.ANSI_RESET);
        }  else if (this.card instanceof GoldCard) {

            if (!this.card.getIsFront()) {
                System.out.print(AnsiColors.ANSI_YELLOW);
                System.out.println(" Gold      "+this.card.getKingdom().toString()+AnsiColors.ANSI_YELLOW);
                System.out.println(" ___________");
                System.out.println("|" + Resource.BLANK.toString()+AnsiColors.ANSI_YELLOW + "         " + Resource.BLANK.toString()+AnsiColors.ANSI_YELLOW + "|");
                System.out.println("|     "+this.card.getKingdom().toString()+AnsiColors.ANSI_YELLOW+"     |");

                System.out.println("|"+Resource.BLANK.toString()+AnsiColors.ANSI_YELLOW + "         " + Resource.BLANK.toString()+AnsiColors.ANSI_YELLOW+"|");
                System.out.println("|___________|"+ AnsiColors.ANSI_RESET);
            } else {
                System.out.println(" Gold      "+this.card.getKingdom().toString()+AnsiColors.ANSI_YELLOW);
                System.out.println(" ____"+AnsiColors.ANSI_WHITE+((GoldCard) this.card).getPointCondition().toString()+AnsiColors.ANSI_YELLOW+"_"+AnsiColors.ANSI_WHITE+((GoldCard) this.card).getPoints()+AnsiColors.ANSI_YELLOW+"____");
                System.out.println("|" + this.card.getCorner(0).toString()+AnsiColors.ANSI_YELLOW + "         " + this.card.getCorner(1).toString()+AnsiColors.ANSI_YELLOW + "|");
                System.out.println("|           |");
                System.out.println("|"+this.card.getCorner(2).toString()+AnsiColors.ANSI_YELLOW + "         " + this.card.getCorner(3).toString()+AnsiColors.ANSI_YELLOW+"|");

                ArrayList<Resource> PlayableCondition = ((GoldCard) this.card).getPlayableCondition();
                switch (PlayableCondition.size()) {
                    case 3 -> {System.out.println("|____"+PlayableCondition.get(0).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(1).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(2).toString()+AnsiColors.ANSI_YELLOW+"____|");}
                    case 4 -> {System.out.println("|___"+PlayableCondition.get(0).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(1).toString()+AnsiColors.ANSI_YELLOW+"_"+PlayableCondition.get(2).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(3).toString()+AnsiColors.ANSI_YELLOW+"___|");}
                    case 5 -> {System.out.println("|___"+PlayableCondition.get(0).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(1).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(2).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(3).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(4).toString()+AnsiColors.ANSI_YELLOW+"___|");}
                }
                System.out.print(AnsiColors.ANSI_RESET);
            }

        } else if (this.card instanceof ResourceCard) {

            if (!this.card.getIsFront()) {
                System.out.print(AnsiColors.ANSI_RESET);
                System.out.println(" Resource  "+this.card.getKingdom().toString()+AnsiColors.ANSI_RESET);
                System.out.println(" ___________");
                System.out.println("|" + Resource.BLANK.toString()+AnsiColors.ANSI_RESET + "         " + Resource.BLANK.toString()+AnsiColors.ANSI_RESET + "|");
                System.out.println("|     "+this.card.getKingdom().toString()+AnsiColors.ANSI_RESET+"     |");

                System.out.println("|"+Resource.BLANK.toString()+AnsiColors.ANSI_RESET + "         " + Resource.BLANK.toString()+AnsiColors.ANSI_RESET+"|");
                System.out.println("|___________|"+AnsiColors.ANSI_RESET);
            } else {
                System.out.println(" Resource  "+this.card.getKingdom().toString()+AnsiColors.ANSI_RESET);
                if (((ResourceCard) this.card).getPoints()==0)
                    System.out.println(" ___________");
                else
                    System.out.println(" _____"+AnsiColors.ANSI_WHITE+((ResourceCard) this.card).getPoints()+AnsiColors.ANSI_RESET+"_____");
                System.out.println("|" + this.card.getCorner(0).toString()+AnsiColors.ANSI_RESET + "         " + this.card.getCorner(1).toString()+AnsiColors.ANSI_RESET + "|");
                System.out.println("|           |");
                System.out.println("|"+this.card.getCorner(2).toString()+AnsiColors.ANSI_RESET + "         " + this.card.getCorner(3).toString()+AnsiColors.ANSI_RESET+"|");
                System.out.println("|___________|"+AnsiColors.ANSI_RESET);
            }
        }
    }

    public void printFirstCardLine() {
        if (this.card instanceof StartingCard) {
            System.out.print(AnsiColors.ANSI_WHITE);
            System.out.print("  Starting   "+ AnsiColors.ANSI_RESET);
        } else if (this.card instanceof GoldCard) {
            System.out.print(AnsiColors.ANSI_YELLOW);
            System.out.print(" Gold      "+this.card.getKingdom().toString()+AnsiColors.ANSI_RESET + " ");
        } else {
            System.out.print(AnsiColors.ANSI_RESET);
            System.out.print(" Resource  "+this.card.getKingdom().toString()+AnsiColors.ANSI_RESET + " ");
        }
    }

    /*if (this.card instanceof StartingCard) {
        System.out.print(AnsiColors.ANSI_WHITE);
    } else if (this.card instanceof GoldCard) {
        System.out.print(AnsiColors.ANSI_YELLOW);
        if (!this.card.getIsFront()) {

        } else {

        }
    } else {
        System.out.print(AnsiColors.ANSI_RESET);
        if (!this.card.getIsFront()) {

        } else {

        }
    }*/

    public void printSecondCardLine() {
        if (this.card instanceof StartingCard) {
            System.out.print(AnsiColors.ANSI_WHITE);
            System.out.print(" ___________ ");
        } else if (this.card instanceof GoldCard) {
            System.out.print(AnsiColors.ANSI_YELLOW);
            if (!this.card.getIsFront()) {
                System.out.print(" ___________ ");
            } else {
                System.out.print(" ____"+AnsiColors.ANSI_WHITE+((GoldCard) this.card).getPointCondition().toString()+AnsiColors.ANSI_YELLOW+"_"+AnsiColors.ANSI_WHITE+((GoldCard) this.card).getPoints()+AnsiColors.ANSI_YELLOW+"____ "+AnsiColors.ANSI_RESET);
            }
        } else {
            System.out.print(AnsiColors.ANSI_RESET);
            if (!this.card.getIsFront()) {
                System.out.print(" ___________ ");
            } else {
                if (((ResourceCard) this.card).getPoints()==0)
                    System.out.print(" ___________ ");
                else
                    System.out.print(" _____"+AnsiColors.ANSI_WHITE+((ResourceCard) this.card).getPoints()+AnsiColors.ANSI_RESET+"_____ ");
            }
        }
    }

    public void printThirdCardLine() {
        if (this.card instanceof StartingCard) {
            System.out.print(AnsiColors.ANSI_WHITE);
            System.out.print("|" + this.card.getCorner(0).toString()+ AnsiColors.ANSI_WHITE + "         " + this.card.getCorner(1).toString()+ AnsiColors.ANSI_WHITE + "|" + AnsiColors.ANSI_RESET);
        } else if (this.card instanceof GoldCard) {
            System.out.print(AnsiColors.ANSI_YELLOW);
            if (!this.card.getIsFront()) {
                System.out.print("|" + Resource.BLANK.toString()+AnsiColors.ANSI_YELLOW + "         " + Resource.BLANK.toString()+AnsiColors.ANSI_YELLOW + "|" + AnsiColors.ANSI_RESET);
            } else {
                System.out.print("|" + this.card.getCorner(0).toString()+AnsiColors.ANSI_YELLOW + "         " + this.card.getCorner(1).toString()+AnsiColors.ANSI_YELLOW + "|" + AnsiColors.ANSI_RESET);
            }
        } else {
            System.out.print(AnsiColors.ANSI_RESET);
            if (this.card.getIsFront()) {
                System.out.print("|" + Resource.BLANK.toString()+AnsiColors.ANSI_RESET + "         " + Resource.BLANK.toString()+AnsiColors.ANSI_RESET + "|");
            } else {
                System.out.print("|" + this.card.getCorner(0).toString()+AnsiColors.ANSI_RESET + "         " + this.card.getCorner(1).toString()+AnsiColors.ANSI_RESET + "|");
            }
        }
    }

    public void printFourthCardLine () {
        if (this.card instanceof StartingCard) {
            System.out.print(AnsiColors.ANSI_WHITE);
            if (card.getIsFront()) {
                Resource[] PermanentResources = ((StartingCard) this.card).getPermanentResources();
                switch (((StartingCard) this.card).getPermanentResources().length) {
                    case 1 -> {System.out.print("|     " + PermanentResources[0].toString() + AnsiColors.ANSI_WHITE + "     |"+ AnsiColors.ANSI_RESET);}
                    case 2 -> {System.out.print("|    " + PermanentResources[0].toString() + AnsiColors.ANSI_WHITE + " " + PermanentResources[1].toString() + AnsiColors.ANSI_WHITE + "    |" + AnsiColors.ANSI_RESET);}
                    case 3 -> {System.out.print("|    " + PermanentResources[0].toString() + AnsiColors.ANSI_WHITE + PermanentResources[1].toString() + AnsiColors.ANSI_WHITE + PermanentResources[2].toString() + AnsiColors.ANSI_WHITE + "    |" + AnsiColors.ANSI_RESET);}
                }
            } else {
                System.out.print("|           |");
            }
        } else if (this.card instanceof GoldCard) {
            System.out.print(AnsiColors.ANSI_YELLOW);
            if (!this.card.getIsFront()) {
                System.out.print("|     "+this.card.getKingdom().toString()+AnsiColors.ANSI_YELLOW+"     |" + AnsiColors.ANSI_RESET);
            } else {
                System.out.print("|           |" + AnsiColors.ANSI_RESET);
            }
        } else {
            System.out.print(AnsiColors.ANSI_RESET);
            if (!this.card.getIsFront()) {
                System.out.print("|     "+this.card.getKingdom().toString()+AnsiColors.ANSI_RESET+"     |" + AnsiColors.ANSI_RESET);
            } else {
                System.out.print("|           |" + AnsiColors.ANSI_RESET);
            }
        }
    }

    public void printFifthCardLine() {
        if (this.card instanceof StartingCard) {
            System.out.print(AnsiColors.ANSI_WHITE);
            System.out.print("|"+this.card.getCorner(2).toString()+ AnsiColors.ANSI_WHITE + "         " + this.card.getCorner(3).toString()+ AnsiColors.ANSI_WHITE+"|" + AnsiColors.ANSI_RESET);
        } else if (this.card instanceof GoldCard) {
            System.out.print(AnsiColors.ANSI_YELLOW);
            if (!this.card.getIsFront()) {
                System.out.print("|"+Resource.BLANK.toString()+AnsiColors.ANSI_YELLOW + "         " + Resource.BLANK.toString()+AnsiColors.ANSI_YELLOW+"|" + AnsiColors.ANSI_RESET);
            } else {
                System.out.print("|"+this.card.getCorner(2).toString()+AnsiColors.ANSI_YELLOW + "         " + this.card.getCorner(3).toString()+AnsiColors.ANSI_YELLOW+"|" + AnsiColors.ANSI_RESET);
            }
        } else {
            System.out.print(AnsiColors.ANSI_RESET);
            if (!this.card.getIsFront()) {
                System.out.print("|"+Resource.BLANK.toString()+AnsiColors.ANSI_RESET + "         " + Resource.BLANK.toString()+AnsiColors.ANSI_RESET+"|");
            } else {
                System.out.print("|"+this.card.getCorner(2).toString()+AnsiColors.ANSI_RESET + "         " + this.card.getCorner(3).toString()+AnsiColors.ANSI_RESET+"|");
            }
        }
    }

    public void printSixthCardLine() {
        if (this.card instanceof StartingCard) {
            System.out.print(AnsiColors.ANSI_WHITE);
            System.out.print("|___________|"+AnsiColors.ANSI_RESET);
        } else if (this.card instanceof GoldCard) {
            System.out.print(AnsiColors.ANSI_YELLOW);
            if (!this.card.getIsFront()) {
                System.out.print("|___________|"+ AnsiColors.ANSI_RESET);
            } else {
                ArrayList<Resource> PlayableCondition = ((GoldCard) this.card).getPlayableCondition();
                switch (PlayableCondition.size()) {
                    case 3 -> {System.out.print("|____"+PlayableCondition.get(0).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(1).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(2).toString()+AnsiColors.ANSI_YELLOW+"____|");}
                    case 4 -> {System.out.print("|___"+PlayableCondition.get(0).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(1).toString()+AnsiColors.ANSI_YELLOW+"_"+PlayableCondition.get(2).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(3).toString()+AnsiColors.ANSI_YELLOW+"___|");}
                    case 5 -> {System.out.print("|___"+PlayableCondition.get(0).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(1).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(2).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(3).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(4).toString()+AnsiColors.ANSI_YELLOW+"___|");}
                }
                System.out.print(AnsiColors.ANSI_RESET);
            }
        } else {
            System.out.print(AnsiColors.ANSI_RESET);
            System.out.print("|___________|"+AnsiColors.ANSI_RESET);
        }
    }

    public void printThreeCards (GameCard one, GameCard two, GameCard three) {
        System.out.println("   Card N°1        Card N°2        Card N°3   ");
        this.SetCard(one);
        printFirstCardLine();
        System.out.print("   ");
        this.SetCard(two);
        printFirstCardLine();
        System.out.print("   ");
        this.SetCard(three);
        printFirstCardLine();
        System.out.println();

        this.SetCard(one);
        printSecondCardLine();
        System.out.print("   ");
        this.SetCard(two);
        printSecondCardLine();
        System.out.print("   ");
        this.SetCard(three);
        printSecondCardLine();
        System.out.println();

        this.SetCard(one);
        printThirdCardLine();
        System.out.print("   ");
        this.SetCard(two);
        printThirdCardLine();
        System.out.print("   ");
        this.SetCard(three);
        printThirdCardLine();
        System.out.println();

        this.SetCard(one);
        printFourthCardLine();
        System.out.print("   ");
        this.SetCard(two);
        printFourthCardLine();
        System.out.print("   ");
        this.SetCard(three);
        printFourthCardLine();
        System.out.println();

        this.SetCard(one);
        printFifthCardLine();
        System.out.print("   ");
        this.SetCard(two);
        printFifthCardLine();
        System.out.print("   ");
        this.SetCard(three);
        printFifthCardLine();
        System.out.println();

        this.SetCard(one);
        printSixthCardLine();
        System.out.print("   ");
        this.SetCard(two);
        printSixthCardLine();
        System.out.print("   ");
        this.SetCard(three);
        printSixthCardLine();
        System.out.println();
    }
}
