package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.Resource;
import it.polimi.ingsw.model.card.ResourceCard;

import java.util.NoSuchElementException;
import java.util.Queue;

public class Deck {
    private Queue<ResourceCard> Cards;

    private ResourceCard[] UncoveredCards;

    /**
     * Deck constructor
     * @param cards queue of the cards to be added
     * @param uncoveredCards array of the uncovered card on the table
     */
    public Deck (Queue<ResourceCard> cards, ResourceCard[] uncoveredCards) {
        this.Cards.addAll(cards);
        this.UncoveredCards = uncoveredCards.clone();
    }

    /**
     * Draw function
     * @param which can be 0 (from deck), 1(first uncovered card), 2(second uncovered card)
     * @return ResourceCard chosen
     */
    public ResourceCard Draw (int which) {
        if (which == 0) {//drawing from deck
            try {
                return Cards.remove();
            } catch (NoSuchElementException e) {
                System.out.println("Deck empty, cannot Draw");
            }
        } else { //drawing from uncovered cards
            try {
                ResourceCard drawn = UncoveredCards[which-1];
                setUncoveredCard(which);
                return drawn;
            } catch (NullPointerException e) {
                System.out.println("No such card in here");
            }
        }
        return null;
    }

    /**
     * UncoveredCards getter
     * @return ResourceCard[]
     */
    public ResourceCard[] getUncoveredCards (){
        return this.UncoveredCards.clone();
    }

    /**
     * UncoveredCards setter, it is taken from the deck
     * @param Which can be 1(first uncovered card) or 2(second uncovered card)
     */
    public void setUncoveredCard (int Which) {
        this.UncoveredCards[Which-1] = this.Draw(0);
    }
}
