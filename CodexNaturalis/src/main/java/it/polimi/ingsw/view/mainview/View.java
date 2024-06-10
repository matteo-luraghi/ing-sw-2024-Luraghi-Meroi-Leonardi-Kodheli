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
import it.polimi.ingsw.model.gamelogic.gamechat.GameChat;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * View interface, will be implemented for GUI and CLI
 * @author Lorenzo Meroi
 */
public interface View extends Remote {
    /**
     * method to initialize the CLI for a specific Client
     * @throws RemoteException to handle exceptions that may occur when using RMI
     * @throws ConnectionClosedException if unable to connect to the server
     */
    void start() throws RemoteException, ConnectionClosedException;

    /**
     * method to show any type of String
     * @param s is the string you want to be displayed
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void showMessage(String s) throws RemoteException;

    /**
     * method to join or create a new game
     * @param gameNames is the list of all the joinable games
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void showJoinOrCreate(ArrayList<String> gameNames) throws RemoteException;

    /**
     * method to make the player insert its nickname
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void insertNickname(boolean isJoin, String gameName) throws RemoteException;

    /**
     * method to make the player choose its color
     * @param colors available
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void insertColor(ArrayList<Color> colors) throws RemoteException;

    /**
     * asks the client how many players there has to be in the game
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void askForPlayersNumber() throws RemoteException;

    /**
     * method to display the waiting for players page
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void ShowWaitingForPlayers() throws RemoteException;

    /**
     * method to show the private goal of a specific player
     * @param player of which to display the goal
     * @param game in which the player is partecipating
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void ShowPrivateGoal(Player player, GameState game) throws RemoteException;

    /**
     * method to make the user choose on which side to play the starting card
     * @param card is the starting card we are referring to
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void ChooseStartingCardSide(StartingCard card) throws RemoteException;

    /**
     * displays the two private goals the client has to choose between
     * @param goalCards is an array of two goal cards
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void ShowChoosePrivateGoal(GoalCard[] goalCards) throws RemoteException;

    /**
     * displays the player field of a specific player
     * @param playerToSee specifies which playerfield has to be displayed
     * @param playerAsking tells which player is asking to see it
     * @param game we are referring to
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void ShowPlayerField(Player playerToSee, Player playerAsking, GameState game) throws RemoteException;

    /**
     * displays the two decks and the uncovered cards
     * @param game we are referring to
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void ShowDecks(GameState game) throws RemoteException;

    /**
     * displays the scoreboard
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void ShowScoreBoard(ScoreBoard scoreBoard) throws RemoteException;

    /**
     * shows who has won the game
     * @param game we are referring to
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void ShowWinner(GameState game) throws RemoteException;

    /**
     * shows the end of game text
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void ShowEndOfGame() throws RemoteException;

    /**
     * sets the game chat
     * @param gameChat the chat
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void setGameChat (GameChat gameChat) throws RemoteException;

    /**
     * method to get the user's inputs
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void getCommands() throws RemoteException;

    /**
     * isMyTurn setter
     * @param isMyTurn tells whether it's the client's turn or not
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void setMyTurn(boolean isMyTurn) throws RemoteException;

    /**
     * playPhase setter
     * @param playPhase tells whether it's the client's turn or not
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void setPlayPhase(boolean playPhase) throws RemoteException;

    /**
     * isMyTurn setter
     * @param user is the user that is going to use this client
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void setUser(Player user) throws RemoteException;

    /**
     * game setter
     * @param game the game we need to set
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void setGame(GameState game) throws RemoteException;

    /**
     * Update the list of names of available games
     * @param gameNames the names
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void setGameNames(ArrayList<String> gameNames) throws RemoteException;

    /**
     * Disconnect the client
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void disconnectClient() throws RemoteException;
}
