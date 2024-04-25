package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.View;

/**
 * TurnEnded class
 * used to end the current player's turn
 * @author Matteo Leonardo Luraghi
 */
public class TurnEnded extends ServerMessage {
    private static final long serialVersionUID = -350188259113524461L;

    /**
     * Constructor, sets the message type as TURN_ENDED
     */
    public TurnEnded() {
        super(Message.TURN_ENDED);
    }

    /**
     * Show the turn ended message
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowEndTurn();
    }
}
