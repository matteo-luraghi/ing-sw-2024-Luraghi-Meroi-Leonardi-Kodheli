package it.polimi.ingsw.connection.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageService extends Remote {
    String sendMessage(String clientMessage) throws RemoteException;
}
