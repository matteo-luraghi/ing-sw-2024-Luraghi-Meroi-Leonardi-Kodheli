package it.polimi.ingsw.model.card;

/**
 * GameCard Class
 * @author Matteo Leonardo Luraghi
 */
abstract public class GameCard {
    private Resource[] corners;
    private final Kingdom kingdom;
    private boolean isFront;

    /**
     * Constructor
     * @param kingdom type of the card
     * @param isFront which side of the card is currently visible
     * @param corners content of the front corners of the card
     */
    public GameCard(Kingdom kingdom, boolean isFront, Resource[] corners) {
        this.kingdom = kingdom;
        this.isFront = isFront;
        this.corners = corners.clone();
    }

    /**
     * Corner getter
     * @param corner index of the selected corner
     * @return Resource present on the selected corner
     */
    public Resource getCorner(int corner) {
        return this.corners[corner];
    }

    /**
     * CoverCorner
     * @param corner index of the corner to be covered
     */
    public void coverCorner(int corner) {
        this.corners[corner] = Resource.COVERED;
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
