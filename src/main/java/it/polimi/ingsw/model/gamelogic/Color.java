package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.view.mainview.AnsiColors;

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

    /**
     * Convert the current color to string
     * @return string format of the color otherwise "UNKNOWN" string
     */
    @Override
    public String toString() {
        switch (this) {
            case YELLOW -> {return AnsiColors.ANSI_YELLOW+"yellow"+AnsiColors.ANSI_RESET;}
            case RED -> {return AnsiColors.ANSI_RED+"red"+AnsiColors.ANSI_RESET;}
            case BLUE -> {return AnsiColors.ANSI_BLUE+"blue"+AnsiColors.ANSI_RESET;}
            case GREEN -> {return AnsiColors.ANSI_GREEN+"green"+AnsiColors.ANSI_RESET;}
            default -> {return "UNKNOWN";}
        }
    }

    /**
     * Implementation for gui, since can't use properly AnsiColors
     * @return String format of the color if present in the enum otherwise "UNKNOWN"
     */
    public String toStringGUI(){
        switch (this) {
            case YELLOW -> { return "yellow";}
            case RED -> {return "red";}
            case BLUE -> {return "blue";}
            case GREEN -> {return "green";}
            default -> {return "UNKNOWN";}
        }
    }

}
