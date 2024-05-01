package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.view.mainview.AnsiColors;

/**
 * class Player
 * @author Lorenzo Meroi
 */
public class Player {
    private String nickname;
    private Color color = null;

    /**
     * Player constructor
     * @param nickname name to identify a Player
     * @param color to identify a Player
     */
    public Player (String nickname, Color color) {
        this.nickname = nickname;
        this.color = color;
    }

    /**
     * Nickname getter
     * @return String
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * nickname setter
     * @param nickname to give the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * color getter
     * @return Color
     */
    public Color getColor() {
        return color;
    }

    /**
     * color setter
     * @param color to give the player
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * toString method for the player
     * @return the coloured String of the player's name
     */
    public String toString () {
        switch (this.color) {
            case RED -> {return AnsiColors.ANSI_RED+this.nickname+AnsiColors.ANSI_RESET;}
            case BLUE -> {return AnsiColors.ANSI_BLUE+this.nickname+AnsiColors.ANSI_RESET;}
            case GREEN -> {return AnsiColors.ANSI_GREEN+this.nickname+AnsiColors.ANSI_RESET;}
            case YELLOW -> {return AnsiColors.ANSI_YELLOW+this.nickname+AnsiColors.ANSI_RESET;}
            default -> {return this.nickname;}
        }
    }

}
