package it.polimi.ingsw.model.gamelogic;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * class ScoreBoard
 * @author Lorenzo Meroi
 */
public class ScoreBoard implements Serializable {
    @Serial
    private static final long serialVersionUID = -5530929421267416333L;
    private Map<Player, Integer> board;
    private Map<Player, Integer> reachedGoals;

    /**
     * ScoreBoard constructor
     * @param Players is ArrayList of all the Player in playing a game, Players.length gte 2 and Players.length lte 4
     */
    public ScoreBoard (ArrayList<Player> Players) {
        board = new HashMap<>();
        reachedGoals= new HashMap<>();
        for (Player Player : Players) {
            board.put(Player, 0);
            reachedGoals.put(Player,0);
        }
    }

    /**
     * Board getter
     * @return the Game's Board
     */
    public Map<Player, Integer> getBoard() {
        Map<Player, Integer> cloneBoard= (new HashMap<Player, Integer>());
        cloneBoard.putAll(board);
        return cloneBoard;
    }

    /**
     * Points getter given the player
     * @param player of which you want to know the points
     * @return the points of the given Player
     */
    public Integer getPoints(Player player) {
        return board.get(player);
    }

    /**
     * adds the occurrences of a goal card when they are calculated in the gametable function
     * @param player Player referenced
     * @param reachedGoalsCount number of goals to be added
     */
    public void addReachedPointsCount(Player player, Integer reachedGoalsCount)
    {
        if(reachedGoals.containsKey(player))
            reachedGoals.put(player,reachedGoals.get(player)+reachedGoalsCount);
    }

    /**
     * Get the occurrences of goals that the player made
     * @param player referenced player
     * @return Integer value of the occurrences
     */
    public Integer GetReachedPointsCount(Player player)
    {
        return reachedGoals.getOrDefault(player,-1);
    }

    /**
     * Points adder
     * @param player to which you want to add points to
     * @param points how many points you want to add
     * @throws IllegalArgumentException if points less than 0
     */
    public void addPoints (Player player, Integer points) throws IllegalArgumentException {
        if(points<0)
            throw new IllegalArgumentException();
        else
            board.put(player, board.get(player)+points);
    }
}
