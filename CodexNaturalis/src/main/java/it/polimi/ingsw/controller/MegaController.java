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
        //this.controllerState = controllerState; TODO: new LoginPlayers();
    }
    /**
     * ControllerState setter, making it possible to transition between states
     * @param newState the controllerState i want to switch to
     */
    public void setState(ControllerState newState){ this.controllerState = newState; }

    /**
     * ?
     */
    public void handleController(){
        //TODO: I have no idea what to put inside of here
    }
}
