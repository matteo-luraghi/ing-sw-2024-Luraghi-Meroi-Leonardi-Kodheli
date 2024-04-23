package it.polimi.ingsw.model.states;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;

/**
 * GameFlowState class
 * it manages the normal flow of the game
 * The players' turns are implemented through a state pattern
 * @author Lorenzo Meroi
 */
public class GameFlowState extends State{

    private Player turn;

    private TurnState turnState;

    private boolean penultimateRound;

    private boolean finalTurn;

    /**
     * constructor of the GameFlow class
     * @param game is the game state to which the state refers
     */
    public GameFlowState(GameState game) {
        super(game);
        turn = game.getPlayers().get(0);
        turnState = new PlayState(this);
        finalTurn = false;
        penultimateRound = false;
    }


    /**
     * method to handle the game flow
     */
    @Override
    public void HandleState() {
        while(!this.CheckEndState()) {
            if (penultimateRound && turn.equals(game.getPlayers().getFirst()))
                this.finalTurn = true;

            //now the turnState should be a PlayState
            turnState.HandleTurnState();

            if (game.getGameTable().getScoreBoard().getPoints(turn) >= 20 || (game.getGameTable().getGoldDeck().isDeckEmpty() && game.getGameTable().getResourceDeck().isDeckEmpty()))
                this.penultimateRound = true;

            if (!(game.getGameTable().getGoldDeck().isDeckEmpty() && game.getGameTable().getResourceDeck().isDeckEmpty()))
                //now the turnState should be a DrawState
                turnState.HandleTurnState();

            ChangeTurn();
        }
        this.transition(new CountGoalsState(this.game));
    }

    /**
     * turn getter
     * @return the player which is now playing
     */
    public Player getTurn() {
        return this.turn;
    }

    /**
     * turn state getter
     * @return the turn state in which the player is playing
     */
    public TurnState getTurnState() {
        return turnState;
    }

    /**
     * method to handle a specific turn state
     */
    public void HandleTurnState() {
        turnState.HandleTurnState();
    }

    public void SetTurnState (TurnState newState) {
        this.turnState = newState;
    }

    /**
     * method to change the player who is playing
     */
    private void ChangeTurn () {
        turn = (game.getPlayers().indexOf(turn) != 3) ? game.getPlayers().get(game.getPlayers().indexOf(turn)+1) : game.getPlayers().get(0);
    }

    /**
     * method to check whether to transition to the next state or not
     * @return true if it can, false if it's not yet the case
     */
    private boolean CheckEndState() {
        return (turn.equals(game.getPlayers().get(0)) && penultimateRound && finalTurn);
    }
}
