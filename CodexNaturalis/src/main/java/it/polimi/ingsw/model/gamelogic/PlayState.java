package it.polimi.ingsw.model.gamelogic;

/**
 * PlayState class
 * State in which the player needs to play a card
 * @author Lorenzo Meroi
 */
public class PlayState extends TurnState{

    /**
     * PlayState constructor
     * @param game refers to the GameFlowState object of a determined game
     */
    public PlayState(GameFlowState game) {
        super(game);
    }

    /**
     * method to handle the playing phase of the turn
     */
    @Override
    public void HandleTurnState() {

    }
}
