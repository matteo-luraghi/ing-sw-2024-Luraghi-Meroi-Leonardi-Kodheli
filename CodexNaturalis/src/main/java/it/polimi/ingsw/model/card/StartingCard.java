package it.polimi.ingsw.model.card;

/**
 * StartingCard Class
 * @author Matteo Leonardo Luraghi
 */
public class StartingCard extends GameCard{
    private final Resource[] backCorners;
    private final Resource[] permanentResources;

    /**
     * Constructor
     * @param kingdom type of the card
     * @param isFront which side of the card is currently visible
     * @param frontCorners corners visible on the front of the card
     * @param backCorners corners visible on the back of the card
     * @param permanentResources resources on the back of the card that can't be covered
     */
    public StartingCard(Kingdom kingdom, boolean isFront,
                        Resource[] frontCorners, Resource[] backCorners, Resource[] permanentResources) {
        super(kingdom, isFront, frontCorners);
        this.backCorners = backCorners.clone();
        this.permanentResources = permanentResources.clone();
    }

    /**
     * Corner getter
     * @param corner index of the selected corner
     * @return Resource present on the selected corner
     */
    @Override
    public Resource getCorner(int corner) {
        if(this.getIsFront()) {
            super.getCorner(corner);
        }
        return this.backCorners[corner];
    }

    /**
     * PermanentResource getter
     * @return array of the permanent resources
     */
    public Resource[] getPermanentResources() {
        return this.permanentResources.clone();
    }
}
