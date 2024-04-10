package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.State;

/**
 * MegaController class, manages player input and sends messages to the model
 * @author Gabriel Leonardi
 */
public class MegaController {
    private GameState game;
    private ControllerState controllerState;

    /**
     * MegaController constructor
     * @param game Game that the megaController manages
     */
    public MegaController(GameState game){
        this.game = game;
        this.controllerState = new LoginPlayer();
    }
    /**
     * ControllerState setter, making it possible to transition between states
     * @param newState the controllerState i want to switch to
     */
    public void setState(ControllerState newState){ this.controllerState = newState; }

    /**
     * gameState getter
     * @return the current gameState;
     */
    public GameState getGame() {
        return game;
    }
}
