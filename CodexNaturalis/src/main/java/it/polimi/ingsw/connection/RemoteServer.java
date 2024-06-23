package it.polimi.ingsw.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * RemoteServer interface
 * used to expose useful server methods
 * @author Matteo Leonardo Luraghi
 */
public interface RemoteServer extends Remote {

    /**
     * Add a client to a game via its connectionHandler
     * @param connectionHandler the connectionHandler to save in a game
     * @throws RemoteException if errors occur during RMI connection
     */
    void joinGame(ConnectionHandler connectionHandler, String gameName) throws RemoteException;

    /**
     * Create a new game
     * @param connectionHandler the client handler of the game's creator
     * @param numberOfPlayers the number of players for the new game
     * @param gameName the name of the new game
     * @throws RemoteException if errors occur during RMI connection
     */
    void createGame(ConnectionHandler connectionHandler, int numberOfPlayers, String gameName) throws RemoteException;

    /**
     * Check if the nickname is unique in all the games
     * @param nickname the nickname
     * @return true if no other player with the same nickname
     * @throws RemoteException if errors occur during RMI connection
     */
    boolean checkUniqueNickname(String nickname) throws RemoteException;

    /**
     * Remove and disconnect a client
     * @param connectionHandler the connectionHandler relative to the client
     * @throws RemoteException if errors occur during RMI connection
     */
    void removeClient(ConnectionHandler connectionHandler) throws RemoteException;

    /**
     * Get all the games' names
     * @return the names
     * @throws RemoteException if errors occur during RMI connection
     */
     ArrayList<String> getGamesNames() throws RemoteException;
}
