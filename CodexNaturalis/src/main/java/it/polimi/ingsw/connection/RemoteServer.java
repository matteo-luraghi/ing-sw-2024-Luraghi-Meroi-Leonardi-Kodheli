package it.polimi.ingsw.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RemoteServer interface
 * used to expose useful server methods
 * @author Matteo Leonardo Luraghi
 */
public interface RemoteServer extends Remote {

    /**
     * Add a client to a game via its connectionHandler
     * @param connectionHandler the connectionHandler to save in a game
     */
    void addToGame(ConnectionHandler connectionHandler) throws RemoteException;

    /**
     * Overloading of addToGame with number of players
     * @param connectionHandler the client handler
     * @param numberOfPlayers the number of players for the new game
     */
    void addToGame(ConnectionHandler connectionHandler, int numberOfPlayers) throws RemoteException;
}
