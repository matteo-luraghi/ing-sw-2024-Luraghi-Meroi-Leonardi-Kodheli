package it.polimi.ingsw.controller;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.message.serverMessage.*;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.*;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Controller class, manages player input and sends messages to the model
 * @author Gabriel Leonardi
 */
public class Controller {
    private GameState game;
    protected Scanner scanner;
    private final ArrayList<ConnectionHandler> connectionHandlers;
    private final Lock connectionLock;
    private boolean isGameStarted;
    private boolean isGameEnded;
    private boolean isPenultimateTurn;
    private boolean isLastTurn;
    /**
     * Constructor without parameters
     */
    public Controller() {
        this.connectionLock = new ReentrantLock();
        this.connectionHandlers = new ArrayList<>();
        isGameStarted = false;
        isGameEnded = false;
        isPenultimateTurn = false;
        isLastTurn = false;
    }

    /**
     * Controller constructor
     *
     * @param game Game that the megaController manages
     */
    public Controller(GameState game) {
        this.game = game;
        this.scanner = new Scanner(System.in); //probably don't need this
        //connection attributes setup
        connectionLock = new ReentrantLock();
        this.connectionHandlers = new ArrayList<>();
        isGameStarted = false;
        isGameEnded = false;
        isPenultimateTurn = false;
        isLastTurn = false;
    }

    /**
     * gameState getter
     *
     * @return the current gameState;
     */
    public GameState getGame() {
        return game;
    }

    /**
     * gameState setter
     *
     * @param game the game
     */
    public void setGame(GameState game) {
        this.game = game;
    }

    /**
     * Client handlers getter
     *
     * @return list of handlers
     */
    public ArrayList<ConnectionHandler> getHandlers() {
        return this.connectionHandlers;
    }

    /**
     * Add a client handler to the list
     *
     * @param handler client handler to be added
     */
    public void addHandler(ConnectionHandler handler) {
        this.connectionHandlers.add(handler);
    }

    /**
     * Client Handler getter by nickname
     *
     * @param nickname the nickname of a Player
     * @return the corresponding ConnectionHandler
     */
    public ConnectionHandler getHandlerByNickname(String nickname) {
        connectionLock.lock();
        try {
            for (ConnectionHandler c : this.connectionHandlers) {
                if (c.getClientNickname().equals(nickname)) {
                    return c;
                }
            }
        } finally {
            connectionLock.unlock();
        }
        return null;
    }

    /**
     * Send a message to all the clients
     *
     * @param msg the message to be sent
     */
    public void broadcastMessage(Serializable msg) {
        connectionLock.lock();
        try {
            for (ConnectionHandler c : this.connectionHandlers) {
                c.sendMessageClient(msg);
            }
        } finally {
            connectionLock.unlock();
        }
    }

    /**
     * isGamEnded setter
     *
     * @param gameEnded value
     */
    public void setGameEnded(boolean gameEnded) {
        this.isGameEnded = gameEnded;
    }

    /**
     * isGameEnded getter
     *
     * @return value
     */
    public boolean isGameEnded() {
        return this.isGameEnded;
    }

    /**
     * isGameStarted setter
     *
     * @param gameStarted value
     */
    public void setGameStarted(boolean gameStarted) {
        this.isGameStarted = gameStarted;
    }

    /**
     * isGameStarted getter
     *
     * @return value
     */
    public boolean isGameStarted() {
        return this.isGameStarted;
    }

