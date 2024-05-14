package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.ScoreBoard;
import it.polimi.ingsw.psp17.ConnectToServerController;
import it.polimi.ingsw.psp17.GUIApplication;
import it.polimi.ingsw.view.mainview.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * GUI class to show the game using graphic interface
 */
public class GUI extends Application implements View{
    private Client client = null;
    private Controller controller = null;
    private ViewGameCardFactory gameCardViewer;
    private ViewScoreBoardFactory scoreBoardViewer;
    private ViewPlayerFieldFactory playerFieldViewer;
    private ViewDeckFactory deckViewer;
    private ViewGoalCardFactory goalCardViewer;
    private String sceneName;
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
    }

    /**
     * Method that starts the GUI for a player
     */
    public void start(){
        GUIApplication.loadApplication(sceneName);
    }

    /**
     * Method to open a specific stage for a player
     * @param stage the stage that needs to be loaded
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ConnectToServerController.class.getResource(sceneName));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Method that tries to connect to a specified server (boolean return?)
     */
    public void connectToServer(){

    }

    /**
     * method to show any type of String
     *
     * @param s is the string you want to be displayed
     */
    @Override
    public void showMessage(String s) {

    }

    /**
     * method to make the player insert its nickname
     */
    @Override
    public void insertNickname() {
    }

    /**
     * method to make the player choose its color
     * @param colors available
     */
    @Override
    public void insertColor(ArrayList<Color> colors) {}

    /**
     * asks the client how many players there has to be in the game
     */
    @Override
    public void askForPlayersNumber() {
    }

    /**
     * method to display the waiting for players page
     */
    @Override
    public void ShowWaitingForPlayers() {

    }

    /**
     * method to show the private goal of a specific player
     *
     * @param player of which to display the goal
     * @param game   in which the player is partecipating
     */
    @Override
    public void ShowPrivateGoal(Player player, GameState game) {

    }

    @Override
    public void ChooseStartingCardSide(StartingCard card) {

    }

    /**
     * displays the two private goals the client has to choose between
     *
     * @param goalCards is an array of two goal cards
     */
    @Override
    public void ShowChoosePrivateGoal(GoalCard[] goalCards) {

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
    public void setUser(Player user) throws RemoteException {

    }

    /**
     * game setter
     *
     * @param game the game we need to set!
     */
    @Override
    public void setGame(GameState game) throws RemoteException {

    }

    /**
     * Disconnect the client
     */
    @Override
    public void disconnectClient() throws RemoteException {

    }
}
