package it.polimi.ingsw.model.card;

/**
 * Kingdom Enumeration
 * @author Gabriel Leonardi
 */
public enum Kingdom {
    PLANT,
    ANIMAL,
    INSECT,
    FUNGI,
    STARTING;

    public String toString () {
        switch (this) {
            case INSECT -> {return "I";}
            case PLANT -> {return "P";}
            case FUNGI -> {return "F";}
            case ANIMAL -> {return "A";}
            default -> {return "";}
        }
    }
}
