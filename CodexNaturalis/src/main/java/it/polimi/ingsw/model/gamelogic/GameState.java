package it.polimi.ingsw.model.gamelogic;

import java.util.ArrayList;

/**
 * class GameState
 * @author Françesk Kodheli
 */
public class GameState {
    private State state;
    private ArrayList<Player> players;
    private Player turn;
    private TurnState turnState;
    private GameTable gameTable;
    private Player winner;

    /**
     * GameState Constructor, firstly configured as in SETUP state
     *
     * @param Players        ArrayList of the players for the session
     * @param StartingPlayer The choosen player to start
     * @param GameTable      Main GameTable Object
     */
    public GameState(ArrayList<Player> Players, Player StartingPlayer, GameTable GameTable)
    {
        this.players = new ArrayList<Player>();
        this.players.addAll(Players);
        this.turn = StartingPlayer;
        this.gameTable = GameTable;
        this.winner = null;
        this.state = State.SETUP;
    }

    /**
     * getState returns the current state
     *
     * @return State
     */
    public State getState() {
        return this.state;
    }

    /**
     * Sets the next state SETUP->GAMEFLOW->COUNTGOALS->FINAL (if final won't be changed)
     *
     * @return true if the state changed
     */
    public boolean NextState()
    {
        if (!state.equals(State.FINAL)) {
            State++;
            if(state==State.COUNTGOALS)
            {
                for(PlayerField playerField: gameTable.getPlayerZones().keySet()) {
                    GameTable.countGoalPoints(playerField);
                }
            this.winner=ComputeWinner();
            }
            return true;
        }
        return false;
    }

    /**
     * Players getter
     * @return ArrayList<Player>
     */
    public ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) this.players.clone();
    }

    /**
     * Turn Getter
     * @return Player
     */
    public Player getTurn()
    {
        return this.turn;
    }

    /**
     * Sets the next player in the arraylist as the next turn
     */
    public void nextTurn()
    {
        int i= this.players.indexOf(this.turn);
        i++;
        if(i==this.players.size())
        {
            i=0;
        }
        this.turn=this.players.get(i);
    }

    /**
     * TurnState Getter
     * @return TurnState
     */
    public TurnState getTurnState()
    {
        return this.turnState;
    }

    /**
     * swithces the turnstate between play and draw
     */
    public void ChangeTurnState()
    {
        if(this.turnState.equals(TurnState.PLAY))
            this.turnState=TurnState.DRAW;
        else
            this.turnState=TurnState.PLAY;
    }

    /**
     * GameTable Getter
     * @return GameTable
     */
    public GameTable getGameTable()
    {
        return this.gameTable;
    }

    /**
     * Compute the winner by getting the scoreboard
     * @return
     */
    private Player ComputeWinner()
    {
        int points=0;
        Player CurrWinner = null;
        for(Player player: this.players)
        {
            if(gameTable.getScoreBoard().getPoints(player)>points)
            {
                points=gameTable.getScoreBoard().getPoints(player);
                CurrWinner=player;
            }

        }

        return CurrWinner;
    }

/**
 * Gets the winner, to add exception for when the winner is not set or in the wrong game state
 * @return Player
 */
public Player getWinner()
    {
        if(state.equals(State.FINAL)) {
            return this.winner;
        }
        return null;
    }
}
