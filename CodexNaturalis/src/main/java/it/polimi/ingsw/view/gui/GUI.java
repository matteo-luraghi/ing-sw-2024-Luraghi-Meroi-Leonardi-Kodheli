package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.connection.ConnectionClosedException;
import it.polimi.ingsw.connection.RemoteServer;
import it.polimi.ingsw.connection.rmi.IPNotFoundException;
import it.polimi.ingsw.connection.rmi.RMIClient;
import it.polimi.ingsw.connection.socket.SocketClient;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.Resource;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.*;
import it.polimi.ingsw.model.gamelogic.gamechat.GameChat;
import it.polimi.ingsw.view.gui.eventhandlers.*;
import it.polimi.ingsw.view.mainview.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * GUI class to show the game using graphic interface
 */
public class GUI extends Application implements View{
    private Client client = null;
    private String sceneName;
    private Stage stage;
    private boolean connectedToServer;
    private EventHandler currentEventHandler;
    private int numOfPlayersChosen;
    private boolean isJoining;
    private String gameName;
    private GameState game = null;
    private Player user = null;
    boolean isDisconnecting;
    private GameChat gameChat;
    /**
     * GUI constructor
     */
    public GUI(){
        this.sceneName = "ConnectToServer.fxml";
        connectedToServer = false;
        numOfPlayersChosen = -1;
        gameName = "";
        isDisconnecting = false;
    }

    /**
     * Method that starts the GUI for a player
     */
    public void start() throws ConnectionClosedException {
        launch();
        //This exception will get thrown after a player has closed the application
        throw new ConnectionClosedException("Connection closed");
    }

