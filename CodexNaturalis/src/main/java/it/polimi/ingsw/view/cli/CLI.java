package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.connection.message.clientMessage.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.gamelogic.*;
import it.polimi.ingsw.view.mainview.View;

import it.polimi.ingsw.view.mainview.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
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
        WelcomeMessage();
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
                client.sendMessageServer(new PlayStartingCardResponse(card, isFront));
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
        } while (!correctInput);

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
                "                                                       \n" +
                "                                                       \n" +
                "                                                       \n" +
                "                                                       \n" +
                "                                                       \n" +
                "                                                       \n" +
                "                                                       \n" +
                "                                                       \n" +
                "                                                       \n" +
                "                                                       \n" +
                "\n");
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

    //game, user, playPhase

    /**
     * playPhase setter
     * @param playPhase tells whether it's the client's turn or not
     */
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
     * isMyTurn setter
     * @param isMyTurn tells whether it's the client's turn or not
     */
    public void setGame (GameState game) {
        this.game = game;
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
    public void getCommands() {
        String command = "";
        Player lastPlayerField = null;
        while (true) {
            System.out.println("Enter a command: ");
            command = this.scanner.nextLine();

            switch (command.toLowerCase()) {
                case "show my goal card" -> {ShowPrivateGoal(user, game);}
                case "show field" -> {lastPlayerField = commandShowPlayerField(game, user);}
                case "show decks" -> {ShowDecks(game);}
                case "show scoreboard" -> {ShowScoreBoard(game.getGameTable().getScoreBoard());}
                case "show card" -> {commandShowCard(game, user, lastPlayerField);}
                case "show legend" -> {showLegend();}
                case "play card" -> {
                    if (isMyTurn && playPhase) {
                        ShowPlayerField(user, user, game);

                        boolean correctInput = false;
                        int card = 0;
                        System.out.println();
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
                        do {
                            System.out.println("On which side do you want to play it?\nFRONT|BACK");
                            boolean isFront;
                            String isFrontString = scanner.nextLine();
                            if (isFrontString.equalsIgnoreCase("front") || isFrontString.equalsIgnoreCase("back")) {
                                isFront = isFrontString.equalsIgnoreCase("front");
                                correctInput = true;

                                Coordinates where = getCardCoordinatesFromInput();
                                client.sendMessageServer(new PlayCardResponse(game.getGameTable().getPlayerZones().get(user).getHand().get(card - 1), where, isFront));
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
                    if (isMyTurn && !playPhase) {
                        boolean correctInput = false;
                        ShowDecks(game);
                        System.out.println();
                        do {
                            System.out.println("From which deck do you want to draw from?\nResource|Gold");
                            String deck = scanner.nextLine();
                            if (deck.equalsIgnoreCase("resource") || deck.equalsIgnoreCase("gold")) {
                                do {
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
                                        client.sendMessageServer(new DrawCardResponse(which, (deck.equalsIgnoreCase("gold"))));
                                    } else {
                                        System.out.println(AnsiColors.ANSI_RED + "Invalid input. Try again." + AnsiColors.ANSI_RESET);
                                    }
                                } while (!correctInput);
                            } else {
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
        System.out.println(AnsiColors.ANSI_CYAN + "The available commands in this phase are:" + AnsiColors.ANSI_RESET);
        System.out.println(AnsiColors.ANSI_GREEN + "help              ->" + AnsiColors.ANSI_RESET + " displays all the available commands");
        System.out.println(AnsiColors.ANSI_GREEN + "show my goal card ->" + AnsiColors.ANSI_RESET + " displays your private goal card");
        System.out.println(AnsiColors.ANSI_GREEN + "show field        ->" + AnsiColors.ANSI_RESET + " displays a player's field");
        System.out.println(AnsiColors.ANSI_GREEN + "show decks        ->" + AnsiColors.ANSI_RESET + " displays the decks, the uncovered cards you can draw from and the common goals");
        System.out.println(AnsiColors.ANSI_GREEN + "show scoreboard   ->" + AnsiColors.ANSI_RESET + " displays the game's scoreboard");
        System.out.println(AnsiColors.ANSI_GREEN + "show card         ->" + AnsiColors.ANSI_RESET + " displays a specific card");
        System.out.println(AnsiColors.ANSI_GREEN + "show legend       ->" + AnsiColors.ANSI_RESET + " displays the game's legend");
        if (isMyTurn) {
            if (playing) {
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
}
