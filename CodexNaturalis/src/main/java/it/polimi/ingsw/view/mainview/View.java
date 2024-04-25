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

    void showMessage (String s);

    void ShowWaitingForPlayers();

    void ShowPrivateGoals(Player player);

    void ShowPlayerField(Player player);

    void ShowDecks();

    void ShowScoreBoard();

    void ShowWinner();

    void ShowEndOfGame();
}
