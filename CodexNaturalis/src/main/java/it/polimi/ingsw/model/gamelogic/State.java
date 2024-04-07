package it.polimi.ingsw.model.gamelogic;

import java.util.ArrayList;

/**
 * States of the game abstract class
 * It is implemented through a state pattern
 * @author Lorenzo Meroi
 */
public abstract class State {
    protected GameState game;


    /**
     * State class constructor
     * @param game is the gamestate to which the states refer
     */
    public State (GameState game) {
        this.game = game;
    }

    /**
     * Game getter
     * @return the GameState to which the states refer
     */
    public GameState getGame() {
        return game;
    }

    /**
     * method to handle a determined state of the game
     */
    abstract public void HandleState();

    /**
     * method to switch to the next state of the game
     * @param state to make the transition to
     */
    public void transition(State state) {
        game.setState(state);
    }
}
