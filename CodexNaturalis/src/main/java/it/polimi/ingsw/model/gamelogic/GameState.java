package it.polimi.ingsw.model.gamelogic;

/**
 * class GameState
 * @author Françesk Kodheli
 */
public class GameState {
    private State State;
    private ArrayList<Player> Players;
    private Player Turn;
    private TurnState TurnState;
    private GameTable GameTable;
    private Player Winner;

    /**
     * GameState Constructor, firstly configured as in SETUP state
     * @param Players ArrayList of the players for the session
     * @param StartingPlayer The choosen player to start
     * @param GameTable Main GameTable Object
     */
    public GameState(ArrayList<Player> Players, Player StartingPlayer, GameTable GameTable)
    {
      this.Players=new ArrayList<Player>();
      for(Player Player: Players)
      {
          this.Players.add(Player);
      }
      Turn=StartingPlayer;
      this.GameTable=GameTable;
      Winner=null;
      State=State.SETUP;

    }

    /**
     * getState returns the current state
     * @return State
     */
    public State getState() {
        return State;
    }

    /**
     * Sets the next state SETUP->GAMEFLOW->COUNTGOALS->FINAL (if final won't be changed)
     * @return true if the state changed
     */
    public boolean NextState()
    {
        if(State != State.FINAL)
        {
            State++;
            if(state==State.COUNTGOALS)
            {
                for(PlayerField PlayerField: GameTable.PlayerZones.keySet()) {
                }
                 GameTable.CountGoalPoints(PlayerField);
                }
            Winner=ComputeWinner();
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
        return Players;
    }

    /**
     * Turn Getter
     * @return Player
     */
    public Player getTurn()
    {
        return Turn;
    }

    /**
     * Sets the next player in the arraylist as the next turn
     */
    public nextTurn()
    {
        int i= Players.indexOf(Turn);
        i++;
        if(i==Players.size())
        {
            i=0;
        }
        Turn=Players.get(i);


    }

    /**
     * TurnState Getter
     * @return TurnState
     */
    public TurnState getTurnState()
    {

        return TurnState;
    }

    /**
     * swithces the turnstate between play and draw
     */
    public ChangeTurnState()
    {
        if(TurnState==TurnState.PLAY)
            TurnState=TurnState.DRAW;
        else
            TurnState=TurnState.PLAY;
    }

    /**
     * GameTable Getter
     * @return GameTable
     */
    public GameTable getGameTable() {
        return GameTable;
    }

    /**
     * Compute the winner by getting the scoreboard
     * @return
     */
    private Player ComputeWinner() {
        int points=0;
        Player CurrWinner;
        for(Palyer Player: Players)
        {
            if(GameTable.GetScoreBoard().GetPoints(Player)>points)
            {
                points=GameTable.GetScoreBoard().GetPoints(Player);
                CurrWinner=Player;
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
        if(State==State.FINAL)
        {
            return Winner;
        }
        return null;
    }
}
