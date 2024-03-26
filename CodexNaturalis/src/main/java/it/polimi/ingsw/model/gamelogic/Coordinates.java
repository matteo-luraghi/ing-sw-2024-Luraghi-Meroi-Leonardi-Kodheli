package it.polimi.ingsw.model.gamelogic;

/**
 * Coordinates Class
 * @author Gabriel Leonardi
 */
public class Coordinates {
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
