package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.view.mainview.View;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.*;

import java.io.IOException;
import java.util.Scanner;

/**
 * CLI class to show everything using text interface
 * @author Lorenzo Meroi
 */
public class CLI implements View {
    private Client client;
    private Scanner scanner = new Scanner(System.in);
    private ViewGameCardFactory gameCardViewer = null;
    private ViewScoreBoardFactory scoreBoardViewer  = null;
    private ViewPlayerFieldFactory playerFieldViewer  = null;
    private ViewDeckFactory deckViewer  = null;
    private ViewGoalCardFactory goalCardViewer = null;

    /**
     * CLI constructor
     */
    public CLI () {
        this.deckViewer = new ViewDeckCLIFactory();
        this.gameCardViewer = new ViewGameCardCLIFactory();
        this.playerFieldViewer = new ViewPlayerFieldCLIFactory();
        this.scoreBoardViewer = new ViewScoreBoardCLIFactory();
        this.goalCardViewer = new ViewGoalCardCLIFactory();
    }

    @Override
    public void start() {
        boolean socketError = true;
        while (socketError) {
            try {
                this.client = connectToServer();
                this.client.init();
                socketError = false;
            } catch (IOException e) {}
        }
    }

    private boolean validateIP(String address) {
        String zeroTo255 = "([01]?\\d{1,2}|2[0-4]\\d|25[0-5])";
        String IP_REGEX = "^(" + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + ")$";
        return address.matches(IP_REGEX);
    }

    @Override
    public Client connectToServer() {
        final int DEFAULT_PORT = 2807;
        final String DEFAULT_ADDRESS = "127.0.0.1";
        final int MIN_PORT = 1024;
        final int MAX_PORT = 65535;
        int port = DEFAULT_PORT;
        String ip = DEFAULT_ADDRESS;
        boolean validInput = false;
        boolean firstTry = true;
        boolean notAnInt = false;
        boolean wrongPort = false;

        do {
            if (!firstTry) {
                System.out.println("ERROR: Invalid address!");
            } else {
                System.out.println("Enter the server address");
            }
            System.out.print("Insert 'd' for the default value (" + DEFAULT_ADDRESS + "): ");
            String address = scanner.nextLine();
            if (address.equalsIgnoreCase("d") || address.equalsIgnoreCase("localhost") || address.equals(DEFAULT_ADDRESS)) {
                validInput = true;
            } else if (validateIP(address)) {
                ip = address;
                validInput = true;
            } else {
                firstTry = false;
            }
        } while(!validInput);

        validInput = false;

        while (!validInput) {
            if (notAnInt) {
                notAnInt = false;
                System.out.println("Please insert only numbers");
            }
            if (wrongPort) {
                wrongPort = false;
                System.out.println("ERROR: MIN PORT = " + MIN_PORT + ", MAX PORT = " + MAX_PORT);
            }
            System.out.println("Select a valid port between [" + MIN_PORT + ", " + MAX_PORT + "]");
            System.out.print("Insert 'd' for the default value (" + DEFAULT_PORT + "): ");

            String input = scanner.nextLine();
            if(input.equalsIgnoreCase("d")){
                validInput = true;
            } else {
                try {
                    port = Integer.parseInt(input);
                    if(MIN_PORT <= port && port <= MAX_PORT){
                        validInput = true;
                    } else {
                        wrongPort = true;
                    }
                } catch (NumberFormatException e){
                    notAnInt = true;
                }
            }
        }
        return new Client(ip, port, this);
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
    public void ShowLogin() {

    }

    @Override
    public void askForPlayersNumber() {

    }

    /**
     * method to show to players when waiting for others to join
     */
    @Override
    public void ShowWaitingForPlayers() {
        System.out.println("Waiting for players...");
    }

    @Override
    public void ShowPrivateGoal(Player player) {

    }

    @Override
    public void ShowChoosePrivateGoal(GoalCard[] goalCards) {
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
