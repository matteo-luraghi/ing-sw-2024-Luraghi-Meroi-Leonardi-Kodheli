package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.gamelogic.Deck;
import it.polimi.ingsw.model.gamelogic.GameTable;
import it.polimi.ingsw.view.cli.ViewGameCardCLIFactory;
import it.polimi.ingsw.view.cli.ViewGoalCardCLIFactory;

import java.io.Serial;
import java.io.Serializable;

/**
 * viewDeckFactory abstract class
 * @author Lorenzo Meroi
 */
public abstract class ViewDeckFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = -3109862720277657303L;
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
     * method to show the game's common goals next to one another
     */
    public abstract void showCommonGoals();
}
