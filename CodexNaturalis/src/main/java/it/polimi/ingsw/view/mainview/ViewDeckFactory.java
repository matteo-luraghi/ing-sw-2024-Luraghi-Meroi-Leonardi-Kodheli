package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.gamelogic.Deck;
import it.polimi.ingsw.model.gamelogic.GameTable;
import it.polimi.ingsw.view.cli.ViewGameCardCLIFactory;
import it.polimi.ingsw.view.cli.ViewGoalCardCLIFactory;

/**
 * viewDeckFactory abstract class
 * @author Lorenzo Meroi
 */
public abstract class ViewDeckFactory {

    protected Deck deck;
    protected ViewGameCardFactory gameCardViewer;
    protected ViewGoalCardFactory goalCardViewer;
    protected GoalCard[] commonGoals;

    /**
     * abstract method to show a deck
     */
    public abstract void show();

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
     * gameCardViewer setter
     * @param gameCardViewer to show cards
     */
    public void setGameCardViewer(ViewGameCardFactory gameCardViewer) {
        this.gameCardViewer = gameCardViewer;
    }
    /**
     * goalCardViewer setter
     * @param goalCardViewer to show cards
     */
    public void setGameCardViewer(ViewGoalCardFactory goalCardViewer) {
        this.goalCardViewer = goalCardViewer;
    }
}
