package it.polimi.ingsw.connection;

import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

/**
 * ClientMain class
 * used to launch the game client side
 * @author Matteo Leonardo Luraghi
 */
public class ClientMain {

    /**
     * Main, launches the game in CLI or GUI
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if(args.length == 0) {
            new GUI().start();
        } else if (args.length > 1) {
            System.out.println("Too many arguments, insert -cli to start in CLI mode (default GUI mode)");
        } else {
            if(args[0].equals("-cli")) {
                new CLI().start();
            } else {
                System.out.println("Command not found, insert -cli to start in CLI mode (default GUI mode)");
            }
        }
    }
}
