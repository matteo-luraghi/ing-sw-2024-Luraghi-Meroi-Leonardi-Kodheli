package it.polimi.ingsw.connection;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteServer extends Remote {
    void addToGame(ConnectionHandler connectionHandler) throws RemoteException;
    void addToGame(ConnectionHandler connectionHandler, int numberOfPlayers) throws RemoteException;
}
