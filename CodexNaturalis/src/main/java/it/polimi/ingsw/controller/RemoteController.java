package it.polimi.ingsw.controller;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.*;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

/**
 * RemoteController interface
 * used to expose the controller's methods
 * @author Matteo Leonardo Luraghi
 */
 public interface RemoteController extends Remote {

    /**
     * gameState getter
     *
     * @return the current gameState throws RemoteException;
     */
     GameState getGame() throws RemoteException;

    /**
     * gameState setter
     *
     * @param game the game
     */
     void setGame(GameState game) throws RemoteException;

    /**
     * Client handlers getter
     *
     * @return list of handlers
     */
     ArrayList<ConnectionHandler> getHandlers() throws RemoteException;

    /**
     * Add a client handler to the list
     *
     * @param handler client handler to be added
     */
     void addHandler(ConnectionHandler handler) throws RemoteException;

    /**
     * Client Handler getter by nickname
     *
     * @param nickname the nickname of a Player
     * @return the corresponding SocketConnectionHandler
     */
     ConnectionHandler getHandlerByNickname(String nickname) throws RemoteException;

    /**
     * Send a message to all the clients
     *
     * @param msg the message to be sent
     */
     void broadcastMessage(Serializable msg) throws RemoteException;

    /**
     * isGamEnded setter
     *
     * @param gameEnded value
     */
     void setGameEnded(boolean gameEnded) throws RemoteException;

    /**
     * isGameEnded getter
     *
     * @return value
     */
     boolean isGameEnded() throws RemoteException;

    /**
     * isGameStarted setter
     *
     * @param gameStarted value
     */
     void setGameStarted(boolean gameStarted) throws RemoteException;

    /**
     * isGameStarted getter
     *
     * @return value
     */
     boolean isGameStarted() throws RemoteException;

     void chooseColorState(ConnectionHandler connectionHandler) throws RemoteException;

     void setColor(ConnectionHandler connectionHandler, Color color) throws RemoteException;

     void checkGame() throws RemoteException;

    /**
     * Start the game, creating the necessary resources
     */
     void start() throws RemoteException;

    /**
     * Sets the chosen startingCard as the startingCard for a certain player
     * @param card the starting card chosen
     * @param isFront the way it needs to face
     * @param connectionHandler the player that is sending the request
     */
     void setStartingCard(StartingCard card, boolean isFront, ConnectionHandler connectionHandler) throws RemoteException;
    /**
     * Sets the chosen goal card as a private goal for a certain player
     * @param goal the goal card chosen
     * @param connectionHandler the player that is sending the request
     */
     void setPrivateGoalCard(GoalCard goal, ConnectionHandler connectionHandler) throws RemoteException;

    /**
     * Send a YourTurn message to the player that needs to play
     */
     void yourTurnState() throws RemoteException;

    /**
     * Send a PlayCardRequest message to the player that needs to choose which card to play
     */
     void playCardState() throws RemoteException;

    /**
     * Make the player play a card if it can be played, otherwise make the player chose a new pair of card/where
     * @param card The card that needs to be played
     * @param where Where the card needs to be played
     * @param connectionHandler The player who is playing the card
     */
     void playCard(ConnectionHandler connectionHandler, ResourceCard card, Coordinates where, boolean isFront) throws RemoteException;

    /**
     * Send a DrawCardRequest message to the player that needs to choose which card to draw
     */
     void drawCardState() throws RemoteException;

    /**
     * Make the player draw a card if it can be drawn, otherwise make the player draw from somewhere else
     * @param which the card he wants to draw
     * @param isGold which deck he wants to draw from
     * @param connectionHandler The player who is playing the card
     */
     void drawCard(ConnectionHandler connectionHandler, int which, boolean isGold) throws RemoteException;

    /**
     * Passes the turn to the next player, while checking if it's the penultimate or the last turn
     */
     void changeTurnState() throws RemoteException;

    /**
     * Count the points scored by the common and private goal cards
     */
     void countGoals() throws RemoteException;

    /**
     * Show to all players the winner of the match
     */
     void showLeaderBoard() throws RemoteException;

}
