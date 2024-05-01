package it.polimi.ingsw.model.gamelogic;

import java.io.Serial;
import java.io.Serializable;

/**
 * Coordinates Class
 * @author Gabriel Leonardi
 */
public class Coordinates implements Serializable {
    @Serial
    private static final long serialVersionUID = 3185563969058087051L;
    final private int x;
    final private int y;

    /**
     * Constructor
     * @param x x coordinate of the card
     * @param y y coordinate of the card
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * X Getter
     * @return the x coordinate of the card
     */
    public int getX() {
        return x;
    }

    /**
     * Y Getter
     * @return the y coordinate of the card
     */
    public int getY() {
        return y;
    }
}
