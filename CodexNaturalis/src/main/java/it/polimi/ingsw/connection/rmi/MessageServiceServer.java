package it.polimi.ingsw.connection.rmi;

import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class MessageServiceServer implements MessageService {
    public MessageServiceServer() {
        super();
    }

    @Override
    public String sendMessage(String clientMessage) throws RemoteException {
        return "Client Message".equals(clientMessage) ? "Server Message" : null;
    }

    public static void main(String[] args) {
        try {
            MessageService server = new MessageServiceServer();
            MessageService stub = (MessageService) UnicastRemoteObject.exportObject((MessageService) server, 0);
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MessageService", stub);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
