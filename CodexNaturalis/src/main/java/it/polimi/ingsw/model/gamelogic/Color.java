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

}
