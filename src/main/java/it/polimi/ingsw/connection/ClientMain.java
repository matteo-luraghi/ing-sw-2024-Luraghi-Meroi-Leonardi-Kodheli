package it.polimi.ingsw.connection;

import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

import java.rmi.ConnectException;
import java.rmi.RemoteException;

/**
 * ClientMain class
 * used to launch the game for the client
 * @author Matteo Leonardo Luraghi
 */
public class ClientMain {

    /**
     * Main, launches the game in CLI or GUI
     * @param args command line arguments
     */
    public static void main(String[] args) {

        boolean started = false;

        do {
            try {
                //if no args are passed in, gui will start
                if(args.length == 0 || args[0].equalsIgnoreCase("-gui")){
                    started = true;
                    new GUI().start();
                } else if(args[0].equalsIgnoreCase("-cli")) {
                    started = true;
                    new CLI().start();
                } else {
                    System.err.println("Invalid argument passed!");
                    System.out.println("Insert a valid argument:\n-gui\n-cli");
                    System.exit(0);
                }
            } catch (ConnectionClosedException e) {
                System.err.println(e.getMessage());
                System.exit(0);
            }
        } while (!started);

    }
}
