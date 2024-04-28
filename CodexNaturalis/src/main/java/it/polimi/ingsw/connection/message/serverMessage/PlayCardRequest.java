package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.View;

/**
 * PlayCardRequest class
 * used to ask a client to play a card
 * @author Matteo Leonardo Luraghi
 */
public class PlayCardRequest extends ServerMessage {
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
     * Show the player's playerfield to make them choose which card to play and where
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowPlayerField(player, player, game);
    }
}
