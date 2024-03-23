package it.polimi.ingsw.model.gamelogic;

import java.util.ArrayList;
import java.util.Map;

/**
 * class ScoreBoard
 * @author Lorenzo Meroi
 */
public class ScoreBoard {
    private Map<Player, Integer> Board;

    /**
     * ScoreBoard constructor
     * @param Players is ArrayList of all the Player in playing a game, Players.length>=2 && Players.length<=4
     */
    public ScoreBoard (ArrayList<Player> Players) {
        for (Player Player : Players) {
            Board.put(Player, 0);
        }
    }

    /**
     * Points getter given the player
     * @param player of which you want to know the points
     * @return the points of the given Player
     */
    public Integer getPoints(Player player) {
        return Board.get(player);
    }

    /**
     * Points adder
     * @param player to which you want to add points to
     * @param points how many points you want to add
     */
    public void addPoints (Player player, Integer points) {
        Board.put(player, Board.get(player)+points);
    }
}
