package it.polimi.ingsw.model.card;

import java.io.Serial;
import java.io.Serializable;

/**
 * PointCondition Enumeration
 * @author Gabriel Leonardi
 */
public enum PointCondition implements Serializable {
    NORMAL,
    CORNER,
    FEATHER,
    POTION,
    SCROLL,
    UNKNOWN;

    @Serial
    private static final long serialVersionUID = 2155997802452745419L;

    /**
     * to string method for string format of the ENUM
     * @return string format of the ENUM otherwise "UNKNOWN"
     */
    public String toString () {
        switch (this) {
            case SCROLL -> {return "S";}
            case POTION -> {return "T";}
            case CORNER -> {return "C";}
            case FEATHER -> {return "R";}
            case NORMAL -> {return "N";}
            default -> {return "UNKNOWN";}
        }
    }

    /**
     * extened to string version
     * @return extended text string
     */
    public String toStringExt() {
        switch (this) {
            case SCROLL -> {return "For every scroll resource in your field (S)";}
            case POTION -> {return "For every potion resource in your field (T)";}
            case CORNER -> {return "For every corner covered when playing this card (C)";}
            case FEATHER -> {return "For every feather resource in your field (R)";}
            case NORMAL -> {return "You gain the points when playing the card (N)";}
            default -> {return "UNKNOWN";}
        }
    }
}
