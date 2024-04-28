package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
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
     * Constructor that sets the message type as YOUR_TURN and saves the selected player
     * @param player it's this player's turn
     */
    public YourTurn(Player player, GameState game) {
        super(Message.YOUR_TURN);
        this.player = player;
        this.game = game;
    }

    /**
     * Show the player's hand and zone in the CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowPlayerField(player, player, game);
    }
}
