package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.view.mainview.View;

/**
 * Winner class
 * notifies all the players about the winner of the game
 * @author Matteo Leonardo Luraghi
 */
public class Winner extends ServerMessage {
    private static final long serialVersionUID = -9101701077881585L;
    private final GameState game;

    /**
     * Constructor, sets the message type as WINNER
     */
    public Winner(GameState game) {
        super(Message.WINNER);
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
