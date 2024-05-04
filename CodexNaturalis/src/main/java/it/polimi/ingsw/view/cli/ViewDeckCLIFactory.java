package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.GameCard;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.view.mainview.AnsiColors;
import it.polimi.ingsw.view.mainview.ViewDeckFactory;

/**
 * ViewDeckCLIFactory class
 * @author Lorenzo Meroi
 */
public class ViewDeckCLIFactory extends ViewDeckFactory {

    public ViewDeckCLIFactory() {
        this.goalCardViewer = new ViewGoalCardCLIFactory();
        this.gameCardViewer = new ViewGameCardCLIFactory();
    }

    /**
     * method to show a deck and it's card
     */
    @Override
    public void show() {

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
            GameCard card = this.deck.getUncoveredCards()[0];
            card.flip();
            gameCardViewer.SetCard(card);
            gameCardViewer.Show();
            card.flip();
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
            GameCard card = this.deck.getUncoveredCards()[1];
            card.flip();
            gameCardViewer.SetCard(card);
            gameCardViewer.Show();
            card.flip();
        }

    }

    /**
     * method to display the game's common goals
     */
    @Override
    public void showCommonGoals() {
        System.out.println();
        System.out.println("Common Goals:");
        this.goalCardViewer.SetCard(this.commonGoals[0]);
        this.goalCardViewer.Show();
        this.goalCardViewer.SetCard(this.commonGoals[1]);
        this.goalCardViewer.Show();
        System.out.println();
    }
}
