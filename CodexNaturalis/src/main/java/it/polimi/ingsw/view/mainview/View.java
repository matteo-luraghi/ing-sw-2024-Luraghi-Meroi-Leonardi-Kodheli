package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.gamelogic.Player;

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

    void start();

    Client connectToServer();

    void showMessage (String s);

    void ShowLogin();

    void askForPlayersNumber();

    void ShowWaitingForPlayers();

    void ShowChoosePrivateGoal(GoalCard[] goalCards);

    void ShowPrivateGoal(Player player);

    void ShowPlayerField(Player player);

    void ShowDecks();

    void ShowScoreBoard();

    void ShowWinner();

    void ShowEndOfGame();
}
