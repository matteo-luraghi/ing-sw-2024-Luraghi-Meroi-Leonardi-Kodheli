package it.polimi.ingsw.model.card;

import it.polimi.ingsw.view.mainview.AnsiColors;

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
    COVERED,
    UNKNOWN;

    public String toString() {
        switch (this) {
            case ANIMAL -> {return AnsiColors.ANSI_CYAN+"A";}
            case BLANK -> {return AnsiColors.ANSI_WHITE+"B";}
            case FUNGI -> {return AnsiColors.ANSI_RED+"F";}
            case PLANT -> {return AnsiColors.ANSI_GREEN+"P";}
            case HIDDEN -> {return AnsiColors.ANSI_BLACK+"H";}
            case INSECT -> {return AnsiColors.ANSI_PURPLE+"I";}
            case POTION -> {return AnsiColors.ANSI_YELLOW+"T";}
            case SCROLL -> {return AnsiColors.ANSI_YELLOW+"S";}
            case COVERED -> {return AnsiColors.ANSI_WHITE+"C";}
            case FEATHER -> {return AnsiColors.ANSI_YELLOW+"R";}
            default -> {return "UNKNOWN";}
        }
    }
}
