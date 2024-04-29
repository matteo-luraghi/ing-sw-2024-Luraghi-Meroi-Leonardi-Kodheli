package it.polimi.ingsw.model.card;

/**
 * PointCondition Enumeration
 * @author Gabriel Leonardi
 */
public enum PointCondition {
    NORMAL,
    CORNER,
    FEATHER,
    POTION,
    SCROLL,
    UNKNOWN;

    public String toString () {
        switch (this) {
            case SCROLL -> {return "S";}
            case POTION -> {return "T";}
            case CORNER -> {return "C";}
            case FEATHER -> {return "R";}
            default -> {return "UNKNOWN";}
        }
    }
}
