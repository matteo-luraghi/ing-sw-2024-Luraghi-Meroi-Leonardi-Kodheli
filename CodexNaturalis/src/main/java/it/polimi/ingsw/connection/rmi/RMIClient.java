package it.polimi.ingsw.connection.rmi;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {

    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry();
            MessageService server = (MessageService) registry.lookup("MessageService");
            String responseMessage = server.sendMessage("Client Message");
            System.out.println(responseMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
