package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.ResourceCard;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;

/**
 * Deck class
 * @author Lorenzo Meroi
 */
public class Deck {
    private Queue<ResourceCard> cards;

    private ResourceCard[] uncoveredCards;

    /**
     * Deck constructor
     * @param cards queue of the cards to be added
     * @param uncoveredCards array of the uncovered card on the table
     */
    public Deck (Queue<ResourceCard> cards, ResourceCard[] uncoveredCards) {
        this.cards = new LinkedList<>();
        this.cards.addAll(cards);
        this.uncoveredCards = uncoveredCards.clone();
    }

    /**
     * Draw function (only called internally from the Deck class)
     * @param which can be 0 (from deck), 1(first uncovered card), 2(second uncovered card)
     * @throws NullPointerException when trying to draw from an uncovered card that does not exist
     * @return ResourceCard chosen
     */
    private ResourceCard Draw (int which) {
        if (which == 0) {//drawing from deck
            return cards.remove();
        } else { //drawing from uncovered cards
            try {
                ResourceCard drawn = uncoveredCards[which-1];
                setUncoveredCard(which);
                return drawn;
            } catch (NullPointerException e) {  //TODO: Custom exception?
                System.err.println("No such card in here");
            }
        }
        return null;
    }

    /**
     * Draw from the deck function accessible from the outside
     * @return ResourceCard chosen
     */
    public ResourceCard DrawFromDeck(){
        if(cards.isEmpty()){
            System.err.println("Deck empty, cannot Draw");
            return null;
        }
        return Draw(0);
    }

    /**
     * Draw from the deck function accessible from the outside
     * @param which (should only be 0 or 1) indicating the index of the UncoveredResourceCard to draw
     * @return ResourceCard chosen
     */
    public ResourceCard DrawFromUncovered(int which){
        if(which != 0 && which != 1){
            System.err.println("param which is outOfBounds");
            return null;
        }
        return Draw(which + 1);
    }

    /**
     * UncoveredCards getter
     * @return ResourceCard[]
     */
    public ResourceCard[] getUncoveredCards (){
        return this.uncoveredCards.clone();
    }

    /**
     * UncoveredCards setter, it is taken from the deck
     * @param Which can be 1(first uncovered card) or 2(second uncovered card)
     */
    public void setUncoveredCard (int Which) {
        this.uncoveredCards[Which-1] = this.Draw(0);
    }
}
