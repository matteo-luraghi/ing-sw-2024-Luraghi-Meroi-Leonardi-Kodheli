package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.gamelogic.Deck;

/**
 * DeckObserver class
 * @author Lorenzo Meroi
 */
public class DeckObserver {

    private ViewDeckFactory deckViewer;

    /**
     * DeckObserver constructor
     * @param deckViewer is the viewer you want to update on any change
     */
    public void DeckObserver (ViewDeckFactory deckViewer) {
        this.deckViewer = deckViewer;
    }

    /**
     * method to update the view on a change
     * @param deck is the deck that has changed
     */
    public void Update (Deck deck) {
        this.deckViewer.setDeck(deck);
        this.deckViewer.show();
    }
}
