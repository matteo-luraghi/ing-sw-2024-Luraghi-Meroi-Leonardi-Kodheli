package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.GameCard;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.view.mainview.AnsiColors;
import it.polimi.ingsw.view.mainview.ViewDeckFactory;
import it.polimi.ingsw.view.mainview.ViewGoalCardFactory;

/**
 * ViewDeckCLIFactory class
 * @author Lorenzo Meroi
 */
public class ViewDeckCLIFactory extends ViewDeckFactory {

    /**
     * ViewDeckCLIFactory constructor
     */
    public ViewDeckCLIFactory() {
        this.goalCardViewer = new ViewGoalCardCLIFactory();
        this.gameCardViewer = new ViewGameCardCLIFactory();
    }

    /**
     * method to show a deck and it's card
     */
    @Override
    public void show() {

        if (!this.deck.getCards().isEmpty()) {
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


            if (this.deck.getUncoveredCards()[0]!=null && this.deck.getUncoveredCards()[1]!=null) {
                ((ViewGameCardCLIFactory) this.gameCardViewer).printThreeCards(card, this.deck.getUncoveredCards()[0], this.deck.getUncoveredCards()[1], false);
            } else {

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardCLIFactory) this.gameCardViewer).printFirstCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print("             ");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printFirstCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print("             ");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printFirstCardLine();
                }
                System.out.println();

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardCLIFactory) this.gameCardViewer).printSecondCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print(" ___________ ");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printSecondCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print(" ___________ ");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printSecondCardLine();
                }
                System.out.println();

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardCLIFactory) this.gameCardViewer).printThirdCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print("|           |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printThirdCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print("|           |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printThirdCardLine();
                }
                System.out.println();

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardCLIFactory) this.gameCardViewer).printFourthCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print("|     NO    |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printFourthCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print("|     NO    |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printFourthCardLine();
                }
                System.out.println();

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardCLIFactory) this.gameCardViewer).printFifthCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print("|    CARD   |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printFifthCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print("|    CARD   |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printFifthCardLine();
                }
                System.out.println();

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardCLIFactory) this.gameCardViewer).printSixthCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print("|___________|");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printSixthCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print("|___________|");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardCLIFactory) this.gameCardViewer).printSixthCardLine();
                }
                System.out.println();

            }
        } else {
            if (this.deck.isGold()) {
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
        }
    }

    /**
     * method to display the game's common goals
     */
    @Override
    public void showCommonGoals() {
        System.out.println();
        System.out.println("Common Goals:");
        ((ViewGoalCardCLIFactory) this.goalCardViewer).printTwoCards(this.commonGoals[0], this.commonGoals[1]);
        System.out.println();
    }
}
