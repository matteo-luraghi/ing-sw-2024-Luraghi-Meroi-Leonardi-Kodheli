package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.GameState;
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
    private final Scanner scanner = new Scanner(System.in);
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

    /**
     * method to initialize the CLI for a specific Client
     */
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

    /**
     * method to check wheter or not an IP address is valid
     * @param address is the String in which the IP is contained
     * @return a boolean telling whether it is valid or not
     */
    private boolean validateIP(String address) {
        String zeroTo255 = "([01]?\\d{1,2}|2[0-4]\\d|25[0-5])";
        String IP_REGEX = "^(" + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + "\\." + zeroTo255 + ")$";
        return address.matches(IP_REGEX);
    }

    /**
     * method to connect a client to the server
     * @return the client connected
     */
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
    private void ClearScreen () {
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
        ClearScreen();
        System.out.println(s);
    }

    /**
     * method to login a player into a game
     * @return the new player to insert into the game
     */
    @Override
    public Player ShowLogin() {
        return new Player("default", Color.RED);
    }

    /**
     * asks the client how many players there has to be in the game
     * @return the number of player that will be in the game
     */
    @Override
    public int askForPlayersNumber() {
        System.out.println("How many player will play the game?");
        return scanner.nextInt();
    }

    /**
     * method to show to players when waiting for others to join
     */
    @Override
    public void ShowWaitingForPlayers() {
        ClearScreen();
        System.out.println("Waiting for players...");
    }

    /**
     * method to show the private goal of a specific player
     * @param player of which to display the goal
     * @param game in which the player is partecipating
     */
    @Override
    public void ShowPrivateGoal(Player player, GameState game) {
        ClearScreen();
        this.goalCardViewer.SetCard(game.getGameTable().getPlayerZones().get(player).getPrivateGoal());
        this.goalCardViewer.Show();
    }

    /**
     * displays the two private goals the client has to choose between
     * @param goalCards is an array of two goal cards
     */
    @Override
    public void ShowChoosePrivateGoal(GoalCard[] goalCards) {
        ClearScreen();
        this.goalCardViewer.SetCard(goalCards[0]);
        this.goalCardViewer.Show();
        this.goalCardViewer.SetCard(goalCards[1]);
        this.goalCardViewer.Show();
    }

    /**
     * displays the player field of a specific player
     * @param playerToSee specifies which playerfield has to be displayed
     * @param playerAsking tells which player is asking to see it
     * @param game we are referring to
     */
    @Override
    public void ShowPlayerField(Player playerToSee, Player playerAsking, GameState game) {
        ClearScreen();
        this.playerFieldViewer.setPlayerField(game.getGameTable().getPlayerZones().get(playerToSee), playerToSee);
        if (playerAsking.equals(playerToSee))
            this.playerFieldViewer.showComplete();
        else
            this.playerFieldViewer.showToOthers();
    }

    /**
     * displays the two decks and the uncovered cards
     * @param game we are referring to
     */
    @Override
    public void ShowDecks(GameState game) {
        ClearScreen();
        this.deckViewer.setDeck(game.getGameTable().getGoldDeck());
        this.deckViewer.show();
        this.deckViewer.setDeck(game.getGameTable().getResourceDeck());
        this.deckViewer.show();
    }

    /**
     * displays the scoreboard
     */
    @Override
    public void ShowScoreBoard() {
        ClearScreen();
        this.scoreBoardViewer.show();
    }

    /**
     * shows who has won the game
     * @param game we are referring to
     */
    @Override
    public void ShowWinner(GameState game) {
        ClearScreen();
        System.out.println(game.getWinner().toString() + "has won!");
    }

    /**
     * shows the end of game text
     */
    @Override
    public void ShowEndOfGame() {
        System.out.println("The game has ended...");
    }
}
