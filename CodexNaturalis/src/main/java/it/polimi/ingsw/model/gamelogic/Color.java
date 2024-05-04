package it.polimi.ingsw.model.gamelogic;

import java.io.Serial;
import java.io.Serializable;

/**
 * Color enumeration
 * @author Lorenzo Meroi
 */
public enum Color implements Serializable {
    RED,
    BLUE,
    YELLOW,
    GREEN;

    @Serial
    private static final long serialVersionUID = -3997072925040508193L;
}
