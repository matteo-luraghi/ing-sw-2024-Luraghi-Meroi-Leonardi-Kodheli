package it.polimi.ingsw.model.gamelogic;

/**
 * TurnState abstract class
 * it is implemented through a state pattern
 * @author Lorenzo Meroi
 */
public abstract class TurnState {

    protected GameFlowState gameFlowState;

    /**
     * TurnState constructor
     * @param game refers to the GameFlowState object of a determined game
     */
    public TurnState (GameFlowState game) {
        this.gameFlowState = game;
    }

    /**
     * method to handle the state of the turn
     */
    public abstract void HandleTurnState();

    /**
     * method to change the state of a turn
     * @param turnState refers to which state of a turn to change to
     */
    public void Transition (TurnState turnState) {
        gameFlowState.SetTurnState(turnState);
    }
}
