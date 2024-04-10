package it.polimi.ingsw.model.states;

/**
 * PlayState class
 * State in which the player needs to play a card
 * @author Lorenzo Meroi
 */
public class PlayState extends TurnState{

    /**
     * PlayState constructor
     * @param gameFlowState refers to the GameFlowState object of a determined game
     */
    public PlayState(GameFlowState gameFlowState) {
        super(gameFlowState);
    }

    /**
     * method to handle the playing phase of the turn
     */
    @Override
    public void HandleTurnState() {
        //make the player choose a card
        //make the player choose where to play the card
        //this.gameFlowState.game.getGameTable().getPlayerZones().get(gameFlowState.game.getTurn()).Play(where, card);

        if (!(gameFlowState.game.getGameTable().getResourceDeck().isDeckEmpty() && gameFlowState.game.getGameTable().getGoldDeck().isDeckEmpty()))
            this.Transition(new DrawState(this.gameFlowState));
    }
}
