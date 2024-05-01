package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

/**
 * TurnEnded class
 * used to end the current player's turn
 * @author Matteo Leonardo Luraghi
 */
public class TurnEnded extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 2741292733883597798L;
    private final Player player;
    private final GameState game;

    /**
     * Constructor
     */
    public TurnEnded(Player player, GameState game) {
        this.player = player;
        this.game = game;
    }

    /**
     * Show the turn ended message
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.showMessage("Your turn has ended!");
        if (view.getClass() == CLI.class) {
            ((CLI) view).setMyTurn(false);
            ((CLI) view).GetCommandWhileNotYourTurn(game ,player);
        }
    }
}
