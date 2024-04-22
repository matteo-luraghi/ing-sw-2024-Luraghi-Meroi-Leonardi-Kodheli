package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.view.mainview.ViewDeckFactory;

public class ViewDeckCLIFactory extends ViewDeckFactory {


    @Override
    public void show() {
        if (this.deck.isDeckEmpty()) {
            System.out.println("           _____      _____ ");
            System.out.println(" deck     |  no |    |  no |");
            System.out.println("|-----|   |1card|    |2card|");
            System.out.println("|empty|   |_____|    |_____|");
        } else {
            if (this.deck.isGold())
                System.out.print("Gold Deck       ");
            else
                System.out.print("Resource Deck   ");

            if (this.deck.getUncoveredCards()[0].getIsGold())
                System.out.print("Gold       ");
            else
                System.out.print("Resource   ");

            if (this.deck.getUncoveredCards()[1].getIsGold())
                System.out.print("Gold");
            else
                System.out.print("Resource");
            System.out.println("");

            System.out.println("|--"+this.deck.getCards().size()+"--|    -----     -----");
        }

    }
}
