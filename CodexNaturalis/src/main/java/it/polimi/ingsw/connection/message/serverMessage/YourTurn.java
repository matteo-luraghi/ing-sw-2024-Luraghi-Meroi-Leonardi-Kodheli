package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.View;

/**
 * YourTurn class
 * used to tell the player that it's its turn
 * @author Matteo Leonardo Luraghi
 */
public class YourTurn extends ServerMessage {
    private static final long serialVersionUID = -532540375599572593L;
    private final Player player;
    private final GameState game;

    /**
     * Constructor
     * @param player it's this player's turn
     */
    public YourTurn(Player player, GameState game) {
        this.player = player;
        this.game = game;
    }

    /**
     * Show the player's hand and zone in the CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        //Set my turn to true
        view.ShowPlayerField(player, player, game);
    }
}
