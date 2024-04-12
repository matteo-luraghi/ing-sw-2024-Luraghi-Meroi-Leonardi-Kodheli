package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.controller.MegaController;
import it.polimi.ingsw.model.gamelogic.Player;

/**
 * Megaview interface, will be implemented for GUI and CLI
 * @author Lorenzo Meroi
 */
public interface MegaView {
    MegaController controller;
    ViewGameCardFactory gameCardViewer;
    ViewScoreBoardFactory scoreBoardViewer;
    ViewPlayerFieldFactory playerFieldViewer;
    ViewDeckFactory deckViewer;

    void ShowLogin();

    void ShowWaitingForPlayers();

    void ShowPrivateGoals(Player player);

    void ShowPlayerField(Player player);

    void ShowDecks();

    void ShowScoreBoard();

    void ShowWinner();

    void ShowEndOfGame();
}
