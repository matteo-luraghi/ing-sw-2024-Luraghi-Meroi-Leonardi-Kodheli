package it.polimi.ingsw.model.card;

import java.io.Serial;

/**
 * ResourceCard Class
 * @author Matteo Leonardo Luraghi
 */
public class ResourceCard extends GameCard{
    @Serial
    private static final long serialVersionUID = 5448274245993938851L;
    private final int points;
    private final boolean isGold;
    /**
     * Constructor
     * @param kingdom type of the card
     * @param isFront which side of the card is currently visible
     * @param corners content of the front corners of the card
     * @param points points assigned when the card is played
     * @param isGold true if the card is gold
     * @param id the card's id
     */
    public ResourceCard(Kingdom kingdom, boolean isFront, Resource[] corners, int points, boolean isGold, int id) {
        super(kingdom, isFront, corners, id);
        this.points = points;
        this.isGold = isGold;
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
