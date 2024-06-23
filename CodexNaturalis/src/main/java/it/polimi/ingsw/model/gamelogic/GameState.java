package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.gamelogic.gamechat.GameChat;
import it.polimi.ingsw.model.gamelogic.gamechat.Message;

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

    private GameChat gameChat;

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
        gameChat=new GameChat();
    }

    /**
     * chat getter
     * @return the chat
     */
    public GameChat getChat() {
        return this.gameChat;
    }

    /**
     * save message method for gamechat
     * @param message the new message
     */
    public void saveMessage(Message message)
    {
        if(players.contains(message.getAuthor())) {
            gameChat.saveMessage(message);
        }
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

    /**
     * add Player method
     * @param player Player to add
     * @throws IndexOutOfBoundsException when there are already 4 players
     */
    public void addPlayer(Player player) throws IndexOutOfBoundsException{
        if(players.size() < 4){
            players.add(player);
        } else {
            throw new IndexOutOfBoundsException();
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
        i=i%this.players.size();
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
     *
     */
    private void ComputeWinner()
    {
        int points=-1;
        int goalCount=-1;
        Player currWinner = null;
        for(Player player: this.players)
        {
            //for each player find the corresponding max points and in case of equality choose the one with more achieved points
            if(gameTable.getScoreBoard().getPoints(player)>points || (gameTable.getScoreBoard().getPoints(player)==points && gameTable.getScoreBoard().GetReachedPointsCount(player)>goalCount))
            {
                points=gameTable.getScoreBoard().getPoints(player);
                goalCount=gameTable.getScoreBoard().GetReachedPointsCount(player);
                currWinner=player;
            }


        }

        winner=currWinner;
    }

/**
 * Gets the winner, to add exception for when the winner is not set or in the wrong game state
 * @return Player
 */
    public Player getWinner()   {

        if(winner==null)
            ComputeWinner();//no need to recall this method more than one time for the same game
        return winner;

    }
}
