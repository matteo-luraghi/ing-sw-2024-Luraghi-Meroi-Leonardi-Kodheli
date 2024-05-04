package it.polimi.ingsw.model.card;

import it.polimi.ingsw.view.mainview.AnsiColors;

import java.io.Serial;
import java.io.Serializable;

/**
 * Kingdom Enumeration
 * @author Gabriel Leonardi
 */
public enum Kingdom implements Serializable {
    UNKNOWN,
    PLANT,
    ANIMAL,
    INSECT,
    FUNGI,
    STARTING;
    @Serial
    private static final long serialVersionUID = -4163434024888030176L;
    public String toString () {
        switch (this) {
            case INSECT -> {return AnsiColors.ANSI_PURPLE+"I";}
            case PLANT -> {return AnsiColors.ANSI_GREEN+"P";}
            case FUNGI -> {return AnsiColors.ANSI_RED+"F";}
            case ANIMAL -> {return AnsiColors.ANSI_BLUE+"A";}
            case STARTING -> {return AnsiColors.ANSI_WHITE+"S";}
            default -> {return "UNKNOWN";}
        }
    }

}
