package it.polimi.ingsw.model.gamelogic;

/**
 * CountGoalState class
 * it manages the state of the game when the points from the goals need to be calculated
 * @author Lorenzo Meroi
 */
public class CountGoalsState extends State{

    /**
     * CountGoalState class constructor
     * @param game is the gamestate to which the states refer
     */
    public CountGoalsState(GameState game) {
        super(game);
    }

    /**
     * method to handle the state of the game when the points from the goals need to be calculated
     */
    @Override
    public void HandleState() {

    }

    /**
     * method to count the goal points for a player
     * @param player of which you want to calculate the points of
     * @return the points from the goals gained from the player
     */
    private int CountGoalsForPlayer(Player player) {

    }
}
