package it.polimi.ingsw.card;

/**
 * GameCard Class
 * @author Matteo Leonardo Luraghi
 */
abstract public class GameCard {
    private final Kingdom kingdom;
    private boolean isFront;

    /**
     * Constructor
     * @param kingdom type of the card
     * @param isFront which side of the card is currently visible
     */
    public GameCard(Kingdom kingdom, boolean isFront) {
        this.kingdom = kingdom;
        this.isFront = isFront;
    }

    /**
     * Kingdom getter
     * @return kingdom of the card
     */
    public Kingdom getKingdom() {
        return this.kingdom;
    }

    /**
     * IsFront getter
     * @return true if the front side of the card is visible
     */
    public boolean getIsFront() {
        return this.isFront;
    }

    /**
     * Flips the card, changing its visible side
     */
    public void flip() {
        this.isFront = !this.isFront;
    }
}
