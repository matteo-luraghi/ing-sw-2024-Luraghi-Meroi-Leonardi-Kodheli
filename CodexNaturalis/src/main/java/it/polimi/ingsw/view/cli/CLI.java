package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.connection.ConnectionClosedException;
import it.polimi.ingsw.connection.RemoteServer;
import it.polimi.ingsw.connection.rmi.IPNotFoundException;
import it.polimi.ingsw.connection.rmi.RMIClient;
import it.polimi.ingsw.connection.socket.SocketClient;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.gamelogic.*;
import it.polimi.ingsw.model.gamelogic.gamechat.GameChat;
import it.polimi.ingsw.model.gamelogic.gamechat.Message;
import it.polimi.ingsw.view.mainview.View;

import it.polimi.ingsw.view.mainview.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.util.ArrayList;
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
    private boolean isMyTurn = false;
    private GameState game = null;
    private Player user = null;
    private boolean playPhase = false;
    private boolean connected = true;
    private String gameName;
    private ArrayList<String> gameList;

    private GameChat gameChat;

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
     * Starts the correct type of client and handles connection exception
     * then listens for disconnections
     * @throws ConnectionClosedException if unable to connect to the server
     */
    @Override
    public void start() throws ConnectionClosedException{

        // initialize the chat
        this.gameChat = new GameChat();

        boolean connected = false;
        String ip = null;
        int port = 0;
        Client client = null;
        String nickname = null;

        do {
            //make the client connect to the server through an IP and port given by the user
            System.out.println("Which connection type would you like to use? (rmi or socket)");
            String connection = scanner.nextLine();
            if (connection.equalsIgnoreCase("socket")) {
                //socket connection
                System.out.println("Insert a valid ip address:");
                ip = scanner.nextLine();
                System.out.println("Insert a valid port to connect:");
                String portStr = scanner.nextLine();
                //try catch to see if the port inserted is valid and to try to connect to the given server
                try {
                    port = Integer.parseInt(portStr);
                    try {
                        client = new SocketClient(ip, port, this);
                        connected = true;
                    } catch (IOException | IllegalArgumentException e) {
                        System.out.println("Error connecting to the server, try again");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Insert a valid port number!");
                }
            } else if(connection.equalsIgnoreCase("rmi")) {
                //RMI connection
                System.out.println("Insert a valid ip address:");
                ip = scanner.nextLine();
                System.out.println("Insert a valid port to connect:");
                String portStr = scanner.nextLine();
                //try catch to see if the port inserted is valid and to try to connect to the given server
                try {
                    port = Integer.parseInt(portStr);
                    try {
                        client = new RMIClient(ip, port, this);
                        connected = true;
                    } catch (RemoteException | NotBoundException | IllegalArgumentException | IPNotFoundException e) {
                        System.out.println("Error connecting to the server, try again");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Insert a valid port number!");
                }

            } else {
                System.out.println("Invalid option");
            }
        } while(!connected);
        WelcomeMessage();
        this.client = client;

        if (client.getClass() == RMIClient.class) {
            //insert the client into the RMI registry
            Registry registry = ((RMIClient) client).getRegistry();
            try {
                RemoteServer server = (RemoteServer) registry.lookup("server");
                ArrayList<String> gameNames = server.getGamesNames();

                showJoinOrCreate(gameNames);
            } catch (Exception e) {
                System.err.println("Error connecting to server");
                e.printStackTrace();
                throw new ConnectionClosedException("Connection closed");
            }
        }

        //while statement to disconnect the client in case of loss of connection
        while(true) {
            if(!client.getConnected() || !this.connected) {
                System.out.println("Disconnected");
                throw new ConnectionClosedException("Connection closed");
            }
        }
    }

    /**
     * Disconnect the client
     */
    @Override
    public void disconnectClient() {
        this.connected = false;
    }

    /**
     * method to clear the console if the program is being run in the windows console
     */
    private void ClearScreen() {
        System.out.print("\033c");
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
    public void showJoinOrCreate(ArrayList<String> gameNames) {
        gameList = gameNames;
        boolean isJoin = false;

        boolean correctInput = false;

        //do-while statement to check the user's input
        do {
            System.out.println("Do you want to join an existing game or create a new one?\nJoin|Create");
            String gameChoice = this.scanner.nextLine();

            if (!gameChoice.equalsIgnoreCase("join") && !gameChoice.equalsIgnoreCase("create")) {
                System.out.println(AnsiColors.ANSI_RED + "Invalid input, try again" + AnsiColors.ANSI_RESET);
            } else {
                correctInput = true;
                if (gameChoice.equalsIgnoreCase("join")) {
                    isJoin = true;
                }
            }
        } while (!correctInput);

        if (isJoin) {
            //the user's choice is to join a game
            while (gameList.isEmpty() && isJoin) {
                //there are no available games at the moment
                System.out.println("There are no available games, create a new one or wait for some game to be started.\nCreate|Refresh");
                String createOrRefresh = scanner.nextLine();

                if (!createOrRefresh.equalsIgnoreCase("create") && !createOrRefresh.equalsIgnoreCase("refresh")) {
                    System.out.println(AnsiColors.ANSI_RED + "Invalid input, try again" + AnsiColors.ANSI_RESET);
                } else {
                    if (createOrRefresh.equalsIgnoreCase("refresh")) {
                        client.refreshGamesNames();
                    } else {
                        //the user chooses to create a game instead
                        isJoin = false;
                    }
                }
            }
            if (isJoin) {
                //there is at least one available game
                System.out.println("Here there are all the available games:");

                for (String name : gameList) {
                    System.out.println("- " + name);
                }
                System.out.println();

                correctInput = false;
                //do-while statement to check the user's input
                do {
                    System.out.println("which one do you want to join?\nType 'refresh' to refresh the game's list.");
                    gameName = scanner.nextLine();

                    if (gameName.equalsIgnoreCase("refresh")) {
                        client.refreshGamesNames();

                        if (gameList.isEmpty()) {
                            System.out.println("There are no available games, type 'refresh' to refresh the game's list.");
                        } else {
                            for (String name : gameList) {
                                System.out.println("- " + name);
                            }
                            System.out.println();
                        }
                    } else {
                        if (!gameList.contains(gameName)) {
                            System.out.println(AnsiColors.ANSI_RED + "Name not present. Try again." + AnsiColors.ANSI_RESET);
                        } else {
                            correctInput = true;
                        }
                    }
                } while (!correctInput);
            }
        }
        if (!isJoin) {
            //creating a new game
            correctInput = false;
            //do-while statement to check that the user doesn't choose an illegal name for the game
            do {
                System.out.println("Choose a name for your game:");
                gameName = scanner.nextLine();

                if (gameList.contains(gameName)) {
                    System.out.println(AnsiColors.ANSI_RED + "Name already present, choose another one." + AnsiColors.ANSI_RESET);
                } else if (gameName.equalsIgnoreCase("join") || gameName.equalsIgnoreCase("create") || gameName.equalsIgnoreCase("refresh")) {
                    System.out.println(AnsiColors.ANSI_RED + "Illegal game name, choose another one." + AnsiColors.ANSI_RESET);
                } else {
                    correctInput = true;
                }
            } while (!correctInput);
        }

        this.gameName = gameName;
        insertNickname(isJoin, gameName);
    }


    /**
     * method to make the player insert its nickname
     */
    @Override
    public void insertNickname(boolean isJoin, String gameName) {
        boolean valid = false;
        while (!valid) {
            System.out.println("Choose a nickname");
            String nickname = scanner.nextLine();
            try {
                client.gameChoice(isJoin, gameName, nickname);
                valid = true;
            } catch (Exception e) {
                // if RMI client exception if the nickname is already present
                System.out.println("Nickname already existent, choose another one");
            }
        }
    }

    /**
     * method to make the player choose its color
     * @param colors available
     */
    public void insertColor(ArrayList<Color> colors) {
        boolean correct = false;
        Color color = null;

        while (!correct) {
            System.out.print("Choose a color from the ones available: ");
            for (Color c : colors) {
                System.out.print(c.toString() + "| ");
            }
            System.out.println();
            String choice = scanner.nextLine();
            
            switch (choice.toUpperCase()) {
                case ("RED") -> {if (colors.contains(Color.RED)) {color = Color.RED; correct = true;}}
                case ("BLUE") -> {if (colors.contains(Color.BLUE)) {color = Color.BLUE; correct = true;}}
                case ("YELLOW") -> {if (colors.contains(Color.YELLOW)) {color = Color.YELLOW; correct = true;}}
                case ("GREEN") -> {if (colors.contains(Color.GREEN)) {color = Color.GREEN; correct = true;}}
                default -> {System.out.println(AnsiColors.ANSI_RED+"Incorrect input."+AnsiColors.ANSI_RESET);}
            }
        };

        client.colorResponse(color);
    }

    /**
     * asks the client how many players there has to be in the game
     */
    @Override
    public void askForPlayersNumber() {
        int number = -1;
        do {
            System.out.println("How many player will play the game? (2-4)");
            String numberStr = scanner.nextLine();
            try {
                number = Integer.parseInt(numberStr);
            } catch (NumberFormatException e) {
                number = -1;
            }
            if (number <= 1 || number > 4) {
                System.out.println("Insert a valid number! (2-4)");
            }
        } while (number<=1 || number > 4);
        client.playersNumberResponse(number, this.gameName);
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
        this.goalCardViewer.SetCard(game.getGameTable().getPlayerZones().get(user).getPrivateGoal());
        this.goalCardViewer.Show();
    }

    /**
     * method to make the player choose on which side to play a starting card
     * @param card is the starting card
     */
    @Override
    public void ChooseStartingCardSide(StartingCard card) {
        if (card.getIsFront()) {
            System.out.println("This is the front of your starting card:");
            ShowCard(card);
            card.flip();
            System.out.println("This is the back of your starting card:");
            ShowCard(card);
            card.flip();
        } else {
            card.flip();
            System.out.println("This is the front of your starting card:");
            ShowCard(card);
            card.flip();
            System.out.println("This is the back of your starting card:");
            ShowCard(card);
        }
        boolean correct = false;
        while (!correct) {
            System.out.println("On which side do you want to play it?\nFRONT|BACK");
            String side = scanner.nextLine();
            if (side.equalsIgnoreCase("front") || side.equalsIgnoreCase("back")) {
                correct = true;
                boolean isFront = side.equalsIgnoreCase("front");
                client.playStartingCardResponse(card, isFront);
            } else {
                System.out.println(AnsiColors.ANSI_RED + "Invalid input. Try again." +AnsiColors.ANSI_RESET);
            }
        }
    }

    /**
     * method to show a gamecard
     * @param card to be displayed
     */
    public void ShowCard(GameCard card) {
        this.gameCardViewer.SetCard(card);
        this.gameCardViewer.Show();
    }

    /**
     * displays the two private goals the client has to choose between
     * @param goalCards is an array of two goal cards
     */
    @Override
    public void ShowChoosePrivateGoal(GoalCard[] goalCards) {
        ClearScreen();
        System.out.println("Goal Card N°1");
        this.goalCardViewer.SetCard(goalCards[0]);
        this.goalCardViewer.Show();
        System.out.println("Goal Card N°2");
        this.goalCardViewer.SetCard(goalCards[1]);
        this.goalCardViewer.Show();

        boolean correctInput = false;
        int result = 1;
        do {
            System.out.println("Select a card:\n1|2");
            try {
                String choice = scanner.nextLine();
                result = Integer.parseInt(choice);
                correctInput = true;
            } catch (NumberFormatException e) {
                correctInput = false;
                System.out.println(AnsiColors.ANSI_RED+"Invalid input. Insert a number."+AnsiColors.ANSI_RESET);
            }
            if (result!=1 && result!=2) {
                correctInput = false;
                System.out.println(AnsiColors.ANSI_RED+"Invalid input. Insert a number."+AnsiColors.ANSI_RESET);
            }
        } while (!correctInput);

        if (result == 1) {
            client.goalCardResponse(goalCards[0]);
        } else if (result == 2) {
            client.goalCardResponse(goalCards[1]);
        }

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
        GoalCard[] commonGoals = new GoalCard[2];
        commonGoals[0] = game.getGameTable().getCommonGoal(0);
        commonGoals[1] = game.getGameTable().getCommonGoal(1);
        this.deckViewer.setCommonGoals(commonGoals);
        this.deckViewer.showCommonGoals();
        this.deckViewer.setDeck(game.getGameTable().getGoldDeck());
        this.deckViewer.show();
        System.out.println();
        this.deckViewer.setDeck(game.getGameTable().getResourceDeck());
        this.deckViewer.show();
    }

    /**
     * displays the scoreboard
     */
    @Override
    public void ShowScoreBoard(ScoreBoard scoreBoard) {
        ClearScreen();
        this.scoreBoardViewer.set(scoreBoard);
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

    public void WelcomeMessage() {
        System.out.println("WELCOME TO:");
        System.out.println("\n" +
                "\n" +
                AnsiColors.ANSI_RED + "   _____"+AnsiColors.ANSI_GREEN+"            _"+AnsiColors.ANSI_RESET+"                                  \n" +
                AnsiColors.ANSI_RED + "  / ____|"+AnsiColors.ANSI_GREEN+"          | |"+AnsiColors.ANSI_RESET+"                                 \n" +
                AnsiColors.ANSI_RED + " | |"+AnsiColors.ANSI_PURPLE+"      ___"+AnsiColors.ANSI_GREEN+"    __| |"+AnsiColors.ANSI_CYAN+"  ___"+AnsiColors.ANSI_RED+" __  __" + AnsiColors.ANSI_RESET + "                     \n" +
                AnsiColors.ANSI_RED + " | |"+AnsiColors.ANSI_PURPLE+"     / _ \\"+AnsiColors.ANSI_GREEN+"  / _` |"+AnsiColors.ANSI_CYAN+" / _ \\"+AnsiColors.ANSI_RED+"\\ \\/ /"+AnsiColors.ANSI_RESET+"                     \n" +
                AnsiColors.ANSI_RED + " | |____"+AnsiColors.ANSI_PURPLE+"| (_) ||"+AnsiColors.ANSI_GREEN+" (_| ||"+AnsiColors.ANSI_CYAN+"  __/"+AnsiColors.ANSI_RED+" >  <"+AnsiColors.ANSI_RESET+"                      \n" +
                AnsiColors.ANSI_RED + "  \\_____|"+AnsiColors.ANSI_PURPLE+"\\___/"+AnsiColors.ANSI_GREEN+"  \\__,_|"+AnsiColors.ANSI_CYAN+" \\___|"+AnsiColors.ANSI_RED+"/_/\\_\\"+AnsiColors.ANSI_RESET+"                     \n" +
                "                                                       \n" +
                "                                                       \n" +
                AnsiColors.ANSI_PURPLE+"       _   _"+AnsiColors.ANSI_CYAN+"         _"+AnsiColors.ANSI_CYAN+"                       _"+AnsiColors.ANSI_RED+"  _      \n" +
                AnsiColors.ANSI_PURPLE+"      | \\ | |"+AnsiColors.ANSI_CYAN+"       | |"+AnsiColors.ANSI_CYAN+"                     | |"+AnsiColors.ANSI_RED+"(_)     \n" +
                AnsiColors.ANSI_PURPLE+"      |  \\| |"+AnsiColors.ANSI_GREEN+"  __ _"+AnsiColors.ANSI_CYAN+" | |_"+AnsiColors.ANSI_RED+"  _   _"+AnsiColors.ANSI_PURPLE+"  _ __"+AnsiColors.ANSI_GREEN+"  __ _"+AnsiColors.ANSI_CYAN+" | |"+AnsiColors.ANSI_RED+" _"+AnsiColors.ANSI_PURPLE+"  ___ \n" +
                AnsiColors.ANSI_PURPLE+"      | . ` | "+AnsiColors.ANSI_GREEN+"/ _` |"+AnsiColors.ANSI_CYAN+"| __|"+AnsiColors.ANSI_RED+"| | | |"+AnsiColors.ANSI_PURPLE+"| '__|"+AnsiColors.ANSI_GREEN+"/ _` |"+AnsiColors.ANSI_CYAN+"| |"+AnsiColors.ANSI_RED+"| |"+AnsiColors.ANSI_PURPLE+"/ __|\n" +
                AnsiColors.ANSI_PURPLE+"      | |\\  |"+AnsiColors.ANSI_GREEN+"| (_| |"+AnsiColors.ANSI_CYAN+"| |_"+AnsiColors.ANSI_RED+" | |_| |"+AnsiColors.ANSI_PURPLE+"| |"+AnsiColors.ANSI_GREEN+"  | (_| |"+AnsiColors.ANSI_CYAN+"| |"+AnsiColors.ANSI_RED+"| |"+AnsiColors.ANSI_PURPLE+"\\__ \\\n" +
                AnsiColors.ANSI_PURPLE+"      |_| \\_|"+AnsiColors.ANSI_GREEN+" \\__,_|"+AnsiColors.ANSI_CYAN+" \\__|"+AnsiColors.ANSI_RED+" \\__,_|"+AnsiColors.ANSI_PURPLE+"|_|"+AnsiColors.ANSI_GREEN+"   \\__,_|"+AnsiColors.ANSI_CYAN+"|_|"+AnsiColors.ANSI_RED+"|_|"+AnsiColors.ANSI_PURPLE+"|___/\n"+AnsiColors.ANSI_RESET +
                "                                                       \n");
    }

    /**
     * isMyTurn setter
     * @param isMyTurn tells whether it's the client's turn or not
     */
    @Override
    public void setMyTurn (boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
        if (isMyTurn) {
            client.yourTurnOk();
        }
    }

    /**
     * isMyTurn getter
     * @return whether it is my turn or not
     */
    public boolean getIsMyTurn() {
        return isMyTurn;
    }

    /**
     * playPhase setter
     * @param playPhase tells whether it's the client's turn or not
     */
    @Override
    public void setPlayPhase (boolean playPhase) {
        this.playPhase = playPhase;
    }

    /**
     * playPhase getter
     * @return whether it is the playing phase or not
     */
    public boolean getPlayPhase() {
        return this.playPhase;
    }

    /**
     * isMyTurn setter
     * @param user is the user that is going to use this client
     */
    @Override
    public void setUser (Player user) {
        this.user = user;
    }

    /**
     * user getter
     * @return the client's player
     */
    public Player getUser() {
        return this.user;
    }

    /**
     * game setter
     * @param game the game we need to set!
     */
    @Override
    public void setGame (GameState game) {
        this.game = game;
        for (Player p : game.getPlayers()) {
            if (p.equals(this.user)) {
                this.user = p;
                break;
            }
        }
    }

    /**
     * Update the list of names of available games
     *
     * @param gameNames the names
     */
    @Override
    public void setGameNames(ArrayList<String> gameNames) {
        gameList = gameNames;
    }

    /**
     * Update the game chat
     * @param gameChat the updated chat
     */
    @Override
    public void setGameChat (GameChat gameChat) {
        this.gameChat = gameChat;
    }

    /**
     * isMyTurn getter
     * @return whether it is my turn or not
     */
    public GameState getGame() {
        return this.game;
    }

    /**
     * method to get the user's inputs
     */
    @Override
    public void getCommands() {
        String command = "";
        Player lastPlayerField = null;

        //while statement to continuously check for the user's commands
        //it is stopped only if the user gets disconnected (it gets disconnected at the end of a game)
        while (this.client.getConnected() && this.connected) {
            System.out.println("Enter a command: ");
            command = this.scanner.nextLine();

            switch (command.toLowerCase()) {
                case "show my goal card" -> {ShowPrivateGoal(user, game);}
                case "show field" -> {lastPlayerField = commandShowPlayerField(game, user);}
                case "show decks" -> {ShowDecks(game);}
                case "show scoreboard" -> {ShowScoreBoard(game.getGameTable().getScoreBoard());}
                case "show card" -> {commandShowCard(game, user, lastPlayerField);}
                case "show chat" -> {showChat();}
                case "show legend" -> {showLegend();}
                case "play card" -> {
                    //command available only if it's the client's turn and it's the playphase
                    if (isMyTurn && playPhase) {
                        ShowPlayerField(user, user, game);

                        boolean correctInput = false;
                        int card = 0;
                        System.out.println();
                        //do-while statement to check the user's input and to make him choose the card to play
                        do {
                            System.out.println("Which of your hand's cards?\n1|2|3");
                            try {
                                String cardString = scanner.nextLine();
                                card = Integer.parseInt(cardString);
                                if (card >= 1 && card <= 3) {
                                    correctInput = true;
                                }
                            } catch (NumberFormatException e) {
                                System.out.println(AnsiColors.ANSI_RED + "Invalid input. Enter a number." + AnsiColors.ANSI_RESET);
                            }
                        } while (!correctInput);
                        correctInput = false;
                        //do-while statement to check the user's input and to make him choose the side on which to play the card
                        do {
                            System.out.println("On which side do you want to play it?\nFRONT|BACK");
                            boolean isFront;
                            String isFrontString = scanner.nextLine();
                            if (isFrontString.equalsIgnoreCase("front") || isFrontString.equalsIgnoreCase("back")) {
                                isFront = isFrontString.equalsIgnoreCase("front");
                                correctInput = true;
                                //everything's fine, sending the message to the server
                                Coordinates where = getCardCoordinatesFromInput();
                                client.playCardResponse(game.getGameTable().getPlayerZones().get(user).getHand().get(card - 1), where, isFront);
                            } else {
                                System.out.println(AnsiColors.ANSI_RED + "Incorrect input. Try again." + AnsiColors.ANSI_RESET);
                            }
                        } while (!correctInput);
                    } else {
                        if (!isMyTurn)
                            System.out.println(AnsiColors.ANSI_RED + "It's not your turn. You cannot play a card."  + AnsiColors.ANSI_RESET);
                        else
                            System.out.println(AnsiColors.ANSI_RED + "You already played a card, you need to draw now. Try the command 'draw card'"  + AnsiColors.ANSI_RESET);
                    }
                }
                case "draw card" -> {
                    //command available only if it's the user's turn and it's the drawphase
                    if (isMyTurn && !playPhase) {
                        boolean correctInput = false;
                        ShowDecks(game);
                        System.out.println();
                        //do-while statement to check the user's input and to make him choose what do draw
                        do {
                            System.out.println("From which deck do you want to draw from?\nResource|Gold");
                            String deck = scanner.nextLine();
                            if (deck.equalsIgnoreCase("resource") || deck.equalsIgnoreCase("gold")) {
                                do {
                                    //"deck" draws from the covered cards
                                    //"u1" draws the first uncovered card
                                    //"u2" draw the second uncovered card
                                    System.out.println("Where do you want to draw from?\nDeck|U1|U2");
                                    String where = scanner.nextLine();
                                    if (where.equalsIgnoreCase("deck") || where.equalsIgnoreCase("u1") || where.equalsIgnoreCase("u2")) {
                                        int which = 0;
                                        switch (where) {
                                            case "deck" -> {
                                                which = 0;
                                            }
                                            case "u1" -> {
                                                which = 1;
                                            }
                                            case "u2" -> {
                                                which = 2;
                                            }
                                        }
                                        correctInput = true;
                                        //everything's fine, sending the message to the server
                                        client.drawCardResponse(which, (deck.equalsIgnoreCase("gold")));
                                    } else {
                                        //did not type "deck", "u1" or "u2"
                                        System.out.println(AnsiColors.ANSI_RED + "Invalid input. Try again." + AnsiColors.ANSI_RESET);
                                    }
                                } while (!correctInput);
                            } else {
                                //did not type "resource" or "gold"
                                System.out.println(AnsiColors.ANSI_RED + "Invalid input. Try again." + AnsiColors.ANSI_RESET);
                            }
                        } while (!correctInput);
                    } else {
                        if (!isMyTurn)
                            System.out.println(AnsiColors.ANSI_RED + "It's not your turn. You cannot play a card."  + AnsiColors.ANSI_RESET);
                        else
                            System.out.println(AnsiColors.ANSI_RED + "You need to play a card before drawing. Try the command 'play card'"  + AnsiColors.ANSI_RESET);
                    }
                }
                case "help" -> {ShowCommands();}
                default -> {System.out.println(AnsiColors.ANSI_RED + "Not a valid command, type 'help' to show all the commands available."  + AnsiColors.ANSI_RESET);}
            }
        }
    }

    /**
     * method to show all the available commands the user can ask for
     */
    private void ShowCommands() {
        System.out.println(AnsiColors.ANSI_CYAN + "The available commands in this phase are:" + AnsiColors.ANSI_RESET);
        System.out.println(AnsiColors.ANSI_GREEN + "help              ->" + AnsiColors.ANSI_RESET + " displays all the available commands");
        System.out.println(AnsiColors.ANSI_GREEN + "show my goal card ->" + AnsiColors.ANSI_RESET + " displays your private goal card");
        System.out.println(AnsiColors.ANSI_GREEN + "show field        ->" + AnsiColors.ANSI_RESET + " displays a player's field");
        System.out.println(AnsiColors.ANSI_GREEN + "show decks        ->" + AnsiColors.ANSI_RESET + " displays the decks, the uncovered cards you can draw from and the common goals");
        System.out.println(AnsiColors.ANSI_GREEN + "show scoreboard   ->" + AnsiColors.ANSI_RESET + " displays the game's scoreboard");
        System.out.println(AnsiColors.ANSI_GREEN + "show card         ->" + AnsiColors.ANSI_RESET + " displays a specific card");
        System.out.println(AnsiColors.ANSI_GREEN + "show legend       ->" + AnsiColors.ANSI_RESET + " displays the game's legend");
        if (isMyTurn) {
            if (playPhase) {
                System.out.println(AnsiColors.ANSI_GREEN + "play card         ->" + AnsiColors.ANSI_RESET + " allows you to play a card from your hand onto your field");
            } else {
                System.out.println(AnsiColors.ANSI_GREEN + "draw card         ->" + AnsiColors.ANSI_RESET + " allows you to draw a card from the uncovered ones or from the decks");
            }
        }
    }

    /**
     * method to show what all the symbols mean
     */
    private void showLegend() {
        ClearScreen();
        System.out.println(AnsiColors.ANSI_CYAN + "Resources' Legend: " + AnsiColors.ANSI_RESET);
        System.out.println("- " + Resource.ANIMAL.toStringExt()+AnsiColors.ANSI_RESET);
        System.out.println("- " + Resource.PLANT.toStringExt()+AnsiColors.ANSI_RESET);
        System.out.println("- " + Resource.INSECT.toStringExt()+AnsiColors.ANSI_RESET);
        System.out.println("- " + Resource.FUNGI.toStringExt()+AnsiColors.ANSI_RESET);
        System.out.println("- " + Resource.POTION.toStringExt()+AnsiColors.ANSI_RESET);
        System.out.println("- " + Resource.SCROLL.toStringExt()+AnsiColors.ANSI_RESET);
        System.out.println("- " + Resource.FEATHER.toStringExt()+AnsiColors.ANSI_RESET);
        System.out.println("- " + Resource.BLANK.toStringExt()+AnsiColors.ANSI_RESET);
        System.out.println("- " + Resource.HIDDEN.toStringExt()+AnsiColors.ANSI_RESET);
        System.out.println("- " + Resource.COVERED.toStringExt()+AnsiColors.ANSI_RESET);
        System.out.println();
        System.out.println(AnsiColors.ANSI_CYAN + "Card's Legend: " + AnsiColors.ANSI_RESET);
        System.out.println("Every card has a resource on every corner and some have some in the center.");
        System.out.println("The goal cards display the resources needed for gaining points.");
        System.out.println("Some cards give points when played. The points are written on top of the card, adjacent to their point condition:");
        System.out.print(AnsiColors.ANSI_GREEN);
        System.out.println("- " + PointCondition.NORMAL.toStringExt());
        System.out.println("- " + PointCondition.POTION.toStringExt());
        System.out.println("- " + PointCondition.SCROLL.toStringExt());
        System.out.println("- " + PointCondition.FEATHER.toStringExt());
        System.out.println("- " + PointCondition.CORNER.toStringExt());
        System.out.println(AnsiColors.ANSI_RESET);
        System.out.println("In order to play gold cards, you need to satisfy the playing requirements. You can find thw resources needed to play the card in the bottom of it.");
        System.out.println("You can play cards either on their front or on their back. The back of the cards has all blank corners and provides one permanent resource corresponding to it's kingdom.");
        System.out.println();
        System.out.println(AnsiColors.ANSI_CYAN + "Deck's Legend:" + AnsiColors.ANSI_RESET);
        System.out.println("There are two decks, one resource cards and one of gold cards.");
        System.out.println("Every deck has also two uncovered cards you can draw from.");
        System.out.println("Lastly, next to the decks there are the two common goals.");
    }

    /**
     * method to handle the show field command
     * @param game we are referring to
     * @param asking is the player entering the command
     * @return the reference to the playerfield printed
     */
    private Player commandShowPlayerField(GameState game, Player asking) {
        Player lastPlayerField;
        boolean correctInput = false;
        Player player = null;
        //do-while statement to check the user's input and to make him choose the playerfield to see
        do {
            System.out.println("Which player do you want to see the field of?");
            for (Player p : game.getPlayers()) {
                System.out.print(p.toString()+"| ");
            }
            System.out.println();
            String nickname = scanner.nextLine();
            for (Player p : game.getPlayers()) {
                if (p.getNickname().equals(nickname)) {
                    player = p;
                    correctInput = true;
                }
            }
            if (player==null) {
                System.out.println(AnsiColors.ANSI_RED+"Nickname not valid. Try again."+AnsiColors.ANSI_RESET);
            }
        } while (!correctInput);
        ShowPlayerField(player, asking, game);
        lastPlayerField = player;
        return lastPlayerField;
    }

    /**
     * method to handle the show card command
     * @param game we are referring to
     * @param asking is the player entering the command
     * @param lastPlayerField is the last field the user has seen
     */
    private void commandShowCard(GameState game, Player asking, Player lastPlayerField) {
        if (lastPlayerField == null) {
            //the user has not yet seen a playerfield, making him choose one before chooseing the card
            boolean correctInput = false;
            do {
                System.out.println("Enter the player you want to see the card of:");
                for (Player p : game.getPlayers()) {
                    System.out.print(p.toString() + "| ");
                }
                System.out.println();
                String nickname = scanner.nextLine();
                Player player = null;
                for (Player p : game.getPlayers()) {
                    if (p.getNickname().equals(nickname))
                        player = p;
                }
                if (player == null)
                    System.out.println(AnsiColors.ANSI_RED + "Not a valid nickname. Try again." + AnsiColors.ANSI_RESET);
                else {
                    lastPlayerField = player;
                    correctInput = true;
                }
            } while (!correctInput);

        }
        ShowPlayerField(lastPlayerField, asking, game);
        System.out.println();
        System.out.println("Which card do you want to see?");
        Coordinates where = getCardCoordinatesFromInput();

        GameCard card = null;
        //for statement to find the card inside the gamezone
        for (Coordinates c : game.getGameTable().getPlayerZones().get(lastPlayerField).getGameZone().keySet()) {
            if (c.getX()== where.getX() && c.getY()== where.getY()) {
                card = game.getGameTable().getPlayerZones().get(lastPlayerField).getGameZone().get(c);
            }
        }
        if (where.getX()==0 && where.getY() == 0) {
            if (card == null) {
                System.out.println(AnsiColors.ANSI_RED + "Invalid coordinate. Enter a new command." + AnsiColors.ANSI_RESET);
            } else {
                gameCardViewer.SetCard(card);
                gameCardViewer.Show();
            }
        } else {
            if (card == null) {
                System.out.println(AnsiColors.ANSI_RED + "Invalid coordinate. Enter a new command." + AnsiColors.ANSI_RESET);
            } else {
                if (((ResourceCard) card).getIsGold()) {
                    gameCardViewer.SetCard((GoldCard) card);
                    gameCardViewer.Show();
                } else {
                    gameCardViewer.SetCard((ResourceCard) card);
                    gameCardViewer.Show();
                }
            }
        }
    }

    /**
     * method that handles the request of a card's coordinates
     * @return a new coordinates object
     */
    private Coordinates getCardCoordinatesFromInput () {
        boolean correctInput = false;
        int x = 0;
        int y = 0;
        //do-while statement to check the user's input and to make him choose the X coordinate
        do {
            System.out.println("Insert x coordinate:");
            try {
                String xString = scanner.nextLine();
                x = Integer.parseInt(xString);
                correctInput = true;
            } catch (NumberFormatException e) {
                correctInput = false;
                System.out.println(AnsiColors.ANSI_RED+"Invalid input. Enter a number."+AnsiColors.ANSI_RESET);
            }
        } while (!correctInput);
        correctInput = false;
        //do-while statement to check the user's input and to make him choose the Y coordinate
        do {
            System.out.println("Insert y coordinate:");
            try {
                String yString = scanner.nextLine();
                y = Integer.parseInt(yString);
                correctInput = true;
            } catch (NumberFormatException e) {
                correctInput = false;
                System.out.println(AnsiColors.ANSI_RED+"Invalid input. Enter a number."+AnsiColors.ANSI_RESET);
            }
        } while (!correctInput);
        return (new Coordinates(x,y));
    }

    /**
     * method to handle chat commands
     */
    private void showChat () {
        int messagePage = 0;
        ArrayList<Message> myChat = this.gameChat.messagesToShow(messagePage);
        String command = null;
        boolean exitChat = false;

        printChat(messagePage, myChat);

        while (!exitChat) {
            System.out.println("What do you want to do in chat?\nexit | newer | older | write | refresh");
            command = this.scanner.nextLine();

            switch (command.toLowerCase()) {
                case "exit" -> {exitChat = true;}
                case "newer" -> {
                    if (messagePage == 0) {
                        System.out.println(AnsiColors.ANSI_RED+"You are already seeing the newest messages."+AnsiColors.ANSI_RESET);
                    } else {
                        messagePage--;

                        myChat = this.gameChat.messagesToShow(messagePage);

                        printChat(messagePage, myChat);
                    }
                }
                case "older" -> {
                    if (messagePage == gameChat.getMessages().size()/10) {
                        System.out.println(AnsiColors.ANSI_RED+"You are already seeing the oldest messages."+AnsiColors.ANSI_RESET);
                    } else {
                        messagePage++;

                        myChat = this.gameChat.messagesToShow(messagePage);

                        printChat(messagePage, myChat);
                    }
                }
                case "write" -> {
                    boolean correctInput = false;
                    String recipient = null;

                    do {
                        System.out.println("Who is the recipient?");
                        System.out.print("all| ");
                        for (Player p : game.getPlayers()) {
                            if (p.getNickname().equals(user.getNickname())) {
                                continue;
                            }
                            System.out.print(p.toString()+"| ");
                        }
                        System.out.println();
                        recipient = this.scanner.nextLine();

                        Player player = null;
                        for (Player p : game.getPlayers()) {
                            if (p.getNickname().equals(recipient))
                                player = p;
                        }

                        if (!recipient.equalsIgnoreCase("all") && player==null) {
                            //invalid recipient
                            System.out.println(AnsiColors.ANSI_RED+"Invalid input, try again."+AnsiColors.ANSI_RESET);
                        } else if (player != null && player.getNickname().equalsIgnoreCase(user.getNickname())) {
                            //The recipient is the user itself
                            System.out.println(AnsiColors.ANSI_RED+"Cannot send a message to yourself, try again."+AnsiColors.ANSI_RESET);
                        } else {
                            //valid recipient
                            correctInput = true;
                        }
                    } while (!correctInput);

                    correctInput = false;

                    do {
                        System.out.println("Enter the message text:");
                        String text = this.scanner.nextLine();

                        if (text.isEmpty()) {
                            //empty text
                            System.out.println(AnsiColors.ANSI_RED+"A message cannot be empty."+AnsiColors.ANSI_RESET);
                        } else {
                            //valid message
                            Message toSend = new Message(text, user, recipient);
                            client.sendMessageInChat(toSend);
                            correctInput = true;
                            System.out.println(AnsiColors.ANSI_GREEN+"Message sent!"+AnsiColors.ANSI_RESET);
                        }
                    } while (!correctInput);

                    messagePage = 0;
                    myChat = this.gameChat.messagesToShow(messagePage);
                    printChat(messagePage, myChat);
                }
                case "refresh" -> {
                    messagePage = 0;
                    myChat = this.gameChat.messagesToShow(messagePage);
                    printChat(messagePage, myChat);
                }
                default -> {System.out.println(AnsiColors.ANSI_RED + "Invalid input. Try again." + AnsiColors.ANSI_RESET);}
            }
        }
    }

    /**
     * method to print the game chat
     * @param messagePage the page you want to see
     * @param myChat the game's chat
     */
    private void printChat(int messagePage, ArrayList<Message> myChat) {
        if (!myChat.isEmpty()) {
            //myChat = (ArrayList<Message>) myChat.reversed();
        }

        System.out.println(user.toString()+"'s gamechat. Page:" + messagePage);
        System.out.println();
        for (Message m : myChat) {
            System.out.println("- " + m.toString());
        }
        System.out.println();
    }
}
