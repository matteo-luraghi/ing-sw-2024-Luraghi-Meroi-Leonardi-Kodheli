package it.polimi.ingsw.controller;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.message.serverMessage.GoalCardRequest;
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
    private Player player; //Why this is needed?

    // TODO: use the client handlers to send messages to the clients
    private final ArrayList<ConnectionHandler> connectionHandlers;
    private final Lock connectionLock;
    private boolean isGameStarted;
    private boolean isGameEnded;

    /**
     * Constructor without parameters
     */
    public Controller() {
        this.connectionLock = new ReentrantLock();
        this.connectionHandlers = new ArrayList<>();
    }
    /**
     * Controller constructor
     * @param game Game that the megaController manages
     */
    public Controller(GameState game){
        this.game = game;
        this.scanner = new Scanner(System.in); //probably don't need this
        //connection attributes setup
        connectionLock = new ReentrantLock();
        this.connectionHandlers = new ArrayList<>();
    }

    /**
     * gameState getter
     * @return the current gameState;
     */
    public GameState getGame() {
        return game;
    }

    /**
     * gameState setter
     * @param game the game
     */
    public void setGame(GameState game) {
        this.game = game;
    }

    /**
     * Client handlers getter
     * @return list of handlers
     */
    public ArrayList<ConnectionHandler> getHandlers() {
        return this.connectionHandlers;
    }

    /**
     * Add a client handler to the list
     * @param handler client handler to be added
     */
    public void addHandler(ConnectionHandler handler) {
        this.connectionHandlers.add(handler);
    }

    /**
     * Client Handler getter by nickname
     * @param nickname the nickname of a Player
     * @return the corresponding ConnectionHandler
     */
    public ConnectionHandler getHandlerByNickname(String nickname) {
        connectionLock.lock();
        try {
            for(ConnectionHandler c: this.connectionHandlers) {
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
     * @param msg the message to be sent
     */
    public void broadcastMessage(Serializable msg) {
        connectionLock.lock();
        try {
            for(ConnectionHandler c : this.connectionHandlers) {
                c.sendMessageClient(msg);
            }
        } finally {
            connectionLock.unlock();
        }
    }

    /**
     * isGamEnded setter
     * @param gameEnded value
     */
    public void setGameEnded(boolean gameEnded) {
        this.isGameEnded = gameEnded;
    }

    /**
     * isGameEnded getter
     * @return value
     */
    public boolean isGameEnded() {
        return this.isGameEnded;
    }

    /**
     * isGameStarted setter
     * @param gameStarted value
     */
    public void setGameStarted(boolean gameStarted) {
        this.isGameStarted = gameStarted;
    }

    /**
     * isGameStarted getter
     * @return value
     */
    public boolean isGameStarted() {
        return this.isGameStarted;
    }

    /**
     * Get all the starting cards from the json files
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
    public void start() {
        this.isGameStarted = true;

        Queue<StartingCard> startingCardsQueue = getStartingCards();
        Queue<GoalCard> goalCardsQueue = getGoalCards();
        ArrayList<Player> players = new ArrayList<>();
        Map<Player, PlayerField> playerZones = new HashMap<>();

        for (ConnectionHandler c: this.connectionHandlers) {
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
    }

    /**
     * Asks the first player for the number of players that will be playing this game
     */
    protected void giveNumberPlayers(){
        int numOfPlayers;
        SetUpState setUpState = (SetUpState) game.getState();
        numOfPlayers = setUpState.getNumberOfPlayers();
        if(numOfPlayers == -1){
            view.showMessage("You are the first player");
            do{
                view.showMessage("Choose the number of players for this game");
                numOfPlayers = scanner.nextInt();
                if(numOfPlayers < 2 || numOfPlayers > 4){
                    view.showMessage("Invalid number of players, choose again");
                }
            }while(numOfPlayers < 2 || numOfPlayers > 4);


            setUpState.setNumberOfPlayers(numOfPlayers);
        } else {
            System.err.println("You aren't the first player, you cannot set how many players there are");
        }
    }

    /**
     * Method to let a user register himself as a player, passing the created player to the model
     */
    protected void setPlayer(){
        String nick;
        String colorString;
        boolean correct;
        Color color = null; //Will always get initialized in the second do-while loop

        //Ask the player for his username
        do {
            nick = scanner.nextLine();
            correct = checkUniqueNickname(nick);
            if(!correct){
                view.showMessage("Username already exists, please choose another username");
            }
        }while (!correct);

        //Ask the player for his chosen color
        do {
            colorString = scanner.nextLine();
            try{
                color = Util.stringToColor(colorString);
                correct = checkUniqueColor(color);
                if(!correct) view.showMessage("Color is already taken, please choose a free color");
            } catch (NullPointerException e){
                view.showMessage("Color doesn't exist, please choose a valid color");
                correct = false;
            }
        }while (!correct);

        view.showWaitingForPlayers();
        Player player = new Player(nick, color);
        game.addPlayer(player);
        this.player = player;
    }

    /**
     * Method that makes the player select his private goal card
     * @param option1 The first goal card offered to the player
     * @param option2 The second goal card offered to the player
     */
    public void choosePrivateGoal(GoalCard option1, GoalCard option2){
        boolean correct = false;
        int option;
        do{
            view.showMessage("Which GoalCard will you choose? (1 or 2)");
            option = scanner.nextInt();
            if(option != 1 && option != 2){
                view.showMessage("Invalid number, choose again");
            } else {
                correct = true;
            }
        }while(!correct);

        switch(option){
            case 1:
                game.getGameTable().getPlayerZones().get(player).setPrivateGoal(option1);
                break;
            case 2:
                game.getGameTable().getPlayerZones().get(player).setPrivateGoal(option2);
                break;
        }
    }

    public void chooseCardToPlay(){
        boolean correct = false;
        int index, x, y;
        ResourceCard card = null;
        Coordinates where = null;
        do{
            view.showMessage("Which card will you play? (1, 2 or 3)");
            index = scanner.nextInt();
            if(index <= 0 || index > game.getGameTable().getPlayerZones().get(player).getHand().size()){
                view.showMessage("Choose a valid card");
            } else {
                card = game.getGameTable().getPlayerZones().get(player).getHand().get(index-1);
                correct = true;
            }
        }while(!correct);
        correct = false;
        do{
            view.showMessage("Where do you want to play that card? (first x coordinate then y coordinate)");
            x = scanner.nextInt();
            y = scanner.nextInt();
            where = new Coordinates(x, y);
            correct = game.getGameTable().getPlayerZones().get(player).IsPlayable(where, card);
            if(!correct)
                view.showMessage("This card cannot be played here!");
        }while(!correct);

        game.getGameTable().getPlayerZones().get(player).Play(where, card);
        view.showMessage("Card played!");
    }

    public void chooseCardToDraw(){
        boolean correct = false;
        int deckChoice, cardChoice = -27;
        Deck deck;
        do{
            view.showMessage("From which deck will you draw? (1 for resource, 2 for gold");
            deckChoice = scanner.nextInt();
            if(deckChoice != 1 && deckChoice != 2){
                view.showMessage("Invalid Choice");
            } else {
                do{
                    view.showMessage("Which card will you draw? (0 for top of deck, 1 or 2 for uncovered cards");
                    cardChoice = scanner.nextInt();
                    if(cardChoice < 0 || cardChoice > 2){
                        view.showMessage("Invalid Choice");
                    } else {
                        correct = true;
                    }
                }while(!correct);
            }
        }while(!correct);

        deck = deckChoice == 1 ? game.getGameTable().getResourceDeck() : game.getGameTable().getGoldDeck();
        game.getGameTable().getPlayerZones().get(player).draw(deck, cardChoice);
    }

    /**
     * Method that checks if a player nickname is already present in the game
     * @param nick the nickname that needs checking
     * @return true if it's NOT already present, false if it is
     */
    private boolean checkUniqueNickname(String nick){
        for(Player p : getGame().getPlayers()){
            if(p.getNickname().equals(nick)) return false;
        }
        return true;
    }

    /**
     * Method that checks if a player chosen color is already present in the game
     * @param color the color that needs checking
     * @return true if it's NOT already present, false if it is
     */
    private boolean checkUniqueColor(Color color){
        for(Player p : getGame().getPlayers()){
            if(p.getColor() == color) return false;
        }
        return true;
    }
}
