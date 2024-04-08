package it.polimi.ingsw.model.gamelogic;

/**
 * DrawState class
 * State in which the player needs to draw a card
 * @author Lorenzo Meroi
 */
public class DrawState extends TurnState{
    /**
     * DrawState constructor
     * @param game refers to the GameFlowState object of a determined game
     */
    public DrawState(GameFlowState gameFlowState) {
        super(gameFlowState);
    }

    /**
     * method to handle the drawing phase of the turn
     */
    @Override
    public void HandleTurnState() {
        //make the Player choose where to draw from

        this.Transition(new PlayState(this.gameFlowState));
    }
}