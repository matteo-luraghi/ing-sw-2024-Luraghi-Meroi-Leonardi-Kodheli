package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.View;

/**
 * PickACard class
 * used to notify the player that they need to draw
 * @author Matteo Leonardo Luraghi
 */
public class PickACard extends ServerMessage {
    private static final long serialVersionUID = 4055891453804268070L;

    /**
     * Constructor, sets the message type as PICK_A_CARD
     */
    public PickACard() {
        super(Message.PICK_A_CARD);
    }

    /**
     * Show the decks to the player in the CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowDecks();
    }
}
