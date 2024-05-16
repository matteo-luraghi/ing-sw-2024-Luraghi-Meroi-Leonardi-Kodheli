package it.polimi.ingsw.model.card;

import java.io.Serial;
import java.io.Serializable;

/**
 * GameCard Class
 * @author Matteo Leonardo Luraghi
 */
abstract public class GameCard implements Serializable {
    @Serial
    private static final long serialVersionUID = -1051010533191723823L;
    private final Resource[] corners; //0 is Top LEFT, 1 is TOP RIGHT, 2 is BOTTOM LEFT, 3 is BOTTOM RIGHT
    private final Kingdom kingdom;
    private boolean isFront;

    private int id;

    /**
     * Constructor
     * @param kingdom type of the card
     * @param isFront which side of the card is currently visible
     * @param corners content of the front corners of the card
     */
    public GameCard(Kingdom kingdom, boolean isFront, Resource[] corners, int id) {
        this.kingdom = kingdom;
        this.isFront = isFront;
        this.corners = corners.clone();
        this.id=id;
    }

    /**
     * Card id getter for graphical resources
     * @return int id of the card
     */
    public int getId() {
        return id;
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
