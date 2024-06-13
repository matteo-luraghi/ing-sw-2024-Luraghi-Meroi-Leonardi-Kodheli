package it.polimi.ingsw.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.*;
import it.polimi.ingsw.model.gamelogic.gamechat.GameChat;
import it.polimi.ingsw.model.gamelogic.gamechat.Message;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Controller class, manages player input and sends messages to the model
 * @author Gabriel Leonardi
 */
public class Controller implements RemoteController {
    private GameState game;
    private final int numOfPlayers;
    private final ArrayList<ConnectionHandler> connectionHandlers;
    private final Lock connectionLock;
    private boolean isGameStarted;
    private boolean isGameEnded;
    private boolean isPenultimateTurn;
    private boolean isLastTurn;
    private int numOfGoalCardsChosen;
    private final String gameName;

    /**
     * Constructor
     * @param gameName the game's name
     * @param numOfPlayers the number of players needed for the game
     */
    public Controller(String gameName, int numOfPlayers) {
        this.gameName = gameName;
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
     * gameName getter
     * @return the name of the game
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * gameState getter
     * @return the current gameState;
     */
    @Override
    public GameState getGame() {
        return game;
    }

    /**
     * gameState setter
     * @param game the game
     */
    @Override
    public void setGame(GameState game) {
        this.game = game;
    }

    /**
     * Client handlers getter
     * @return list of handlers
     */
    @Override
    public ArrayList<ConnectionHandler> getHandlers() {
        return this.connectionHandlers;
    }

    /**
     * Add a client handler to the list
     * @param handler client handler to be added
     */
    @Override
    public void addHandler(ConnectionHandler handler) {
        this.connectionHandlers.add(handler);
    }

    /**
     * Client Handler getter by nickname
     * @param nickname the nickname of a Player
     * @return the corresponding ConnectionHandler
     */
    @Override
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
     * @param msg      the message to be sent
     * @param handlers all the handlers to send the message to
     */
    @Override
    public void broadcastMessage(Serializable msg, ArrayList<ConnectionHandler> handlers) {
        connectionLock.lock();
        try {
            for (ConnectionHandler c : handlers) {
                c.sendMessage(msg);
            }
        } finally {
            connectionLock.unlock();
        }
    }

    /**
     * isGamEnded setter
     * @param gameEnded value
     */
    @Override
    public void setGameEnded(boolean gameEnded) {
        this.isGameEnded = gameEnded;
    }

    /**
     * isGameEnded getter
     * @return value
     */
    @Override
    public boolean isGameEnded() {
        return this.isGameEnded;
    }

    /**
     * isGameStarted setter
     * @param gameStarted value
     */
    @Override
    public void setGameStarted(boolean gameStarted) {
        this.isGameStarted = gameStarted;
    }

    /**
     * isGameStarted getter
     * @return value
     */
    @Override
    public boolean isGameStarted() {
        return this.isGameStarted;
    }

    /**
     * Get all the starting cards from the json files
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
                System.err.println("Error parsing starting cards");
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
        for(int i=1; i<=16; i++) {
            String cardPath = "./src/main/resources/CardsJSON/goalCards/goalCard" + i + ".json";
            try(Reader reader = new FileReader(cardPath)) {
                JsonObject parsedGoalCard = parser.parse(reader).getAsJsonObject();
                boolean isResourceGoal=parsedGoalCard.get("isResourceGoal").getAsBoolean();
                if(isResourceGoal) {
                    cardsList.add(Util.fromJSONtoResourceGoalCard(parsedGoalCard));
                } else {
                    cardsList.add(Util.fromJSONtoPositionGoalCard(parsedGoalCard));
                }
            } catch (IOException e) {
                System.err.println("Error parsing goal cards");
            }
        }
        Collections.shuffle(cardsList);
        return new LinkedList<>(cardsList);
    }


    /**
     * sets the state as choose color
     * @param connectionHandler the current connection handler
     */
    @Override
    public void chooseColorState(ConnectionHandler connectionHandler) {
        ArrayList<Color> availableColors = new ArrayList<>(List.of(Color.values()));
        for(ConnectionHandler c : getHandlers()){
            availableColors.remove(c.getClientColor());
        }
        connectionHandler.colorRequest(availableColors);
    }

    /**
     * sets the player's color
     * @param connectionHandler the player's connection handler
     * @param color the chosen color
     */
    @Override
    public void setColor(ConnectionHandler connectionHandler, Color color){
        for(ConnectionHandler c: getHandlers()){
            if(c.getClientColor() != null && c.getClientColor() == color && !c.getClientNickname().equals(connectionHandler.getClientNickname())){
                connectionHandler.sendTextMessage("That color is unavailable, try again!");
                chooseColorState(connectionHandler);
                return;
            }
        }
        //if the program arrives here, the color is available

        // handle both socket and RMI cases
        for (ConnectionHandler c: getHandlers()) {
            if (c.equals(connectionHandler)) {
                c.setClientColor(color);
            }
        }
        if(!this.isGameStarted()) {
            connectionHandler.waitingForPlayers();
            checkGame();
        }
    }

    /**
     * checks if the game is ready to start
     * @throws RemoteException to handle exceptions that may occur using RMI
     */
    @Override
    public void checkGame() {
        Optional<ConnectionHandler> userWithoutColor = getHandlers().stream().parallel()
                .filter(ch -> ch.getClientColor() == null)
                .findFirst();
        if (getHandlers().size() == this.numOfPlayers && userWithoutColor.isEmpty()) {
            start();
        }
    }

    /**
     * Start the game, creating the necessary resources
     */
    @Override
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
            c.setPlayer(player);

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
                //Make the player draw their initial hand
                game.getGameTable().getPlayerZones().get(player).draw(game.getGameTable().getGoldDeck(), 0);
                game.getGameTable().getPlayerZones().get(player).draw(game.getGameTable().getResourceDeck(), 0);
                game.getGameTable().getPlayerZones().get(player).draw(game.getGameTable().getResourceDeck(), 0);

                c.playStartingCardRequest(startingCards.get(player));
                c.goalCardRequest(privateGoals.get(player));
            }
        }
    }

    /**
     * Sets the chosen startingCard as the startingCard for a certain player
     * @param card the starting card chosen
     * @param isFront the way it needs to face
     * @param connectionHandler the player that is sending the request
     */
    @Override
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
    @Override
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

        numOfGoalCardsChosen++;
        if(numOfGoalCardsChosen != getHandlers().size()) {
            connectionHandler.sendTextMessage("Other players are still choosing");
        } else { //When all the players have chosen their goal cards
            for(ConnectionHandler c : getHandlers()) {

                c.updateGame(game);
                if(!c.getClientNickname().equals(game.getTurn().getNickname())){
                    c.notYourTurn("It's not your turn");
                }
                c.listenForCommands();
            }
            this.game.setState(State.GAMEFLOW);
            yourTurnState();
        }
    }

    /**
     * Send a YourTurn message to the player that needs to play
     */
    @Override
    public void yourTurnState() {
        Player currentPlayer = game.getTurn();
        ConnectionHandler c = getHandlerByNickname(currentPlayer.getNickname());
        c.yourTurn();
    }

    /**
     * Send a PlayCardRequest message to the player that needs to choose which card to play
     */
    @Override
    public void playCardState() {
        Player currentPlayer = game.getTurn();
        ConnectionHandler c = getHandlerByNickname(currentPlayer.getNickname());
        game.setTurnState(TurnState.PLAY);
        c.playCardRequest(currentPlayer);
    }

    /**
     * Make the player play a card if it can be played, otherwise make the player chose a new pair of card/where
     * @param connectionHandler The player who is playing the card
     * @param card The card that needs to be played
     * @param where Where the card needs to be played
     * @param isFront The side to play the card
     */
    @Override
    public void playCard(ConnectionHandler connectionHandler, ResourceCard card, Coordinates where, boolean isFront) {
        Player currentPlayer = game.getTurn();
        if(card.getIsFront() != isFront){
            card.flip();
        }
        if(!currentPlayer.getNickname().equals(connectionHandler.getClientNickname())){
            connectionHandler.sendTextMessage("You aren't the current player");
            return;
        }
        if(game.getState() != State.GAMEFLOW || game.getTurnState() != TurnState.PLAY){
            connectionHandler.sendTextMessage("It's not the time to play a card");
            return;
        }

        PlayerField playerZone = game.getGameTable().getPlayerZones().get(currentPlayer);
        if(playerZone.IsPlayable(where, card)){
            // play the card and update points
            int score = playerZone.Play(where, card);
            if(score == -1) {
                connectionHandler.sendTextMessage("Unable to play the card, try again");
                playCardState();
                return;
            }
            game.getGameTable().getScoreBoard().addPoints(currentPlayer, score);

            // update game for all the players
            for (Player player: game.getPlayers()) {
                ConnectionHandler c = getHandlerByNickname(player.getNickname());
                c.updateGame(game);
            }

            //code to skip drawing phase when the decks are empty
            if (game.getGameTable().getGoldDeck().isDeckEmpty() && game.getGameTable().getResourceDeck().isDeckEmpty()) {
                ConnectionHandler c = getHandlerByNickname(currentPlayer.getNickname());
                c.sendTextMessage("The decks are empty. Skipping draw phase.");
                changeTurnState();
            } else {
                drawCardState();
            }
            // next phase
            drawCardState();
        }else{
            connectionHandler.sendTextMessage("Unable to play the card, try again");
            playCardState();
        }
    }

    /**
     * Send a DrawCardRequest message to the player that needs to choose which card to draw
     */
    @Override
    public void drawCardState(){
        Player currentPlayer = game.getTurn();
        ConnectionHandler c = getHandlerByNickname(currentPlayer.getNickname());
        game.setTurnState(TurnState.DRAW);
        c.drawCardRequest(currentPlayer);
    }

    /**
     * Make the player draw a card if it can be drawn, otherwise make the player draw from somewhere else
     * @param connectionHandler The player who is playing the card
     * @param which the card he wants to draw
     * @param isGold which deck he wants to draw from
     */
    @Override
    public void drawCard(ConnectionHandler connectionHandler, int which, boolean isGold) {
        Player currentPlayer = game.getTurn();
        if(!currentPlayer.getNickname().equals(connectionHandler.getClientNickname())){
            connectionHandler.sendTextMessage("You aren't the current player");
            return;
        }
        if(game.getState() != State.GAMEFLOW || game.getTurnState() != TurnState.DRAW){
            connectionHandler.sendTextMessage("It's not the time to draw a card");
            return;
        }

        PlayerField playerZone = game.getGameTable().getPlayerZones().get(currentPlayer);
        Deck deck;

        //choose the appropriate deck
        if(isGold)  {
            deck = game.getGameTable().getGoldDeck();
        } else {
            deck = game.getGameTable().getResourceDeck();
        }

        //Draw the card
        try{
            playerZone.draw(deck, which);

            //Won't be called if the card isn't drawn thanks to NullPointerExceptions
            connectionHandler.notYourTurn("Your turn has ended!");
            //Update the game for everyone
            for(ConnectionHandler c : getHandlers()) {
                c.updateGame(game);
            }
            changeTurnState();
        }catch (NullPointerException e){
            connectionHandler.sendTextMessage("Unable to draw the card, try again");
            drawCardState();
        }
    }

    /**
     * Passes the turn to the next player, while checking if it's the penultimate or the last turn
     */
    @Override
    public void changeTurnState(){
        //TODO: Sometimes it takes one more turn, look up old git from Lorenzo Mevoi
        //Should be fixed, test for this later
        game.nextTurn();

        //code to skip to the end of a game if every hand is empty
        int endGame = 0;
        for (Player p : game.getPlayers()) {
            if (game.getGameTable().getPlayerZones().get(p).getHand().isEmpty()) {
                endGame++;
            }
        }
        if (endGame == game.getPlayers().size()) {
            for(ConnectionHandler c : getHandlers()){
                c.sendTextMessage("Counting goals...");
            }
            countGoals();
            return;
        }
        //this should be the code to skip turns when someone is stuck
        Player currentPlayer = game.getTurn();

        ConnectionHandler currentPlayerHandler = getHandlerByNickname(currentPlayer.getNickname());

        if (!game.getGameTable().getPlayerZones().get(currentPlayer).canPlayHand() || game.getGameTable().getPlayerZones().get(currentPlayer).getHand().isEmpty()) {
            //the player cannot play any card in any position
            currentPlayerHandler.sendTextMessage("You are stuck, skipping your turn");
            changeTurnState();
            return;
        }


        for (Player player : game.getPlayers()) {
            if (!player.getNickname().equals(game.getTurn().getNickname())) {
                ConnectionHandler c = getHandlerByNickname(player.getNickname());
            }
        }

        if (!isPenultimateTurn && game.getGameTable().getScoreBoard().getBoard().values().stream().anyMatch(value -> value >= 20)){
            //If someone has at least 20 points, we start the countdown
            isPenultimateTurn = true;
            for(ConnectionHandler c : getHandlers()){
                c.sendTextMessage("Someone has at least 20 points! Starting penultimate turn!");
            }
        }
        //Here i'm assuming players[0] is the first player, which now is the case but it might not be in the future
        if(game.getTurn() == game.getPlayers().getFirst()){
            if(isLastTurn){
                for(ConnectionHandler c : getHandlers()){
                    c.sendTextMessage("Counting goals...");
                }
                countGoals();
                return;
            } else if (isPenultimateTurn) {
                for(ConnectionHandler c : getHandlers()){
                    c.sendTextMessage("Last turn!!");
                }
                isLastTurn = true;
            }
        }
        yourTurnState();
    }

    /**
     * Count the points scored by the common and private goal cards
     */
    @Override
    public void countGoals(){
        game.setState(State.COUNTGOALS);
        for(Player p: game.getPlayers()) {
            game.getGameTable().getScoreBoard().addPoints(p, game.getGameTable().countGoalPoints(game.getGameTable().getPlayerZones().get(p)));
        }
        showLeaderBoard();
    }

    /**
     * Show to all players the winner of the match
     */
    @Override
    public void showLeaderBoard(){
        game.setState(State.FINAL);
        isGameEnded = true;
        for(ConnectionHandler c : getHandlers()){
            c.showWinner(game);
            c.showScoreBoard(game.getGameTable().getScoreBoard());
        }
        //Do i have to do something for when the game ends?
    }

    /**
     * Save the message in the chat
     * @param message the message
     */
    @Override
    public void addMessageToChat(Message message) {
        game.saveMessage(message);

        GameChat chat = getGame().getChat();

        for (Player player : game.getPlayers()) {
            ConnectionHandler c = getHandlerByNickname(player.getNickname());
            c.updateChat(chat.filterChat(player));
        }
    }
}