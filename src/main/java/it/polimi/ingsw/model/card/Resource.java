package it.polimi.ingsw.model.card;

import it.polimi.ingsw.view.mainview.AnsiColors;

import java.io.Serial;
import java.io.Serializable;

/**
 * Resource Enumeration
 * @author Matteo Leonardo Luraghi
 */
public enum Resource implements Serializable {
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

    @Serial
    private static final long serialVersionUID = 5524296934682817881L;
    /**
     * to string method for string format of the ENUM
     * @return string format of the ENUM otherwise "UNKNOWN"
     */
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
    /**
     * extened to string version
     * @return extended text string
     */
    public String toStringExt() {
        switch (this) {
            case ANIMAL -> {return AnsiColors.ANSI_CYAN+"Animal (A)";}
            case BLANK -> {return AnsiColors.ANSI_WHITE+"Blank (B)";}
            case FUNGI -> {return AnsiColors.ANSI_RED+"Fungi (F)";}
            case PLANT -> {return AnsiColors.ANSI_GREEN+"Plant (P)";}
            case HIDDEN -> {return AnsiColors.ANSI_BLACK+"Hidden (H)";}
            case INSECT -> {return AnsiColors.ANSI_PURPLE+"Insect (I)";}
            case POTION -> {return AnsiColors.ANSI_YELLOW+"Potion (T)";}
            case SCROLL -> {return AnsiColors.ANSI_YELLOW+"Scroll (S)";}
            case COVERED -> {return AnsiColors.ANSI_WHITE+"Covered (C)";}
            case FEATHER -> {return AnsiColors.ANSI_YELLOW+"Feather (R)";}
            default -> {return "UNKNOWN";}
        }
    }
}
