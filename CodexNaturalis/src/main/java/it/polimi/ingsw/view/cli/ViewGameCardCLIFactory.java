package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.Resource;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
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

            Resource[] PermanentResources= ((StartingCard) this.card).getPermanentResources();
            switch (((StartingCard) this.card).getPermanentResources().length) {
                case 1 -> {System.out.println("|     "+PermanentResources[0].toString()+ AnsiColors.ANSI_WHITE+"     |");}
                case 2 -> {System.out.println("|    "+PermanentResources[0].toString()+ AnsiColors.ANSI_WHITE+" "+PermanentResources[1].toString()+ AnsiColors.ANSI_WHITE+"    |");}
                case 3 -> {System.out.println("|    "+PermanentResources[0].toString()+ AnsiColors.ANSI_WHITE + PermanentResources[1].toString()+ AnsiColors.ANSI_WHITE +PermanentResources[2].toString()+ AnsiColors.ANSI_WHITE+"    |");}
            }

            System.out.println("|"+this.card.getCorner(2).toString()+ AnsiColors.ANSI_WHITE + "         " + this.card.getCorner(3).toString()+ AnsiColors.ANSI_WHITE+"|");
            System.out.println("|___________|"+AnsiColors.ANSI_RESET);
        }  else if (this.card instanceof GoldCard) {

            if (!this.card.getIsFront()) {
                System.out.print(AnsiColors.ANSI_YELLOW);
                System.out.println(" Gold      "+this.card.getKingdom().toString()+AnsiColors.ANSI_YELLOW);
                System.out.println(" ___________");
                System.out.println("|" + Resource.HIDDEN.toString()+AnsiColors.ANSI_YELLOW + "         " + Resource.HIDDEN.toString()+AnsiColors.ANSI_YELLOW + "|");
                System.out.println("|     "+this.card.getKingdom().toString()+AnsiColors.ANSI_YELLOW+"     |");

                System.out.println("|"+Resource.HIDDEN.toString()+AnsiColors.ANSI_YELLOW + "         " + Resource.HIDDEN.toString()+AnsiColors.ANSI_YELLOW+"|");
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
                    case 4 -> {System.out.println("|___"+PlayableCondition.get(0).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(1).toString()+"_"+AnsiColors.ANSI_YELLOW+"_"+PlayableCondition.get(2).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(3).toString()+AnsiColors.ANSI_YELLOW+"___|");}
                    case 5 -> {System.out.println("|___"+PlayableCondition.get(0).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(1).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(2).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(3).toString()+AnsiColors.ANSI_YELLOW+PlayableCondition.get(4).toString()+AnsiColors.ANSI_YELLOW+"___|");}
                }
                System.out.print(AnsiColors.ANSI_RESET);
            }

        } else if (this.card instanceof ResourceCard) {

            if (!this.card.getIsFront()) {
                System.out.print(AnsiColors.ANSI_RESET);
                System.out.println(" Resource  "+this.card.getKingdom().toString()+AnsiColors.ANSI_RESET);
                System.out.println(" ___________");
                System.out.println("|" + Resource.HIDDEN.toString()+AnsiColors.ANSI_RESET + "         " + Resource.HIDDEN.toString()+AnsiColors.ANSI_RESET + "|");
                System.out.println("|     "+this.card.getKingdom().toString()+"     |");

                System.out.println("|"+Resource.HIDDEN.toString()+AnsiColors.ANSI_RESET + "         " + Resource.HIDDEN.toString()+AnsiColors.ANSI_RESET+"|");
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
}
