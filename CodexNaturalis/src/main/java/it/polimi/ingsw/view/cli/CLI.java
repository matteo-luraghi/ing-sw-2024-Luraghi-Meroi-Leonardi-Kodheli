package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.connection.message.serverMessage.WaitingForPlayers;
import it.polimi.ingsw.controller.MegaController;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.*;

import java.io.IOException;

/**
 * CLI class to show everything using text interface
 * @author Lorenzo Meroi
 */
public class CLI implements MegaView {

    MegaController controller = null;
    ViewGameCardFactory gameCardViewer = null;
    ViewScoreBoardFactory scoreBoardViewer  = null;
    ViewPlayerFieldFactory playerFieldViewer  = null;
    ViewDeckFactory deckViewer  = null;

    /**
     * CLI constructor
     * @param controller reference to the controller of the game
     * @param gameCardViewer manages the printing of the game cards
     * @param scoreBoardViewer manages the printing of the scoreboard
     * @param playerFieldViewer manages the printing of the players' fields
     * @param deckViewer manages the printing of the decks
     */
    public CLI (MegaController controller, ViewGameCardCLIFactory gameCardViewer, ViewScoreBoardCLIFactory scoreBoardViewer, ViewPlayerFieldCLIFactory playerFieldViewer, ViewDeckCLIFactory deckViewer) {
        this.controller = controller;
        this.deckViewer = deckViewer;
        this.gameCardViewer = gameCardViewer;
        this.playerFieldViewer = playerFieldViewer;
        this.scoreBoardViewer = scoreBoardViewer;
    }

    /**
     * method to clear the console if the program is being run in the windows console
     */
    public void ClearScreen () {
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }

    /**
     * method to print out any String
     * @param s the String you want to be printed
     */
    @Override
    public void showMessage(String s) {
        System.out.println(s);
    }

    @Override
    public void ShowWaitingForPlayers() {
        System.out.println("Waiting for players...");
    }

    @Override
    public void ShowPrivateGoals(Player player) {
        this.controller.getGame().getGameTable().getPlayerZones().get(player).
    }

    @Override
    public void ShowPlayerField(Player player) {

    }

    @Override
    public void ShowDecks() {

    }

    @Override
    public void ShowScoreBoard() {

    }

    @Override
    public void ShowWinner() {

    }

    @Override
    public void ShowEndOfGame() {

    }
}
