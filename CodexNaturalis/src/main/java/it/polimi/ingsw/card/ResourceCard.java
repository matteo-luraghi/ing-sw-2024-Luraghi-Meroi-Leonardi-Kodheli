package it.polimi.ingsw.card;

/**
 * ResourceCard Class
 * @author Matteo Leonardo Luraghi
 */
public class ResourceCard extends GameCard{
    private Resource[] corners;
    private final int points;
    private final boolean isGold;
    /**
     * Constructor
     * @param kingdom type of the card
     * @param isFront which side of the card is currently visible
     * @param corners content of the front corners of the card
     * @param points points assigned when the card is played
     * @param isGold true if the card is gold
     */
    public ResourceCard(Kingdom kingdom, boolean isFront, Resource[] corners, int points, boolean isGold) {
        super(kingdom, isFront);
        this.corners = corners.clone();
        this.points = points;
        this.isGold = isGold;
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
     * Points getter
     * @return points assigned when the card is played
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * IsGold getter
     * @return true if the card is gold
     */
    public boolean getIsGold() {
        return this.isGold;
    }
}
