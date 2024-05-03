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
    private final int numOfPlayers;
    private final ArrayList<ConnectionHandler> connectionHandlers;
    private final Lock connectionLock;
    private boolean isGameStarted;
    private boolean isGameEnded;
    private boolean isPenultimateTurn;
    private boolean isLastTurn;
    private int numOfGoalCardsChosen;
    /**
     * Constructor without parameters
     */
    public Controller(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
        this.connectionLock = new ReentrantLock();
        this.connectionHandlers = new ArrayList<>();
        isGameStarted = false;
        isGameEnded = false;
        isPenultimateTurn = false;
        isLastTurn = false;
        numOfGoalCardsChosen = 0;
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
        JsonParser parser = new JsonParser();
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
        JsonParser parser = new JsonParser();
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

    public void chooseColorState(ConnectionHandler connectionHandler) {
        ArrayList<Color> availableColors = new ArrayList<>(List.of(Color.values()));
        for(ConnectionHandler c : getHandlers()){
            availableColors.remove(c.getClientColor());
        }
        connectionHandler.sendMessageClient(new ColorRequest(availableColors));
    }

    public void setColor(ConnectionHandler connectionHandler, Color color){
        for(ConnectionHandler c: getHandlers()){
            if(c.getClientColor() != null && c.getClientColor() == color){
                connectionHandler.sendMessageClient(new TextMessage("That color is unavailable, try again!"));
                chooseColorState(connectionHandler);
                return;
            }
        }
        //if the program arrives here, the color is available
        connectionHandler.setClientColor(color);
        if(!this.isGameStarted()) {
            connectionHandler.sendMessageClient(new WaitingForPlayers());
            checkGame();
        }
    }

    public void checkGame() {
        if (getHandlers().size() == this.numOfPlayers) {
            start();
        }
    }

    /**
     * Start the game, creating the necessary resources
     */
    public void start() {
        Queue<StartingCard> startingCardsQueue = getStartingCards();
        Queue<GoalCard> goalCardsQueue = getGoalCards();
        ArrayList<Player> players = new ArrayList<>();
        Map<Player, PlayerField> playerZones = new HashMap<>();
        Map<Player, GoalCard[]> privateGoals = new HashMap<>();
        Map<Player, StartingCard> startingCards = new HashMap<>();

        for (ConnectionHandler c : getHandlers()) {
            Player player = new Player(c.getClientNickname(), c.getClientColor());
            players.add(player);
            // randomly pick a starting card for the user
            startingCards.put(player, startingCardsQueue.poll());
            PlayerField field = new PlayerField();
            playerZones.put(player, field);
            // make the user choose a goal card
            GoalCard[] goalCardsOptions = new GoalCard[2];
            goalCardsOptions[0] = goalCardsQueue.poll();
            goalCardsOptions[1] = goalCardsQueue.poll();
            privateGoals.put(player, goalCardsOptions);
        }

        ScoreBoard scoreBoard = new ScoreBoard(players);
        // pick the common goals from the remaining goal cards
        GoalCard[] goalCards = new GoalCard[2];
        goalCards[0] = goalCardsQueue.poll();
        goalCards[1] = goalCardsQueue.poll();

        GameTable table = new GameTable(new Deck(false), new Deck(true), playerZones, goalCards, scoreBoard);
        // the first player is the starting one
        this.game = new GameState(players, players.get(0), table);
        this.isGameStarted = true;

        for (ConnectionHandler c: getHandlers()) {
            Player player = null;
            for (Player p : players) {
                if (c.getClientNickname().equals(p.getNickname())) {
                    player = p;
                }
            }
            if (player != null) {
                game.getGameTable().getPlayerZones().get(player).draw(game.getGameTable().getGoldDeck(), 0);
                game.getGameTable().getPlayerZones().get(player).draw(game.getGameTable().getResourceDeck(), 0);
                game.getGameTable().getPlayerZones().get(player).draw(game.getGameTable().getResourceDeck(), 0);
                c.sendMessageClient(new PlayStartingCardRequest(startingCards.get(player)));
                c.sendMessageClient(new GoalCardRequest(privateGoals.get(player)));
            }
        }
    }

    /**
     * Sets the chosen startingCard as the startingCard for a certain player
     * @param card the starting card chosen
     * @param isFront the way it needs to face
     * @param connectionHandler the player that is sending the request
     */
    public void setStartingCard(StartingCard card, boolean isFront, ConnectionHandler connectionHandler){
        Player currentPlayer = null;
        for(Player p : game.getPlayers()){
            if(p.getNickname().equals(connectionHandler.getClientNickname())) {
                currentPlayer = p;
            }
        }
        if(currentPlayer == null){
            System.err.println("Didn't find a player with your nickname");
            return;
        }
        if(card.getIsFront() != isFront){
            card.flip();
        }
        game.getGameTable().getPlayerZones().get(currentPlayer).addStartingCard(card);
    }
    /**
     * Sets the chosen goal card as a private goal for a certain player
     * @param goal the goal card chosen
     * @param connectionHandler the player that is sending the request
     */
    public void setPrivateGoalCard(GoalCard goal, ConnectionHandler connectionHandler){
        Player currentPlayer = null;
        for(Player p : game.getPlayers()){
            if(p.getNickname().equals(connectionHandler.getClientNickname())) {
                currentPlayer = p;
            }
        }
        if(currentPlayer == null){
            System.err.println("Didn't find a player with your nickname");
            return;
        }

        game.getGameTable().getPlayerZones().get(currentPlayer).setPrivateGoal(goal);

        // if it's not the player's turn notify them
        if(!currentPlayer.getNickname().equals(game.getTurn().getNickname())) {
            connectionHandler.sendMessageClient(new TurnEnded(currentPlayer, game));
        }

        numOfGoalCardsChosen++;
        if(numOfGoalCardsChosen == getHandlers().size()){ //When all the players have chosen their goal cards
            this.game.setState(State.GAMEFLOW);
            yourTurnState();
        }
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
     * Make the player play a card if it can be played, otherwise make the player chose a new pair of card/where
     * @param card The card that needs to be played
     * @param where Where the card needs to be played
     * @param connectionHandler The player who is playing the card
     */
    public void playCard(ConnectionHandler connectionHandler, ResourceCard card, Coordinates where, boolean isFront) {
        Player currentPlayer = game.getTurn();
        if(card.getIsFront() != isFront){
            card.flip();
        }
        if(!currentPlayer.getNickname().equals(connectionHandler.getClientNickname())){
            System.err.println("Someone who isn't the current player is playing");
            return;
        }

        PlayerField playerZone = game.getGameTable().getPlayerZones().get(currentPlayer);
        if(playerZone.IsPlayable(where, card)){
            int score = playerZone.Play(where, card);
            game.getGameTable().getScoreBoard().addPoints(currentPlayer, score);
            drawCardState();
        }else{
            connectionHandler.sendMessageClient(new TextMessage("Unable to play the card, try again"));
            playCardState();
        }
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
     * Make the player draw a card if it can be drawn, otherwise make the player draw from somewhere else
     * @param which the card he wants to draw
     * @param isGold which deck he wants to draw from
     * @param connectionHandler The player who is playing the card
     */
    public void drawCard(ConnectionHandler connectionHandler, int which, boolean isGold) {
        Player currentPlayer = game.getTurn();
        if(!currentPlayer.getNickname().equals(connectionHandler.getClientNickname())){
            System.err.println("Someone who isn't the current player is playing");
            return;
        }

        PlayerField playerZone = game.getGameTable().getPlayerZones().get(currentPlayer);
        Deck deck;

        //choose the appropriate deck
        if(isGold)  deck = game.getGameTable().getGoldDeck();
        else        deck = game.getGameTable().getResourceDeck();

        //Draw the card
        try{
            ResourceCard drawnCard;
            playerZone.draw(deck, which);

            //Won't be called if the card isn't drawn thanks to NullPointerExceptions
            changeTurnState();
        }catch (NullPointerException e){
            connectionHandler.sendMessageClient(new TextMessage("Unable to draw the card, try again"));
            drawCardState();
        }
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
}