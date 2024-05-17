package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.connection.ConnectionClosedException;
import it.polimi.ingsw.connection.RemoteServer;
import it.polimi.ingsw.connection.rmi.RMIClient;
import it.polimi.ingsw.connection.socket.SocketClient;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.ScoreBoard;
import it.polimi.ingsw.view.gui.eventhandlers.*;
import it.polimi.ingsw.view.mainview.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;

/**
 * GUI class to show the game using graphic interface
 */
public class GUI extends Application implements View{
    private Client client = null;
    private ViewGameCardFactory gameCardViewer;
    private ViewScoreBoardFactory scoreBoardViewer;
    private ViewPlayerFieldFactory playerFieldViewer;
    private ViewDeckFactory deckViewer;
    private ViewGoalCardFactory goalCardViewer;
    private String sceneName;
    private Stage stage;
    private boolean connectedToServer;
    private EventHandler currentEventHandler;
    private int numOfPlayersChosen;
    private boolean isJoining;
    private String gameName;
    private GameState game = null;
    private Player user = null;
    /**
     * GUI constructor
     */
    public GUI(){
        this.deckViewer = new ViewDeckGUIFactory();
        this.gameCardViewer = new ViewGameCardGUIFactory();
        this.playerFieldViewer = new ViewPlayerFieldGUIFactory();
        this.scoreBoardViewer = new ViewScoreBoardGUIFactory();
        this.goalCardViewer = new ViewGoalCardGUIFactory();
        this.sceneName = "ConnectToServer.fxml";
        connectedToServer = false;
        numOfPlayersChosen = -1;
        gameName = "";
    }

    /**
     * Method that starts the GUI for a player
     */
    public void start() throws ConnectionClosedException {
        launch();
    }

    /**
     * Method to open a specific stage for a player
     * @param stage the stage that needs to be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(sceneName));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle(sceneName.split("\\.")[0]);
        stage.setScene(scene);
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
                root = (Parent) fxmlLoader.load();
                currentEventHandler = fxmlLoader.getController();
                currentEventHandler.setView(this);
                this.stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    /**
     * Method to connect a client to a server
     * @param ip the ip of the server
     * @param port the port of the server
     * @param connectionProtocol The connection protocol that the client wants to use (either Socket or RMI)
     * @return true if the connection was successful, false otherwise
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
            } catch (RemoteException | NotBoundException | IllegalArgumentException e) {
                connectedToServer = false;
            }
        }
        if (!connectedToServer){
            throw new ConnectionClosedException("Error connecting to the server");
        }

        if (client.getClass() == RMIClient.class) {
            Registry registry = ((RMIClient) client).getRegistry();
            try {
                RemoteServer server = (RemoteServer) registry.lookup("server");
                ArrayList<String> gameNames = server.getGamesNames();

                showJoinOrCreate(gameNames);
            } catch (Exception e) {
                System.err.println("Error connecting to server");
                e.printStackTrace();
                throw new ConnectionClosedException("Connection closed");
            }
        }

        if(connectedToServer){
            new Thread(this::listenForDisconnection).start();
        }
    }

    public void listenForDisconnection() {
        boolean isDisconnecting = false;
        while(true) {
            client = getClient();
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
     * method to show any type of String
     *
     * @param s is the string you want to be displayed
     */
    @Override
    public void showMessage(String s) {
        Platform.runLater(()->{
            if(sceneName.equalsIgnoreCase("setup.fxml")){
                currentEventHandler.showPopup("", s);
            } else if(sceneName.equalsIgnoreCase("playerfield.fxml")){
                //TODO: Implement for when in playerField (should update the "chat")
            }
        });
    }

    @Override
    public void showJoinOrCreate(ArrayList<String> gameNames) {
        sceneName = "JoinGame.fxml";
        changeScene(sceneName);
        Platform.runLater(() -> {
            JoinGameController joinGameHandler = (JoinGameController) currentEventHandler;
            joinGameHandler.setGameNames(gameNames);
        });
    }

    public void setLoginParameters(){
        Platform.runLater(() -> {
            LoginController loginHandler = (LoginController) currentEventHandler;
            loginHandler.setParameters(isJoining, gameName);
        });
    }

    /**
     * method to show the "Login" fxml file
     */
    @Override
    public void insertNickname(boolean isJoin, String gameName) {
        sceneName = "Login.fxml";
        changeScene(sceneName);
        setLoginParameters();
    }

    /**
     * method to make the player choose its color
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
     * Directly send the number of players to the server (the player will have already chosen the number in the form)
     */
    @Override
    public void askForPlayersNumber() {
        //We already have the number of players and the name of the game, so we simulate a response from the client
        client.playersNumberResponse(numOfPlayersChosen, gameName);
    }

    /**
     * method to display the waiting for players message in loginPage
     */
    @Override
    public void ShowWaitingForPlayers() {
        Platform.runLater(() -> {
            LoginController loginController = (LoginController) currentEventHandler;
            loginController.showWaitingForPlayers();
        });
    }

    /**
     * method to show the private goal of a specific player
     *
     * @param player of which to display the goal
     * @param game   in which the player is partecipating
     */
    @Override
    public void ShowPrivateGoal(Player player, GameState game) {
        System.err.println("Not yet implemented, should only be called in player field and even then in GUI it shouldn't really be called");
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
     * displays the two private goals the client has to choose between
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
     * displays the player field of a specific player
     *
     * @param playerToSee  specifies which playerfield has to be displayed
     * @param playerAsking tells which player is asking to see it
     * @param game         we are referring to
     */
    @Override
    public void ShowPlayerField(Player playerToSee, Player playerAsking, GameState game) {
        sceneName = "PlayerField.fxml";
        changeScene(sceneName);
        //TODO: implement when playerToSee != playerAsking
    }

    /**
     * displays the two decks and the uncovered cards
     *
     * @param game we are referring to
     */
    @Override
    public void ShowDecks(GameState game) {

    }

    /**
     * displays the scoreboard
     */
    @Override
    public void ShowScoreBoard(ScoreBoard scoreBoard) {

    }

    /**
     * shows who has won the game
     *
     * @param game we are referring to
     */
    @Override
    public void ShowWinner(GameState game) {

    }

    /**
     * shows the end of game text
     */
    @Override
    public void ShowEndOfGame() {

    }

    /**
     * method to get the user's inputs
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

    }

    /**
     * playPhase setter
     *
     * @param playPhase tells whether it's the client's turn or not
     */
    @Override
    public void setPlayPhase(boolean playPhase) throws RemoteException {

    }

    /**
     * isMyTurn setter
     *
     * @param user is the user that is going to use this client
     */
    @Override
    public void setUser(Player user) throws RemoteException { this.user = user; }

    /**
     * game setter
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
    }

    /**
     * Disconnect the client
     */
    @Override
    public void disconnectClient() throws RemoteException {

    }

    /**
     * Client getter
     * @return this client
     */
    public Client getClient(){ return client; }

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
}
