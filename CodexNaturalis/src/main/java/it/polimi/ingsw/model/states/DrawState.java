package it.polimi.ingsw.model.states;

/**
 * DrawState class
 * State in which the player needs to draw a card
 * @author Lorenzo Meroi
 */
public class DrawState extends TurnState{
    /**
     * DrawState constructor
     * @param gameFlowState refers to the GameFlowState object of a determined game
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
        view.chooseCardToDraw();

        this.Transition(new PlayState(this.gameFlowState));
    }
}
