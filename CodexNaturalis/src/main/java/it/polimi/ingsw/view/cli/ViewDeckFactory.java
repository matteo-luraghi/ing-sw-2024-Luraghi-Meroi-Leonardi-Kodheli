package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.gamelogic.Deck;
import it.polimi.ingsw.view.mainview.AnsiColors;

import java.io.Serial;
import java.io.Serializable;

/**
 * ViewDeckFactory class
 * @author Lorenzo Meroi
 */
public class ViewDeckFactory implements Serializable {
    @Serial
    private static final long serialVersionUID = -3109862720277657303L;
    protected Deck deck;
    protected ViewGameCardFactory gameCardViewer;
    protected ViewGoalCardFactory goalCardViewer;
    protected GoalCard[] commonGoals;

    /**
     * ViewDeckCLIFactory constructor
     */
    public ViewDeckFactory() {
        this.goalCardViewer = new ViewGoalCardFactory();
        this.gameCardViewer = new ViewGameCardFactory();
    }

    /**
     * method to set which deck to show
     * @param deck is the deck you want to be shown
     */
    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    /**
     * Common goals array setter
     * @param commonGoals the shared goals of the game
     */
    public void setCommonGoals(GoalCard[] commonGoals) {
        this.commonGoals = commonGoals;
    }

    /**
     * method to show a deck and it's card
     */
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
                ((ViewGameCardFactory) this.gameCardViewer).printThreeCards(card, this.deck.getUncoveredCards()[0], this.deck.getUncoveredCards()[1], false);
            } else {

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardFactory) this.gameCardViewer).printFirstCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print("             ");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardFactory) this.gameCardViewer).printFirstCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print("             ");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardFactory) this.gameCardViewer).printFirstCardLine();
                }
                System.out.println();

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardFactory) this.gameCardViewer).printSecondCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print(" ___________ ");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardFactory) this.gameCardViewer).printSecondCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print(" ___________ ");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardFactory) this.gameCardViewer).printSecondCardLine();
                }
                System.out.println();

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardFactory) this.gameCardViewer).printThirdCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print("|           |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardFactory) this.gameCardViewer).printThirdCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print("|           |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardFactory) this.gameCardViewer).printThirdCardLine();
                }
                System.out.println();

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardFactory) this.gameCardViewer).printFourthCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print("|     NO    |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardFactory) this.gameCardViewer).printFourthCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print("|     NO    |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardFactory) this.gameCardViewer).printFourthCardLine();
                }
                System.out.println();

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardFactory) this.gameCardViewer).printFifthCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print("|    CARD   |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardFactory) this.gameCardViewer).printFifthCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print("|    CARD   |");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardFactory) this.gameCardViewer).printFifthCardLine();
                }
                System.out.println();

                this.gameCardViewer.SetCard(this.deck.getCards().element());
                ((ViewGameCardFactory) this.gameCardViewer).printSixthCardLine();
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[0]==null) {
                    System.out.print("|___________|");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[0]);
                    ((ViewGameCardFactory) this.gameCardViewer).printSixthCardLine();
                }
                System.out.print("   ");
                if (this.deck.getUncoveredCards()[1]==null) {
                    System.out.print("|___________|");
                } else {
                    this.gameCardViewer.SetCard(this.deck.getUncoveredCards()[1]);
                    ((ViewGameCardFactory) this.gameCardViewer).printSixthCardLine();
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
    public void showCommonGoals() {
        System.out.println();
        System.out.println("Common Goals:");
        ((ViewGoalCardFactory) this.goalCardViewer).printTwoCards(this.commonGoals[0], this.commonGoals[1]);
        System.out.println();
    }
}
