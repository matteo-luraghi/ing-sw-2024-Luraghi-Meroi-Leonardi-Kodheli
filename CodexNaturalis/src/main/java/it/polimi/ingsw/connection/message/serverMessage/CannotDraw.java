package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.MegaView;

/**
 * CannotDraw class
 * used to notify the user that they can't draw the selected card
 * @author Matteo Leonardo Luraghi
 */
public class CannotDraw extends ServerMessage {
    private static final long serialVersionUID = 7060318590315655178L;

    /**
     * Constructor, sets the message type as CANNOT_DRAW
     */
    public CannotDraw() {
        super(Message.CANNOT_DRAW);
    }

    /**
     * Show the player the error message and make them try to draw again
     * @param view the view interface
     */
    @Override
    public void show(MegaView view) {
        view.ShowDecks();
    }
}