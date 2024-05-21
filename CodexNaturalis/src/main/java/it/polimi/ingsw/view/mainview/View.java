package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.connection.ConnectionClosedException;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.ScoreBoard;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * View interface, will be implemented for GUI and CLI
 * @author Lorenzo Meroi
 */
public interface View extends Remote {
    Client client = null;
    Controller controller = null;
    ViewGameCardFactory gameCardViewer = null;
    ViewScoreBoardFactory scoreBoardViewer  = null;
    ViewPlayerFieldFactory playerFieldViewer  = null;
    ViewDeckFactory deckViewer  = null;
    ViewGoalCardFactory goalCardViewer = null;

    /**
     * method to initialize the CLI for a specific Client
     */
    void start() throws RemoteException, ConnectionClosedException;

    /**
     * method to show any type of String
     * @param s is the string you want to be displayed
     */
    void showMessage(String s) throws RemoteException;

    void showJoinOrCreate(ArrayList<String> gameNames) throws RemoteException;

    /**
     * method to make the player insert its nickname
     */
    void insertNickname(boolean isJoin, String gameName) throws RemoteException;

    /**
     * method to make the player choose its color
     * @param colors available
     */
    void insertColor(ArrayList<Color> colors) throws RemoteException;

    /**
     * asks the client how many players there has to be in the game
     */
    void askForPlayersNumber() throws RemoteException;

    /**
     * method to display the waiting for players page
     */
    void ShowWaitingForPlayers() throws RemoteException;

    /**
     * method to show the private goal of a specific player
     * @param player of which to display the goal
     * @param game in which the player is partecipating
     */
    void ShowPrivateGoal(Player player, GameState game) throws RemoteException;

    void ChooseStartingCardSide(StartingCard card) throws RemoteException;

    /**
     * displays the two private goals the client has to choose between
     * @param goalCards is an array of two goal cards
     */
    void ShowChoosePrivateGoal(GoalCard[] goalCards) throws RemoteException;

    /**
     * displays the player field of a specific player
     * @param playerToSee specifies which playerfield has to be displayed
     * @param playerAsking tells which player is asking to see it
     * @param game we are referring to
     */
    void ShowPlayerField(Player playerToSee, Player playerAsking, GameState game) throws RemoteException;

    /**
     * displays the two decks and the uncovered cards
     * @param game we are referring to
     */
    void ShowDecks(GameState game) throws RemoteException;

    /**
     * displays the scoreboard
     */
    void ShowScoreBoard(ScoreBoard scoreBoard) throws RemoteException;

    /**
     * shows who has won the game
     * @param game we are referring to
     */
    void ShowWinner(GameState game) throws RemoteException;

    /**
     * shows the end of game text
     */
    void ShowEndOfGame() throws RemoteException;

    /**
     * method to get the user's inputs
     */
    void getCommands() throws RemoteException;

    /**
     * isMyTurn setter
     * @param isMyTurn tells whether it's the client's turn or not
     */
    void setMyTurn(boolean isMyTurn) throws RemoteException;

    /**
     * playPhase setter
     * @param playPhase tells whether it's the client's turn or not
     */
    void setPlayPhase(boolean playPhase) throws RemoteException;

    /**
     * isMyTurn setter
     * @param user is the user that is going to use this client
     */
    void setUser(Player user) throws RemoteException;

    /**
     * game setter
     * @param game the game we need to set!
     */
    void setGame(GameState game) throws RemoteException;

    /**
     * Update the list of names of available games
     * @param gameNames the names
     */
    void setGameNames(ArrayList<String> gameNames) throws RemoteException;

    /**
     * Disconnect the client
     */
    void disconnectClient() throws RemoteException;
}
