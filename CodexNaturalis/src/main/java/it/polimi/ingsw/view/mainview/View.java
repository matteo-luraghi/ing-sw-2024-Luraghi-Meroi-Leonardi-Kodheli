package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.ScoreBoard;

import java.util.ArrayList;

/**
 * Megaview interface, will be implemented for GUI and CLI
 * @author Lorenzo Meroi
 */
public interface View {
    Client client = null;
    Controller controller = null;
    ViewGameCardFactory gameCardViewer = null;
    ViewScoreBoardFactory scoreBoardViewer  = null;
    ViewPlayerFieldFactory playerFieldViewer  = null;
    ViewDeckFactory deckViewer  = null;
    ViewGoalCardFactory goalCardViewer = null;

    /**
     * method to initialize the CLI for a specific Client
     */
    void start();

    /**
     * method to show any type of String
     * @param s is the string you want to be displayed
     */
    void showMessage (String s);

    /**
     * method to make the player insert its nickname
     */
    void insertNickname();

    /**
     * method to make the player choose its color
     * @param colors available
     */
    void insertColor(ArrayList<Color> colors);

    /**
     * asks the client how many players there has to be in the game
     */
    void askForPlayersNumber();

    /**
     * method to display the waiting for players page
     */
    void ShowWaitingForPlayers();

    /**
     * method to show the private goal of a specific player
     * @param player of which to display the goal
     * @param game in which the player is partecipating
     */
    void ShowPrivateGoal(Player player, GameState game);

    /**
     * displays the two private goals the client has to choose between
     * @param goalCards is an array of two goal cards
     */
    void ShowChoosePrivateGoal(GoalCard[] goalCards);

    /**
     * displays the player field of a specific player
     * @param playerToSee specifies which playerfield has to be displayed
     * @param playerAsking tells which player is asking to see it
     * @param game we are referring to
     */
    void ShowPlayerField(Player playerToSee, Player playerAsking, GameState game);

    /**
     * displays the two decks and the uncovered cards
     * @param game we are referring to
     */
    void ShowDecks(GameState game);

    /**
     * displays the scoreboard
     */
    void ShowScoreBoard(ScoreBoard scoreBoard);

    /**
     * shows who has won the game
     * @param game we are referring to
     */
    void ShowWinner(GameState game);

    /**
     * shows the end of game text
     */
    void ShowEndOfGame();
}
