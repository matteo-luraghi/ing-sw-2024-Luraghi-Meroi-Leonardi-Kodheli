package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

/**
 * PlayCardRequest class
 * used to ask a client to play a card
 * @author Matteo Leonardo Luraghi
 */
public class PlayCardRequest extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 4132994003320223706L;
    private final Player player;
    private final GameState game;

    /**
     * Constructor
     * @param player player that has to play a card
     * @param game game of the player
     */
    public PlayCardRequest(Player player, GameState game) {
        this.player = player;
        this.game = game;
    }

    /**
     * Show the player's playerfield and starts listening for view commands
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowPlayerField(player, player, game);
        if (view.getClass() == CLI.class) {
            ((CLI) view).GetCommandInPlayState(game, player);
        }
    }
}
