package it.polimi.ingsw.connection;

import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

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

        try {
            //if no args are passed in, cli will start TODO: Change it to gui when gui works
            if(args.length == 0 || args[0].equalsIgnoreCase("-cli")){
                new CLI().start();
            } else if(args[0].equalsIgnoreCase("-gui")) {
                new GUI().start();
            } else {
                System.out.println("Insert a valid argument");
            }

        } catch (ConnectionClosedException e) {
            System.out.println(e.getMessage());
        }

    }
}
