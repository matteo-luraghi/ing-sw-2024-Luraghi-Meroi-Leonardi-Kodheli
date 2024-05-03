package it.polimi.ingsw.connection;

import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;

/**
 * ClientMain class
 * used to launch the game for the client
 * * @author Matteo Leonardo Luraghi
 */
public class ClientMain {

    /**
     * Main, launches the game in CLI or GUI
     * @param args command line arguments
     */
    public static void main(String[] args) {

            new CLI().start();

    }
}
