package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.view.mainview.View;

/**
 * DrawCardRequest class
 * used to notify the player that they need to draw
 * @author Matteo Leonardo Luraghi
 */
public class DrawCardRequest extends ServerMessage {
    private static final long serialVersionUID = 4055891453804268070L;
    private final GameState game;

    /**
     * Constructor, sets the message type as PICK_A_CARD
     */
    public DrawCardRequest(GameState game) {
        this.game = game;
    }

    /**
     * Show the decks to the player in the CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowDecks(this.game);
    }
}
