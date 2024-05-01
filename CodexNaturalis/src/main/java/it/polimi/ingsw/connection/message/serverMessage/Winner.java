package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

/**
 * Winner class
 * notifies all the players about the winner of the game
 * @author Matteo Leonardo Luraghi
 */
public class Winner extends ServerMessage {
    @Serial
    private static final long serialVersionUID = -9101701077881585L;
    private final GameState game;

    /**
     * Constructor
     */
    public Winner(GameState game) {
        this.game = game;
    }

    /**
     * Shows the winner in CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowWinner(this.game);
    }
}