    /**
     * Method to open the starting stage (connect to server)
     * @param stage the stage that needs to be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneName));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle(sceneName.split("\\.")[0]);
        stage.setScene(scene);
        stage.getIcons().add(new Image(GUI.class.getResourceAsStream("assets/GUIGraphics/codexNaturalisTransparent.png")));
        stage.show();

        currentEventHandler = fxmlLoader.getController();
        currentEventHandler.setView(this);
    }

    /**
     * Method to change into a particular scene
     * @param sceneName the name of the scene we want to switch to
     */
    public void changeScene(String sceneName){
        this.sceneName = sceneName;
        Platform.setImplicitExit(false);
        Platform.runLater(() -> {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneName));
            Parent root;
            try{
                root = fxmlLoader.load();
                currentEventHandler = fxmlLoader.getController();
                currentEventHandler.setView(this);
                this.stage.setScene(new Scene(root, stage.getWidth(), stage.getHeight()));
                stage.setTitle(sceneName.split("\\.")[0]);
                stage.setOnCloseRequest(e -> {
                    currentEventHandler = fxmlLoader.getController();
                    currentEventHandler.disconnection();
                    Platform.exit();
                    isDisconnecting = true;
                });
                stage.getIcons().add(new Image(GUI.class.getResourceAsStream("assets/GUIGraphics/codexNaturalisTransparent.png")));
                stage.show();
            } catch (IOException ignored){
            }
        });
    }

    /**
     * Get the available games from the server
     */
    public void refreshGameNames() {
        client.refreshGamesNames();
    }



    /**
     * Method to connect a client to a server
     * @param ip the ip of the server
     * @param port the port of the server
     * @param connectionProtocol The connection protocol that the client wants to use (either Socket or RMI)p
     * @throws ConnectionClosedException if unable to connect to the server
     */
    public void connectToServer(String ip, int port, String connectionProtocol) throws ConnectionClosedException {

        if (connectionProtocol.equalsIgnoreCase("socket")){
            try {
                client = new SocketClient(ip, port, this);
                connectedToServer = true;
            } catch (IOException | IllegalArgumentException e) {
                connectedToServer = false;
            }

        }else if (connectionProtocol.equalsIgnoreCase("rmi")){
            try {
                client = new RMIClient(ip, port, this);
                connectedToServer = true;
            } catch (RemoteException | NotBoundException | IllegalArgumentException | IPNotFoundException e) {
                connectedToServer = false;
            }
        }
        if (!connectedToServer){
            throw new ConnectionClosedException("Error connecting to the server");
        }

        if (client.getClass() == RMIClient.class) {
            // get the games' names
            ArrayList<String> gameNames;
            Registry registry = ((RMIClient) client).getServerRegistry();
            try {
                RemoteServer server = (RemoteServer) registry.lookup("server");
                gameNames = server.getGamesNames();
            } catch (Exception e) {
                gameNames = null;
            }

            if (gameNames == null) {
                System.err.println("Error connecting to server");
                throw new ConnectionClosedException("Connection closed");
            }
            // make the player choose to join or create a game
            showJoinOrCreate(gameNames);
        }

        if(connectedToServer){
            new Thread(this::listenForDisconnection).start();
        }
    }

    /**
     * Method to display a message and disconnect the player in case of a disconnection from the server
     */
    public void listenForDisconnection() {
        while(!isDisconnecting) {
            if(client != null && !client.getConnected() && !isDisconnecting) {
                isDisconnecting = true;
                Platform.runLater(() -> {
                    System.out.println("Disconnected");
                    currentEventHandler.disconnection();
                    Platform.runLater(() -> {
                        stage.close();
                    });
                } );
            }
        }
    }
    
    /**
     * Method to show any type of string;
     * this will get shown as a popup in any stage other than player field, where it would be displayed inside the game chat
     * @param s the string you want to be displayed
     */
    @Override
    public void showMessage(String s) {
        Platform.runLater(()->{
            if(currentEventHandler instanceof PlayerFieldController playerFieldController){
                playerFieldController.addChatMessage(s);
            } else {
                currentEventHandler.showPopup(s);
            }
        });
    }

    /**
     * Method to show the joinGame scene
     * @param gameNames the list of all the joinable games
     */
    @Override
    public void showJoinOrCreate(ArrayList<String> gameNames) {
        sceneName = "JoinGame.fxml";
        changeScene(sceneName);
        Platform.runLater(() -> {
            JoinGameController joinGameHandler = (JoinGameController) currentEventHandler;
            joinGameHandler.setGameNames(gameNames);
        });
    }

    /**
     * Update the list of names of available games
     *
     * @param gameNames the names
     */
    @Override
    public void setGameNames(ArrayList<String> gameNames) {
        Platform.runLater(() -> {
            JoinGameController joinGameHandler = (JoinGameController) currentEventHandler;
            joinGameHandler.setGameNames(gameNames);
        });
    }

    /**
     * Set the number of players chosen for the game the player is creating
     * @param num the number of players
     */
    public void setNumOfPlayersChosen(int num){
        numOfPlayersChosen = num;
    }

    /**
     * Set the isJoin flag when joining a game
     * @param isJoining true if you are joining a game, false if you are creating it
     */
    public void setIsJoining(boolean isJoining){ this.isJoining = isJoining; }

    /**
     * Set the name of the game the player is creating or joining
     * @param gameName the name of the game
     */
    public void setGameName(String gameName){ this.gameName = gameName; }

    /**
     * Method to set the parameters to log into a game
     */
    public void setLoginParameters(){
        Platform.runLater(() -> {
            LoginController loginHandler = (LoginController) currentEventHandler;
            loginHandler.setParameters(isJoining, gameName);
        });
    }

    /**
     * Method to show the "Login" fxml file
     */
    @Override
    public void insertNickname(boolean isJoin, String gameName) {
        sceneName = "Login.fxml";
        changeScene(sceneName);
        setLoginParameters();
    }

    /**
     * Method to make the player choose its color
     * @param colors available
     */
    @Override
    public void insertColor(ArrayList<Color> colors) {
        Platform.runLater(() -> {
            LoginController loginController = (LoginController) currentEventHandler;
            loginController.setAvailableColors(colors);
        });
    }

    /**
     * Directly send the number of players to the server (the player will have already chosen the number of players in the form)
     */
    @Override
    public void askForPlayersNumber() {
        //We already have the number of players and the name of the game, so we simulate a response from the client
        client.playersNumberResponse(numOfPlayersChosen, gameName);
    }

    /**
     * Method to display the waiting for players message in loginPage
     */
    @Override
    public void ShowWaitingForPlayers() {
        Platform.runLater(() -> {
            LoginController loginController = (LoginController) currentEventHandler;
            loginController.showWaitingForPlayers();
        });
    }

    /**
     * Shows the setup page and makes the client chose a side of their starting card
     * @param card the starting card that has been randomly assigned to the client
     */
    @Override
    public void ChooseStartingCardSide(StartingCard card) {
        sceneName = "Setup.fxml";
        changeScene(sceneName);
        Platform.runLater(() -> {
            SetupController setupHandler = (SetupController) currentEventHandler;
            setupHandler.setStartingCard(card);
        });
    }

    /**
     * This method doesn't need to show anything in GUI since the player can already see his private goal
     */
    @Override
    public void ShowPrivateGoal(Player player, GameState game) {

    }

    /**
     * Displays the two private goals the client has to choose between
     *
     * @param goalCards is an array of two goal cards
     */
    @Override
    public void ShowChoosePrivateGoal(GoalCard[] goalCards) {
        Platform.runLater(() -> {
            SetupController setupHandler = (SetupController) currentEventHandler;
            setupHandler.setGoalCards(goalCards);
        });
    }

    /**
     * In GUI this method can't be called directly
     *
     * @param playerToSee  specifies which playerfield has to be displayed
     * @param playerAsking tells which player is asking to see it
     * @param game         we are referring to
     */
    @Override
    public void ShowPlayerField(Player playerToSee, Player playerAsking, GameState game) {

    }

    /**
     * Display the player field of a specific player
     *
     * @param playerToSee  specifies which player field has to be displayed
     * @param playerAsking tells which player is asking to see it
     */
    public void ShowPlayerField(Player playerToSee, Player playerAsking) {
        Platform.runLater(() -> {
            PlayerFieldController playerFieldHandler = (PlayerFieldController) currentEventHandler;
            playerFieldHandler.setPlayerFieldOwner(playerToSee);
            playerFieldHandler.setPlayerField(game.getGameTable().getPlayerZoneForUser(playerToSee.getNickname()), playerAsking.getNickname().equals(playerToSee.getNickname()));
        });
    }

    /**
     * Displays the player field of a specific username
     *
     * @param usernameToSee  specifies which player field has to be displayed
     * @param playerAsking tells which player is asking to see it
     */
    public void ShowPlayerFieldFromName(String usernameToSee, Player playerAsking) {
        for(Player p: game.getPlayers()){
            if(p.getNickname().equals(usernameToSee)){
                ShowPlayerField(p, playerAsking);
            }
        }
    }

    /**
     * Displays the two decks and the uncovered cards
     *
     * @param game we are referring to
     */
    @Override
    public void ShowDecks(GameState game) {
        Platform.runLater(() -> {
            PlayerFieldController playerFieldHandler = (PlayerFieldController) currentEventHandler;
            playerFieldHandler.setDecks(game.getGameTable().getResourceDeck(), game.getGameTable().getGoldDeck());
        });
    }

    /**
     * Displays the two decks and the uncovered cards without passing the game
     */
    public void ShowDecks(){
        ShowDecks(game);
    }

    /**
     * Displays the scoreboard
     * @param scoreBoard the scoreboard we want to show
     */
    @Override
    public void ShowScoreBoard(ScoreBoard scoreBoard) {
        Platform.runLater(() -> {
            PlayerFieldController playerFieldHandler = (PlayerFieldController) currentEventHandler;
            playerFieldHandler.setScoreBoard(scoreBoard);
        });
    }

    /**
     * Displays the scoreboard without having to pass the game
     */
    public void showScoreBoard(){
        ShowScoreBoard(game.getGameTable().getScoreBoard());
    }

    /**
     * Shows the common goals for this view's game
     */
    public void showCommonGoals(){
        GoalCard[] commonGoals = new GoalCard[2];
        commonGoals[0] = game.getGameTable().getCommonGoal(0);
        commonGoals[1] = game.getGameTable().getCommonGoal(1);

        Platform.runLater(() -> {
            PlayerFieldController playerFieldHandler = (PlayerFieldController) currentEventHandler;
            playerFieldHandler.setCommonGoals(commonGoals);
        });
    }


    /**
     * Shows the player who has won the game
     *
     * @param game we are referring to
     */
    @Override
    public void ShowWinner(GameState game) {
        Platform.runLater(() -> {
            currentEventHandler.showPopup(game.getWinner().getNickname() + " has won!");
        });
    }

    /**
     * Sets the game chat
     *
     * @param gameChat the chat
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    @Override
    public void setGameChat(GameChat gameChat) throws RemoteException {
        if (this.gameChat == null || !gameChat.getLastMessage().equals(this.gameChat.getLastMessage())){
            showChat();
        }
        this.gameChat = gameChat;
    }

    /**
     * Game setter, update all the fields that update when the game updates
     *
     * @param game the game we need to set!
     */
    @Override
    public void setGame(GameState game) throws RemoteException {
        if(sceneName.equalsIgnoreCase("Setup.fxml")){
            Platform.runLater(()-> {
                currentEventHandler.closePopup();
                sceneName = "PlayerField.fxml";
                changeScene(sceneName);
            });
        }

        this.game = game;
        for (Player p : game.getPlayers()) {
            if (p.equals(this.user)) {
                this.user = p;
                break;
            }
        }

        Platform.runLater(() -> {
            try {
                PlayerFieldController playerFieldHandler = (PlayerFieldController) currentEventHandler;
                playerFieldHandler.setDecks(game.getGameTable().getResourceDeck(), game.getGameTable().getGoldDeck());
                playerFieldHandler.setScoreBoard(game.getGameTable().getScoreBoard());
                Map<Player, Map<Resource, Integer>> resourceMaps = new HashMap<>();
                for (Player p : game.getPlayers()) {
                    resourceMaps.put(p, game.getGameTable().getPlayerZones().get(p).getResourceMap());
                }
                playerFieldHandler.setResourceMaps(resourceMaps);
                playerFieldHandler.setCurrentPlayerField(game);
            } catch (ClassCastException ignored){

            }
        });
    }

    /**
     * This method doesn't need to do anything in GUI since no command is available
     */
    @Override
    public void getCommands() throws RemoteException {

    }

    /**
     * isMyTurn setter
     *
     * @param isMyTurn tells whether it's the client's turn or not
     */
    @Override
    public void setMyTurn(boolean isMyTurn) throws RemoteException {
        if (isMyTurn) {
            client.yourTurnOk();
            Platform.runLater(() -> {
                currentEventHandler.showPopup("Your turn");
            });
        }
    }

    /**
     * PlayPhase setter
     *
     * @param playPhase tells whether it's the client's turn or not
     */
    @Override
    public void setPlayPhase(boolean playPhase) throws RemoteException {

    }

    /**
     * User setter
     *
     * @param user is the user that is going to use this client
     */
    @Override
    public void setUser(Player user) throws RemoteException { this.user = user; }

    /**
     * User getter
     * @return the user that is using this client
     */
    public Player getUser(){ return user; }

    /**
     * Disconnect the client
     */
    @Override
    public void disconnectClient() throws RemoteException {
        Platform.runLater(()->{
            isDisconnecting = true;
            System.out.println("Disconnected");
            currentEventHandler.disconnection();
            Platform.runLater(() -> {
                stage.close();
            });
        });
    }

    /**
     * Client getter
     * @return this client
     */
    public Client getClient(){ return client; }

    /**
     * Method used to play a card from the GUI
     * @param chosenIndex the index of the card in his hand
     * @param coordinates where he wants to play it
     */
    public void playCard(int chosenIndex, Coordinates coordinates, boolean isFront) {
        ResourceCard chosenCard = game.getGameTable().getPlayerZones().get(user).getHand().get(chosenIndex);
        client.playCardResponse(chosenCard, coordinates, isFront);
    }

    /**
     * Method to display the playerField's static content
     */
    public void showStaticContent() {
        Platform.runLater(() -> {
            PlayerFieldController playerFieldHandler = (PlayerFieldController) currentEventHandler;
            playerFieldHandler.setStaticContent(game.getPlayers());
        });
    }

    /**
     * Method to visually update the chat to the player, only gets called when starting up the player field or when a new message arrives
     */
    public void showChat(){
        Platform.runLater(() -> {
            PlayerFieldController playerFieldHandler = (PlayerFieldController) currentEventHandler;
            playerFieldHandler.setListOfMessages(gameChat);
        });
    }
}
