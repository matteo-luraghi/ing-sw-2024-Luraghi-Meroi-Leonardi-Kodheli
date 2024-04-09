package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.GoalCard;

/**
 * class Player
 * @author Lorenzo Meroi
 */
public class Player {
    private String nickname;
    private Color color;

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

}
