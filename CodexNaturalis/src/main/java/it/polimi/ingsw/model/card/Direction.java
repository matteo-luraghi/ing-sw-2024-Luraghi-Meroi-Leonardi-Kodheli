package it.polimi.ingsw.model.card;

import java.io.Serial;
import java.io.Serializable;

/**
 * Direction Enumeration
 * @author Gabriel Leonardi
 */
public enum Direction implements Serializable {
    TOP_RIGHT,
    TOP_LEFT,
    TOP,
    UNKNOWN;
    @Serial
    private static final long serialVersionUID = -4169809243861604349L;
}
