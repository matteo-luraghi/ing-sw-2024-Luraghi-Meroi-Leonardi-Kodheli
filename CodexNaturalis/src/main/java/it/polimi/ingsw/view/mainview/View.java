package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.gamelogic.Player;

/**
 * Megaview interface, will be implemented for GUI and CLI
 * @author Lorenzo Meroi
 */
public interface View {
    Controller controller = null;
    ViewGameCardFactory gameCardViewer = null;
    ViewScoreBoardFactory scoreBoardViewer  = null;
    ViewPlayerFieldFactory playerFieldViewer  = null;
    ViewDeckFactory deckViewer  = null;
    ViewGoalCardFactory goalCardViewer = null;

    void showMessage (String s);

    void ShowLogin();

    void askForPlayersNumber();

    void ShowWaitingForPlayers();

    void ShowChoosePrivateGoal(Player player);

    void ShowPlayerField(Player player);

    void ShowDecks();

    void ShowScoreBoard();

    void ShowWinner();

    void ShowEndOfGame();
}
