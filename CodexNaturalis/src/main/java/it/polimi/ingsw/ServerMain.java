package it.polimi.ingsw;


import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class ServerMain implements Remote {
    public void start() throws RemoteException {
        LocateRegistry.createRegistry(1099);
        // esportazione dell'oggetto, tutte le modifiche sulla copia devono essere trasferite all'oggetto originale
        Remote stub = (Remote) UnicastRemoteObject.exportObject(obj, 0);
        try {
            Naming.rebind("//localhost/Server", stub);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
