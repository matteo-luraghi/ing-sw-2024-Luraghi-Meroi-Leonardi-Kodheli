package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.gamelogic.Deck;

/**
 * viewDeckFactory abstract class
 * @author Lorenzo Meroi
 */
public abstract class ViewDeckFactory {

    protected Deck deck;

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
}
