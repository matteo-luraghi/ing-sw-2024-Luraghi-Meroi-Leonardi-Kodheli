package it.polimi.ingsw.connection;

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
                started = true;
            } catch(NumberFormatException e) {
                System.out.println("Insert a valid number!");
            } catch (IOException e) {
                System.out.println("Error starting the server, try again");
            }

        } while (!started);

    }
}
