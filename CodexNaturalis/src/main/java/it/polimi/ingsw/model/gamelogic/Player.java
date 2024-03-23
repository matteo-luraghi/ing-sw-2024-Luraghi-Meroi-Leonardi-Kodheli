package it.polimi.ingsw.model.gamelogic;

/**
 * class Player
 * @author Lorenzo Meroi
 */
public class Player {
    private String Nickname;
    private Color Color;

    /**
     * Player constructor
     * @param nickname name to identify a Player
     * @param color to identify a Player
     */
    public Player (String nickname, Color color) {
        this.Nickname = nickname;
        this.Color = color;
    }

    /**
     * Nickname getter
     * @return String
     */
    public String getNickname() {
        return Nickname;
    }

    /**
     * nickname setter
     * @param nickname to give the player
     */
    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    /**
     * color getter
     * @return Color
     */
    public Color getColor() {
        return Color;
    }

    /**
     * color setter
     * @param color to give the player
     */
    public void setColor(Color color) {
        Color = color;
    }
}
