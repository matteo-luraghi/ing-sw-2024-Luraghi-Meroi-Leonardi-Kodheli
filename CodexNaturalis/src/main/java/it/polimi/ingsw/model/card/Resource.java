package it.polimi.ingsw.model.card;

/**
 * Resource Enumeration
 * @author Matteo Leonardo Luraghi
 */
public enum Resource {
    PLANT,
    ANIMAL,
    INSECT,
    FUNGI,
    FEATHER,
    POTION,
    SCROLL,
    BLANK,
    HIDDEN,
    COVERED;

    public String toString() {
        switch (this) {
            case ANIMAL -> {return "A";}
            case BLANK -> {return "B";}
            case FUNGI -> {return "F";}
            case PLANT -> {return "P";}
            case HIDDEN -> {return "H";}
            case INSECT -> {return "I";}
            case POTION -> {return "T";}
            case SCROLL -> {return "S";}
            case COVERED -> {return "C";}
            case FEATHER -> {return "R";}
            default -> {return "";}
        }
    }
}
