package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.controller.MegaController;
import it.polimi.ingsw.model.gamelogic.Player;

/**
 * Megaview interface, will be implemented for GUI and CLI
 * @author Lorenzo Meroi
 */
public interface MegaView {
    MegaController controller = null;
    ViewGameCardFactory gameCardViewer = null;
    ViewScoreBoardFactory scoreBoardViewer  = null;
    ViewPlayerFieldFactory playerFieldViewer  = null;
    ViewDeckFactory deckViewer  = null;

    void ShowLogin();

    void ShowWaitingForPlayers();

    void ShowPrivateGoals(Player player);

    void ShowPlayerField(Player player);

    void ShowDecks();

    void ShowScoreBoard();

    void ShowWinner();

    void ShowEndOfGame();
}