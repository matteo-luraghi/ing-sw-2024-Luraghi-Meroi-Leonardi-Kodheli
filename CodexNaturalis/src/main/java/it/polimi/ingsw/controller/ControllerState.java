package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.State;

/**
 * ControllerState class, made to give the controller a state
 * @author Gabriel Leonardi
 */
abstract public class ControllerState {
    protected MegaController controller;

    /**
     * Method made to handle the current action of the state
     */
    abstract public void HandleState();

    /**
     * Method made to switch to the next state of the game
     * @param controllerState To make the transition too
     */
    public void transition(ControllerState controllerState) {
        controller.setState(controllerState);
    }

}
