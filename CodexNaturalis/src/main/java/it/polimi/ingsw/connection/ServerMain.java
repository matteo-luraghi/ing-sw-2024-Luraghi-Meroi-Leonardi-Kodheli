package it.polimi.ingsw.connection;

import it.polimi.ingsw.connection.rmi.IPNotFoundException;

import java.io.IOException;
import java.util.Scanner;

/**
 * ServerMain class
 * @author Matteo Leonardo Luraghi
 */
public class ServerMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean started = false;

        do {
            System.out.println("Insert the port number:");
            String portStr = scanner.nextLine();
            try {
                int port = Integer.parseInt(portStr);
                Server server = new Server(port);
                server.start();
                started = true;
            } catch(NumberFormatException e) {
                System.err.println("Insert a valid number!");
            } catch (IOException e) {
                System.err.println("Error starting the server, try again");
            } catch (IPNotFoundException e) {
                System.err.println("Unable to find server ip");
            }

        } while (!started);

    }
}
