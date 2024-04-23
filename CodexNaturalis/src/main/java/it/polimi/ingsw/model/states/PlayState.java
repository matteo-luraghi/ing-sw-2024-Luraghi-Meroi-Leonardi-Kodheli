package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.card.GameCard;
import it.polimi.ingsw.model.card.ResourceCard;

/**
 * PlayState class
 * State in which the player needs to play a card
 * @author Lorenzo Meroi
 */
public class PlayState extends TurnState{

    private ResourceCard currentCard;
    /**
     * PlayState constructor
     * @param gameFlowState refers to the GameFlowState object of a determined game
     */
    public PlayState(GameFlowState gameFlowState) {
        super(gameFlowState);
    }

    /**
     * CurrentCard setter
     * @param card the card that needs to be saved
     */
    public void setCurrentCard(ResourceCard card){ currentCard = card; }
    /**
     * method to handle the playing phase of the turn
     */
    @Override
    public void HandleTurnState() {
        //make the player choose a card and where to play it
        view.chooseCardToPlay();

        //TODO: Check if this transition works
        if (!(gameFlowState.game.getGameTable().getResourceDeck().isDeckEmpty() && gameFlowState.game.getGameTable().getGoldDeck().isDeckEmpty()))
            this.Transition(new DrawState(this.gameFlowState));
    }
}
