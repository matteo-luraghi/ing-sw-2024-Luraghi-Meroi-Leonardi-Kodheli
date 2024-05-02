package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.connection.message.clientMessage.*;
import it.polimi.ingsw.model.card.GameCard;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.gamelogic.*;
import it.polimi.ingsw.view.mainview.View;

import it.polimi.ingsw.view.mainview.*;

import java.io.IOException;
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
     * method to connect a client to the server
     */
    @Override
    public void start() {
        boolean connected = false;
        String ip = null;
        int port = 0;
        Client client = null;

        do {
            System.out.println("Insert a valid ip address:");
            ip = scanner.nextLine();
            System.out.println("Insert a valid port to connect:");
            String portStr = scanner.nextLine();
            try {
                port = Integer.parseInt(portStr);
                try {
                    client = new Client(ip, port, this);
                    connected = true;
                } catch (IOException | IllegalArgumentException e) {
                    System.out.println("Error connecting to the server, try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Insert a valid port number!");
            }

        } while(!connected);

        this.client = client;
    }

    /**
     * method to clear the console if the program is being run in the windows console
     */
    private void ClearScreen () {
        System.out.print("\033c");
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
     * method to make the player insert its nickname
     */
    @Override
    public void insertNickname() {
        System.out.println("Choose a nickname");
        String nickname = scanner.nextLine();
        client.sendMessageServer(new LoginResponse(nickname));
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

        client.sendMessageServer(new ColorResponse(color));
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
        client.sendMessageServer(new PlayersNumberResponse(number));
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

        // TODO: you know (check user input)
        System.out.println("Select a card:");
        int result = scanner.nextInt();
        if (result == 1) {
            client.sendMessageServer(new GoalCardResponse(goalCards[0]));
        } else if (result == 2) {
            client.sendMessageServer(new GoalCardResponse(goalCards[1]));
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
        this.deckViewer.setDeck(game.getGameTable().getGoldDeck());
        this.deckViewer.show();
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

    /**
     * isMyTurn setter
     * @param isMyTurn tells whether it's the client's turn or not
     */
    public void setMyTurn (boolean isMyTurn) {
        this.isMyTurn = isMyTurn;
        if (isMyTurn) {
            this.client.sendMessageServer(new YourTurnOk());
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
     * method to parse and get the user's input as commands when it's not the client's turn
     */
    public void GetCommandWhileNotYourTurn(GameState game, Player asking) {
        String command = "";
        Player lastPlayerField = null;
        while (!this.isMyTurn) {
            System.out.print("Enter a command: ");
            command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "show my goal card" -> {ShowPrivateGoal(asking, game);}
                case "show field" -> {
                    System.out.println("Which player do you want to see the field of?");
                    for (Player p : game.getPlayers()) {
                        System.out.print(p.toString()+"| ");
                    }
                    System.out.println();
                    String nickname = scanner.nextLine();
                    Player player = null;
                    for (Player p : game.getPlayers()) {
                        if (p.getNickname().equals(nickname))
                            player = p;
                    }
                    if (player == null)
                        System.out.println(AnsiColors.ANSI_RED + "Not a valid nickname. Enter a new command." + AnsiColors.ANSI_RESET);
                    else {
                        ShowPlayerField(player, asking, game);
                        lastPlayerField = player;
                    }
                }
                case "show decks" -> {ShowDecks(game);}
                case "show scoreboard" -> {ShowScoreBoard(game.getGameTable().getScoreBoard());}
                case "show card" -> {
                    if (lastPlayerField == null) {
                        System.out.println("Enter the player you want to see the card of:");
                        for (Player p : game.getPlayers()) {
                            System.out.print(p.toString()+"| ");
                        }
                        System.out.println();
                        String nickname = scanner.nextLine();
                        Player player = null;
                        for (Player p : game.getPlayers()) {
                            if (p.getNickname().equals(nickname))
                                player = p;
                        }
                        if (player == null)
                            System.out.println(AnsiColors.ANSI_RED + "Not a valid nickname. Enter a new command." + AnsiColors.ANSI_RESET);
                        else {
                            lastPlayerField = player;
                        }
                    }
                    ShowPlayerField(lastPlayerField, asking, game);
                    System.out.println();
                    System.out.println("Which card do you want to see?");
                    System.out.println("Insert x coordinate:");
                    int x = scanner.nextInt();
                    System.out.println("Insert y coordinate:");
                    int y = scanner.nextInt();
                    Coordinates where = new Coordinates(x,y);

                    GameCard card = null;
                    for (Coordinates c : game.getGameTable().getPlayerZones().get(lastPlayerField).getGameZone().keySet()) {
                        if (c.getX()==x && c.getY()==y) {
                            card = game.getGameTable().getPlayerZones().get(lastPlayerField).getGameZone().get(c);
                        }
                    }
                    if (x==0 && y == 0) {
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
                case "help" -> {ShowCommands(false);}
                default -> {System.out.println(AnsiColors.ANSI_RED + "Not a valid command, type 'help' to show all the commands available."  + AnsiColors.ANSI_RESET);}
            }
        }
    }

    /**
     * method to parse and get the user's input as commands when it's the playing phase of the client's turn
     */
    public void GetCommandInPlayState(GameState game, Player asking) {
        String command = "";
        Player lastPlayerField = null;
        while (!command.equals("play card")) {
            System.out.print("Enter a command: ");
            command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "show my goal card" -> {ShowPrivateGoal(asking, game);}
                case "show field" -> {
                    System.out.println("Which player do you want to see the field of?");
                    String nickname = scanner.nextLine();
                    Player player = null;
                    for (Player p : game.getPlayers()) {
                        if (p.getNickname().equals(nickname))
                            player = p;
                    }
                    ShowPlayerField(player, asking, game);
                    lastPlayerField = player;
                }
                case "show decks" -> {ShowDecks(game);}
                case "show scoreboard" -> {ShowScoreBoard(game.getGameTable().getScoreBoard());}
                case "show card" -> {
                    if (lastPlayerField == null) {
                        System.out.println("Enter the player you want to see the card of:");
                        for (Player p : game.getPlayers()) {
                            System.out.print(p.toString()+"| ");
                        }
                        System.out.println();
                        String nickname = scanner.nextLine();
                        Player player = null;
                        for (Player p : game.getPlayers()) {
                            if (p.getNickname().equals(nickname))
                                player = p;
                        }
                        if (player == null)
                            System.out.println(AnsiColors.ANSI_RED + "Not a valid nickname. Enter a new command." + AnsiColors.ANSI_RESET);
                        else {
                            lastPlayerField = player;
                        }
                    }
                    ShowPlayerField(lastPlayerField, asking, game);
                    System.out.println();
                    System.out.println("Which card do you want to see?");
                    System.out.println("Insert x coordinate:");
                    int x = scanner.nextInt();
                    System.out.println("Insert y coordinate:");
                    int y = scanner.nextInt();
                    Coordinates where = new Coordinates(x,y);

                    GameCard card = null;
                    for (Coordinates c : game.getGameTable().getPlayerZones().get(lastPlayerField).getGameZone().keySet()) {
                        if (c.getX()==x && c.getY()==y) {
                            card = game.getGameTable().getPlayerZones().get(lastPlayerField).getGameZone().get(c);
                        }
                    }
                    if (x==0 && y == 0) {
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
                case "play card" -> {
                    ShowPlayerField(asking, asking, game);
                    System.out.println();
                    System.out.println("Which of your hand's cards?\n1|2|3");
                    int card = scanner.nextInt();
                    if (card < 1 || card > 3)
                        System.out.println(AnsiColors.ANSI_RED + "Invalid number. Enter a new command." + AnsiColors.ANSI_RESET);
                    else {
                        System.out.println("On which side do you want to play it?\nFRONT|BACK");
                        boolean isFront;
                        String isFrontString = scanner.nextLine();
                        if (isFrontString.equalsIgnoreCase("front") || isFrontString.equalsIgnoreCase("back")) {
                            isFront = isFrontString.equalsIgnoreCase("front");

                            System.out.println("Where do you want to play it?");
                            System.out.println("Write X coordinate");
                            int x = scanner.nextInt();
                            System.out.println("Write Y coordinate");
                            int y = scanner.nextInt();
                            Coordinates where = new Coordinates(x, y);
                            //TODO add boolean isFront to playCardResponse
                            client.sendMessageServer(new PlayCardResponse(game.getGameTable().getPlayerZones().get(asking).getHand().get(card-1), where));
                        } else {
                            System.out.println(AnsiColors.ANSI_RED + "Invalid input. Enter a new command." + AnsiColors.ANSI_RESET);
                        }
                    }
                }
                case "help" -> {ShowCommands(true);}
                default -> {System.out.println(AnsiColors.ANSI_RED + "Not a valid command, type 'help' to show all the commands available."  + AnsiColors.ANSI_RESET);}
            }
        }
    }

    /**
     * method to parse and get the user's input as commands when it's the drawing phase of the client's turn
     */
    public void GetCommandInDrawState(GameState game, Player asking) {
        String command = "";
        Player lastPlayerField = null;
        while (!command.equals("draw card")) {
            System.out.print("Enter a command: ");
            command = scanner.nextLine();

            switch (command.toLowerCase()) {
                case "show my goal card" -> {ShowPrivateGoal(asking, game);}
                case "show field" -> {
                    System.out.println("Which player do you want to see the field of?");
                    String nickname = scanner.nextLine();
                    Player player = null;
                    for (Player p : game.getPlayers()) {
                        if (p.getNickname().equals(nickname)) player = p;
                    }
                    ShowPlayerField(player, asking, game);
                    lastPlayerField = player;
                }
                case "show decks" -> {ShowDecks(game);}
                case "show scoreboard" -> {ShowScoreBoard(game.getGameTable().getScoreBoard());}
                case "show card" -> {
                    if (lastPlayerField == null) {
                        System.out.println("Enter the player you want to see the card of:");
                        for (Player p : game.getPlayers()) {
                            System.out.print(p.toString()+"| ");
                        }
                        System.out.println();
                        String nickname = scanner.nextLine();
                        Player player = null;
                        for (Player p : game.getPlayers()) {
                            if (p.getNickname().equals(nickname))
                                player = p;
                        }
                        if (player == null)
                            System.out.println(AnsiColors.ANSI_RED + "Not a valid nickname. Enter a new command." + AnsiColors.ANSI_RESET);
                        else {
                            lastPlayerField = player;
                        }
                    }
                    ShowPlayerField(lastPlayerField, asking, game);
                    System.out.println();
                    System.out.println("Which card do you want to see?");
                    System.out.println("Insert x coordinate:");
                    int x = scanner.nextInt();
                    System.out.println("Insert y coordinate:");
                    int y = scanner.nextInt();
                    Coordinates where = new Coordinates(x,y);

                    GameCard card = null;
                    for (Coordinates c : game.getGameTable().getPlayerZones().get(lastPlayerField).getGameZone().keySet()) {
                        if (c.getX()==x && c.getY()==y) {
                            card = game.getGameTable().getPlayerZones().get(lastPlayerField).getGameZone().get(c);
                        }
                    }
                    if (x==0 && y == 0) {
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
                case "draw card" -> {
                    ShowDecks(game);
                    System.out.println();
                    System.out.println("From which deck do you want to draw from?\nResource|Gold");
                    String deck = scanner.nextLine();
                    if (deck.equalsIgnoreCase("resource") || deck.equalsIgnoreCase("gold")) {
                        System.out.println("Where do you want to draw from?\nDeck|U1|U2");
                        String where = scanner.nextLine();
                        if (where.equalsIgnoreCase("deck") || where.equalsIgnoreCase("u1") || where.equalsIgnoreCase("u2")) {
                            int which = 0;
                            switch (where) {
                                case "deck" -> {which = 0;}
                                case "u1" -> {which = 1;}
                                case "u2" -> {which = 2;}
                            }

                            client.sendMessageServer(new DrawCardResponse(which, (deck.equalsIgnoreCase("gold"))));
                        }
                    } else {
                        System.out.println(AnsiColors.ANSI_RED + "Invalid input. Enter a new command." + AnsiColors.ANSI_RESET);
                    }
                }
                case "help" -> {ShowCommands(false);}
                default -> {System.out.println(AnsiColors.ANSI_RED + "Not a valid command, type 'help' to show all the commands available."  + AnsiColors.ANSI_RESET);}
            }
        }
    }

    /**
     * method to show all the available commands the user can ask for
     * @param playing is used to distinguish the playing phase of the turn from its drawing phase
     */
    private void ShowCommands(boolean playing) {
        System.out.println("The available commands in this phase are:");
        System.out.println("help              -> displays all the available commands");
        System.out.println("show my goal card -> displays your private goal card");
        System.out.println("show field        -> displays a player's field");
        System.out.println("show decks        -> displays the decks, the uncovered cards you can draw from and the common goals");
        System.out.println("show scoreboard   -> displays the game's scoreboard");
        System.out.println("show card         -> displays a specific card");
        if (isMyTurn) {
            if (playing) {
                System.out.println("play card         -> allows you to play a card from your hand onto your field");
            } else {
                System.out.println("draw card         -> allows you to draw a card from the uncovered ones or from the decks");
            }
        }
    }
}
