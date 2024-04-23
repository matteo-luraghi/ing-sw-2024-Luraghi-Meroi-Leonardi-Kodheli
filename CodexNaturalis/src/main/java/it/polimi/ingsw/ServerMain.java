package it.polimi.ingsw;

import it.polimi.ingsw.controller.MegaController;
import it.polimi.ingsw.model.gamelogic.GameState;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Deck class
 * @author Matteo Leonardo Luraghi
 */
public class ServerMain implements Remote {
    public static void main(String[] args) {
        //TODO: start the server and idle for games
    }
    public void startGame() throws RemoteException {
        LocateRegistry.createRegistry(1099);
        // esportazione dell'oggetto, tutte le modifiche sulla copia devono essere trasferite all'oggetto originale
        MegaController controller = new MegaController(new GameState());
        Remote stub = (Remote) UnicastRemoteObject.exportObject(controller, 0);
        try {
            Naming.rebind("//localhost/Server", stub);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
