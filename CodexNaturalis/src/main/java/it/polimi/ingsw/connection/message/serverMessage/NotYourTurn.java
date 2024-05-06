package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

/**
 * NotYourTurn class
 * used to end the current player's turn
 * @author Matteo Leonardo Luraghi
 */
public class NotYourTurn extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 2741292733883597798L;
    private final Player player;
    private final GameState game;
    private final String message;

    /**
     * Constructor
     */
    public NotYourTurn(Player player, GameState game, String message) {
        this.player = player;
        this.game = game;
        this.message = message;
    }

    /**
     * Show the turn ended message
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.showMessage(this.message);
        if (view.getClass() == CLI.class) {
            ((CLI) view).setMyTurn(false);
            ((CLI) view).setGameToUpdate(true);
            ((CLI) view).GetCommandWhileNotYourTurn(this.game ,this.player);
        }
    }
}