    /**
     * Get all the starting cards from the json files
     *
     * @return the shuffled queue of starting cards
     */
    private Queue<StartingCard> getStartingCards() {
        List<StartingCard> cardsList = new ArrayList<>();
        JsonParser parser = new JsonParser();;
        for(int i=1; i<=6; i++) {
            String cardPath = "./src/main/resources/CardsJSON/startingCards/startingCard" + i + ".json";
            try(Reader reader = new FileReader(cardPath)) {
                JsonObject parsedStartingCard = parser.parse(reader).getAsJsonObject();
                cardsList.add(Util.fromJSONtoStartingCard(parsedStartingCard));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.shuffle(cardsList);
        return new LinkedList<>(cardsList);
    }

    /**
     * Get all the goal cards from the json files
     *
     * @return the shuffled queue of goal cards
     */
    private Queue<GoalCard> getGoalCards() {
        List<GoalCard> cardsList = new ArrayList<>();
        JsonParser parser = new JsonParser();;
        for(int i=1; i<=8; i++) {
            String cardPath = "./src/main/resources/CardsJSON/goalCards/goalCard" + i + ".json";
            try(Reader reader = new FileReader(cardPath)) {
                JsonObject parsedGoalCard = parser.parse(reader).getAsJsonObject();
                boolean isResourceGoal=parsedGoalCard.get("isResourceGoal").getAsBoolean();
                if(isResourceGoal)
                {
                    cardsList.add(Util.fromJSONtoResourceGoalCard(parsedGoalCard));
                }
                else
                {
                    cardsList.add(Util.fromJSONtoPositionGoalCard(parsedGoalCard));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Collections.shuffle(cardsList);
        return new LinkedList<>(cardsList);
    }

    /**
     * Start the game, creating the necessary resources
     */
    public void start() {
        this.isGameStarted = true;

        Queue<StartingCard> startingCardsQueue = getStartingCards();
        Queue<GoalCard> goalCardsQueue = getGoalCards();
        ArrayList<Player> players = new ArrayList<>();
        Map<Player, PlayerField> playerZones = new HashMap<>();

        for (ConnectionHandler c : this.connectionHandlers) {
            Player player = new Player(c.getClientNickname(), c.getClientColor());
            players.add(player);
            // randomly pick a starting card for the user
            StartingCard startingCard = startingCardsQueue.poll();
            PlayerField field = new PlayerField(startingCard);
            playerZones.put(player, field);
            // make the user choose a goal card
            GoalCard[] goalCardsOptions = new GoalCard[2];
            goalCardsOptions[0] = goalCardsQueue.poll();
            goalCardsOptions[1] = goalCardsQueue.poll();
            c.sendMessageClient(new GoalCardRequest(goalCardsOptions));
        }

        ScoreBoard scoreBoard = new ScoreBoard(players);
        // pick the common goals from the remaining goal cards
        GoalCard[] goalCards = new GoalCard[2];
        goalCards[0] = goalCardsQueue.poll();
        goalCards[1] = goalCardsQueue.poll();

        GameTable table = new GameTable(new Deck(false), new Deck(true), playerZones, goalCards, scoreBoard);
        // the first player is the starting one
        this.game = new GameState(players, players.get(0), table);
        this.game.setState(State.GAMEFLOW);
        yourTurnState();
    }

    /**
     * Send a YourTurn message to the player that needs to play
     */
    public void yourTurnState() {
        Player currentPlayer = game.getTurn();
        ConnectionHandler c = getHandlerByNickname(currentPlayer.getNickname());
        c.sendMessageClient(new YourTurn());
    }

    /**
     * Send a PlayCardRequest message to the player that needs to choose which card to play
     */
    public void playCardState() {
        Player currentPlayer = game.getTurn();
        ConnectionHandler c = getHandlerByNickname(currentPlayer.getNickname());
        game.setTurnState(TurnState.PLAY);
        c.sendMessageClient(new PlayCardRequest(currentPlayer, game));
    }
    /**
     * Send a DrawCardRequest message to the player that needs to choose which card to draw
     */
    public void drawCardState(){
        Player currentPlayer = game.getTurn();
        ConnectionHandler c = getHandlerByNickname(currentPlayer.getNickname());
        game.setTurnState(TurnState.DRAW);
        c.sendMessageClient(new DrawCardRequest(game, currentPlayer));
    }
    /**
     * Passes the turn to the next player, while checking if it's the penultimate or the last turn
     */
    public void changeTurnState(){
        game.nextTurn();
        //Here i'm assuming players[0] is the first player, which now is the case but it might not be in the future
        if(game.getTurn() == game.getPlayers().getFirst()){
            if(isLastTurn){
                for(ConnectionHandler c : getHandlers()){
                    c.sendMessageClient(new TextMessage("Counting goals..."));
                    countGoals();
                    return;
                }
            } else if (isPenultimateTurn) {
                for(ConnectionHandler c : getHandlers()){
                    c.sendMessageClient(new TextMessage("Last turn!!"));
                }
                isLastTurn = true;
            }
        }
        if (!isPenultimateTurn && game.getGameTable().getScoreBoard().getBoard().values().stream().anyMatch(value -> value > 20)){
            //If someone has at least 20 points, we start the countdown
            isPenultimateTurn = true;
            for(ConnectionHandler c : getHandlers()){
                c.sendMessageClient(new TextMessage("Someone has at least 20 points! Starting penultimate turn!"));
            }
        }
    }

    /**
     * Count the points scored by the common and private goal cards
     */
    public void countGoals(){
        game.setState(State.COUNTGOALS);
        for(Player p: game.getPlayers()) {
            game.getGameTable().countGoalPoints(game.getGameTable().getPlayerZones().get(p));
        }
        showLeaderBoard();
    }

    /**
     * Show to all players the winner of the match
     */
    public void showLeaderBoard(){
        game.setState(State.FINAL);
        isGameEnded = true;
        for(ConnectionHandler c : getHandlers()){
            c.sendMessageClient(new Winner(game));
        }
        //Do i have to do something for when the game ends?
    }

    // TODO: implement these mock methods
    public void chooseColorState() {
    }

    public boolean drawCard(int which) {
        return true;
    }

    public boolean cardPlayed(ConnectionHandler connectionHandler, ResourceCard card, Coordinates where) {
        return true;
    }
}