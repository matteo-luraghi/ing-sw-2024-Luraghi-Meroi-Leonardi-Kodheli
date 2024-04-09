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
        for (Player player : game.getPlayers()) {
            int points = game.getGameTable().countGoalPoints(game.getGameTable().getPlayerZones().get(player));
            game.getGameTable().getScoreBoard().addPoints(player, points);
        }
        this.transition(new FinalState(this.game));
    }
}
