package it.polimi.ingsw.connection;

import java.util.Scanner;

/**
 * ServerMain class
 * @author Matteo Leonardo Luraghi
 */
public class ServerMain {
    private static final int MIN = 1024;
    private static final int MAX = 65535;
    private static final int DEFAULT = 2807;
    public static void main(String[] args) {
        int port = DEFAULT;
        String input;
        boolean valid = false;
        boolean notAnInt = false;
        boolean wrongPort = false;
        Scanner scanner = new Scanner(System.in);

        while(!valid) {
            if(notAnInt) {
                System.out.println("Please insert only numbers");
            }
            if(wrongPort) {
                System.out.println("Error: min port = "+ MIN + ", max port = "+MAX);
            }

            System.out.println("Please select a valid port between [" + MIN + ", " + MAX + "]");
            System.out.println("Insert 'd' for the default value (" + DEFAULT + ")");

            input = scanner.nextLine();

            if(input.equalsIgnoreCase("d")) {
                port = DEFAULT;
                valid = true;
                System.out.println("Port accepted successfully!");
            } else {
                try {
                    port = Integer.parseInt(input);
                    if (MIN <= port && port <= MAX) {
                        valid = true;
                    } else {
                        wrongPort = true;
                    }
                } catch (NumberFormatException e) {
                    notAnInt = true;
                }
            }
        }
        //valid inputs
        new Server(port).start();
    }
}
