package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.Resource;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.view.mainview.ViewGameCardFactory;

import java.util.ArrayList;

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
            System.out.println("  Starting  ");
            System.out.println(" ___________");
            System.out.println("|" + this.card.getCorner(0).toString() + "         " + this.card.getCorner(1).toString() + "|");

            Resource[] PermanentResources= ((StartingCard) this.card).getPermanentResources();
            switch (((StartingCard) this.card).getPermanentResources().length) {
                case 1 -> {System.out.println("|     "+PermanentResources[0].toString()+"     |");}
                case 2 -> {System.out.println("|    "+PermanentResources[0].toString()+" "+PermanentResources[1].toString()+"    |");}
                case 3 -> {System.out.println("|    "+PermanentResources[0].toString() + PermanentResources[1].toString() +PermanentResources[2].toString()+"    |");}
            }

            System.out.println("|"+this.card.getCorner(2).toString() + "         " + this.card.getCorner(3).toString()+"|");
            System.out.println("|__________|");
        }  else if (this.card instanceof GoldCard) {

            if (!this.card.getIsFront()) {
                System.out.println(" Gold      "+this.card.getKingdom().toString());
                System.out.println(" ___________");
                System.out.println("|" + Resource.HIDDEN.toString() + "         " + Resource.HIDDEN.toString() + "|");
                System.out.println("|     "+this.card.getKingdom().toString()+"     |");

                System.out.println("|"+Resource.HIDDEN.toString() + "         " + Resource.HIDDEN.toString()+"|");
                System.out.println("|__________|");
            } else {
                System.out.println(" Gold      "+this.card.getKingdom().toString());
                System.out.println(" ____"+((GoldCard) this.card).getPointCondition().toString()+"_"+((GoldCard) this.card).getPoints()+"____");
                System.out.println("|" + this.card.getCorner(0).toString() + "         " + this.card.getCorner(1).toString() + "|");
                System.out.println("|           |");
                System.out.println("|"+this.card.getCorner(2).toString() + "         " + this.card.getCorner(3).toString()+"|");

                ArrayList<Resource> PlayableCondition = ((GoldCard) this.card).getPlayableCondition();
                switch (PlayableCondition.size()) {
                    case 3 -> {System.out.println("|____"+PlayableCondition.get(0).toString()+PlayableCondition.get(1).toString()+PlayableCondition.get(2).toString()+"____|");}
                    case 4 -> {System.out.println("|___"+PlayableCondition.get(0).toString()+PlayableCondition.get(1).toString()+"_"+PlayableCondition.get(2).toString()+PlayableCondition.get(3).toString()+"___|");}
                    case 5 -> {System.out.println("|___"+PlayableCondition.get(0)+PlayableCondition.get(1).toString()+PlayableCondition.get(2).toString()+PlayableCondition.get(3).toString()+PlayableCondition.get(4).toString()+"___|");}
                }
            }

        } else if (this.card instanceof ResourceCard) {

            if (!this.card.getIsFront()) {
                System.out.println(" Resource  "+this.card.getKingdom().toString());
                System.out.println(" ___________");
                System.out.println("|" + Resource.HIDDEN.toString() + "         " + Resource.HIDDEN.toString() + "|");
                System.out.println("|     "+this.card.getKingdom().toString()+"     |");

                System.out.println("|"+Resource.HIDDEN.toString() + "         " + Resource.HIDDEN.toString()+"|");
                System.out.println("|__________|");
            } else {
                System.out.println(" Resource  "+this.card.getKingdom().toString());
                if (((ResourceCard) this.card).getPoints()==0)
                    System.out.println(" ___________");
                else
                    System.out.println(" _____"+((ResourceCard) this.card).getPoints()+"_____");
                System.out.println("|" + this.card.getCorner(0).toString() + "         " + this.card.getCorner(1).toString() + "|");
                System.out.println("|           |");
                System.out.println("|"+this.card.getCorner(2).toString() + "         " + this.card.getCorner(3).toString()+"|");
                System.out.println("|__________|");
            }
        }
    }
}
