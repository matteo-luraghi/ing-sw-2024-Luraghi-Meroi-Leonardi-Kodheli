package it.polimi.ingsw.controller;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.*;
import it.polimi.ingsw.model.gamelogic.gamechat.Message;

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
     * @return the current gameState throws RemoteException;
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     GameState getGame() throws RemoteException;

    /**
     * gameState setter
     * @param game the game
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void setGame(GameState game) throws RemoteException;

    /**
     * Client handlers getter
     * @return list of handlers
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     ArrayList<ConnectionHandler> getHandlers() throws RemoteException;

    /**
     * Add a client handler to the list
     * @param handler client handler to be added
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void addHandler(ConnectionHandler handler) throws RemoteException;

    /**
     * Client Handler getter by nickname
     * @param nickname the nickname of a Player
     * @return the corresponding SocketConnectionHandler
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     ConnectionHandler getHandlerByNickname(String nickname) throws RemoteException;

    /**
     * Send a message to all the clients
     * @param msg      the message to be sent
     * @param handlers all the handlers to send the message to
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void broadcastMessage(Serializable msg, ArrayList<ConnectionHandler> handlers) throws RemoteException;

    /**
     * isGamEnded setter
     * @param gameEnded value
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void setGameEnded(boolean gameEnded) throws RemoteException;

    /**
     * isGameEnded getter
     * @return value
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     boolean isGameEnded() throws RemoteException;

    /**
     * isGameStarted setter
     * @param gameStarted value
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void setGameStarted(boolean gameStarted) throws RemoteException;

    /**
     * isGameStarted getter
     * @return value
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     boolean isGameStarted() throws RemoteException;

    /**
     * sets the state as choose color
     * @param connectionHandler the current connection handler
     * @throws RemoteException to handle exceptions that may occur using RMI
     */
     void chooseColorState(ConnectionHandler connectionHandler) throws RemoteException;

    /**
     * sets the player's color
     * @param connectionHandler the player's connection handler
     * @param color the chosen color
     * @throws RemoteException to handle exceptions that may occur using RMI
     */
     void setColor(ConnectionHandler connectionHandler, Color color) throws RemoteException;

     void checkGame() throws RemoteException;

    /**
     * Start the game, creating the necessary resources
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void start() throws RemoteException;

    /**
     * Sets the chosen startingCard as the startingCard for a certain player
     * @param card the starting card chosen
     * @param isFront the way it needs to face
     * @param connectionHandler the player that is sending the request
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void setStartingCard(StartingCard card, boolean isFront, ConnectionHandler connectionHandler) throws RemoteException;
    /**
     * Sets the chosen goal card as a private goal for a certain player
     * @param goal the goal card chosen
     * @param connectionHandler the player that is sending the request
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void setPrivateGoalCard(GoalCard goal, ConnectionHandler connectionHandler) throws RemoteException;

    /**
     * Send a YourTurn message to the player that needs to play
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void yourTurnState() throws RemoteException;

    /**
     * Send a PlayCardRequest message to the player that needs to choose which card to play
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void playCardState() throws RemoteException;

    /**
     * Make the player play a card if it can be played, otherwise make the player chose a new pair of card/where
     * @param card The card that needs to be played
     * @param where Where the card needs to be played
     * @param connectionHandler The player who is playing the card
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void playCard(ConnectionHandler connectionHandler, ResourceCard card, Coordinates where, boolean isFront) throws RemoteException;

    /**
     * Send a DrawCardRequest message to the player that needs to choose which card to draw
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void drawCardState() throws RemoteException;

    /**
     * Make the player draw a card if it can be drawn, otherwise make the player draw from somewhere else
     * @param which the card he wants to draw
     * @param isGold which deck he wants to draw from
     * @param connectionHandler The player who is playing the card
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void drawCard(ConnectionHandler connectionHandler, int which, boolean isGold) throws RemoteException;

    /**
     * Passes the turn to the next player, while checking if it's the penultimate or the last turn
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void changeTurnState() throws RemoteException;

    /**
     * Count the points scored by the common and private goal cards
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void countGoals() throws RemoteException;

    /**
     * Show to all players the winner of the match
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
     void showLeaderBoard() throws RemoteException;

    /**
     * Save the message in the chat
     * @param message the message
     * @throws RemoteException to handle exceptions that may occur when using RMI
     */
    void addMessageToChat(Message message) throws RemoteException;
}
