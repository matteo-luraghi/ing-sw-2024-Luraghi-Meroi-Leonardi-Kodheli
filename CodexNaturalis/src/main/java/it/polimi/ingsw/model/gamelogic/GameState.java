package it.polimi.ingsw.model.gamelogic;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * class GameState
 * @author Françesk Kodheli
 */
public class GameState implements Serializable {
    @Serial
    private static final long serialVersionUID = -1032793492526071375L;
    private State state;
    private TurnState turnState;
    private ArrayList<Player> players;
    private Player turn;
    private GameTable gameTable;
    private Player winner;

    /**
     * GameState Constructor, firstly configured as in SETUP state
     *
     * @param Players        ArrayList of the players for the session
     * @param StartingPlayer The chosen player to start
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
     * getTurnState returns the current turn state
     * @return TurnState
     */
    public TurnState getTurnState() {
        return this.turnState;
    }

    /**
     * Setter for the current state
     * @param newState the state we want to switch to
     */
    public void setState(State newState){ this.state = newState; }

    /**
     * Setter for the current turn state
     * @param turnState the turn state we want to switch to
     */
    public void setTurnState(TurnState turnState) {
        this.turnState = turnState;
    }

    /**
     * Players getter
     * @return ArrayList<Player>
     */
    public ArrayList<Player> getPlayers() {
        return (ArrayList<Player>) this.players.clone();
    }

    public void addPlayer(Player player){
        if(players.size() < 4){
            players.add(player);
        } else {
            System.err.println("More than 4 players in a game");
        }
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
    public Player getWinner()   { return winner; }
}
