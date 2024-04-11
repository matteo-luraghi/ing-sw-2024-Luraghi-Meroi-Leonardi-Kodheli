package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;

/**
 * FinalState class
 * it manages the state of the game when a winner is chosen
 * @author Lorenzo Meroi
 */
public class FinalState extends State{
    /**
     * FinalState class constructor
     * @param game is the gamestate to which the states refer
     */
    public FinalState(GameState game) {
        super(game);
    }

    /**
     * method to handle the final state of the game
     */
    @Override
    public void HandleState() {
        Player winner = game.getWinner();
    }
}
