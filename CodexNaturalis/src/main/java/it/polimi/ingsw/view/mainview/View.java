package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.gamelogic.GameState;
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

    Player ShowLogin();

    int askForPlayersNumber();

    void ShowWaitingForPlayers();

    void ShowPrivateGoal(Player player, GameState game);

    void ShowChoosePrivateGoal(GoalCard[] goalCards);

    void ShowPlayerField(Player playerToSee, Player playerAsking, GameState game);

    void ShowDecks(GameState game);

    void ShowScoreBoard();

    void ShowWinner(GameState game);

    void ShowEndOfGame();
}
