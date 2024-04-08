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

    /**
     * private goal card chooser
     * @param firstGoal first option
     * @param secondGoal second option
     * @return the chosen card
     */
    public GoalCard choosePrivateGoal(GoalCard firstGoal, GoalCard secondGoal) {
        // TODO: call to the controller or something to make the player choose the goal
        return firstGoal;
    }
}
