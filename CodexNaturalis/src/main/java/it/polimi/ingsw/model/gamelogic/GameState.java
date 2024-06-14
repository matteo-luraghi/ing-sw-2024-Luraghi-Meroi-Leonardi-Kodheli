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
     * get a message by Index
     * @param index index of the wanted message
     * @return Message if the index is in bounds
     * @throws IndexOutOfBoundsException when index out of bounds
     */
    public Message getMessageByIndex(int index) throws IndexOutOfBoundsException
    {
        if(index<0 || index>=gameChat.getMessages().size())
            throw new IndexOutOfBoundsException();
        return gameChat.getMessages().get(index);
    }

    /**
     * gets the messages of gamechat
     * @return Arraylist of message
     */
    public ArrayList<Message> getMessages()
    {
        return  gameChat.getMessages();
    }
    /**
     * lastMessage getter
     * @return Message
     */
    public Message getLastMessage()
    {
        return gameChat.getLastMessage();
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
        int goalCount=0;
        Player CurrWinner = null;
        for(Player player: this.players)
        {
            if(gameTable.getScoreBoard().getPoints(player)>points || (gameTable.getScoreBoard().getPoints(player)==points && gameTable.getScoreBoard().GetReachedPointsCount(player)>goalCount))
            {
                points=gameTable.getScoreBoard().getPoints(player);
                goalCount=gameTable.getScoreBoard().GetReachedPointsCount(player);
                CurrWinner=player;
            }


        }

        return CurrWinner;
    }

/**
 * Gets the winner, to add exception for when the winner is not set or in the wrong game state
 * @return Player
 */
    public Player getWinner()   {

        return ComputeWinner(); }
}
