package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.view.mainview.AnsiColors;
import it.polimi.ingsw.view.mainview.ViewDeckFactory;

/**
 * ViewDeckCLIFactory class
 * @author Lorenzo Meroi
 */
public class ViewDeckCLIFactory extends ViewDeckFactory {

    /**
     * method to show a deck and it's card
     */
    @Override
    public void show() {
        System.out.println();
        System.out.println("Common Goals:");
        this.goalCardViewer.SetCard(this.commonGoals[0]);
        this.goalCardViewer.Show();
        this.goalCardViewer.SetCard(this.commonGoals[1]);
        this.goalCardViewer.Show();
        System.out.println();

        if (this.deck.getCards().isEmpty()) {
            if (this.deck.getCards().element() instanceof GoldCard) {
                System.out.print(AnsiColors.ANSI_YELLOW);
                System.out.println("Gold deck");
                System.out.println("|-----------|");
                System.out.println("|___empty___|");
                System.out.print(AnsiColors.ANSI_RESET);
            } else {
                System.out.println("Resource Deck");
                System.out.println("|-----------|");
                System.out.println("|___empty___|");
            }



        } else {
            ResourceCard card;
            if (this.deck.getCards().element() instanceof GoldCard) {
                System.out.print(AnsiColors.ANSI_YELLOW);
                System.out.println("Gold deck");
                card = (GoldCard) this.deck.getCards().element();
                System.out.print(AnsiColors.ANSI_RESET);
            } else {
                System.out.println("Resource Deck");
                card = this.deck.getCards().element();
            }

            gameCardViewer.SetCard(card);
            gameCardViewer.Show();
            System.out.println();
        }

        System.out.println("Uncovered Card N°1");
        if (this.deck.getUncoveredCards()[0] == null) {
            System.out.println(" ___________");
            System.out.println("|           |");
            System.out.println("|     NO    |");
            System.out.println("|    CARD   |");
            System.out.println("|___________|");
        } else {
            gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
            gameCardViewer.Show();
        }

        System.out.println();

        System.out.println("Uncovered Card N°2");
        if (this.deck.getUncoveredCards()[1] == null) {
            System.out.println(" ___________");
            System.out.println("|           |");
            System.out.println("|     NO    |");
            System.out.println("|    CARD   |");
            System.out.println("|___________|");
        } else {
            gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
            gameCardViewer.Show();
        }

    }
}
