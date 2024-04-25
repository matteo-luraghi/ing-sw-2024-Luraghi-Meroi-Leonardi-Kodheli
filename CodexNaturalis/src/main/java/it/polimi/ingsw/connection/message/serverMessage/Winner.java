package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.MegaView;

/**
 * Winner class
 * notifies all the players about the winner of the game
 * @author Matteo Leonardo Luraghi
 */
public class Winner extends ServerMessage {
    private static final long serialVersionUID = -8138532488978566491L;

    /**
     * Constructor, sets the message type as WINNER
     */
    public Winner() {
        super(Message.WINNER);
    }

    /**
     * Shows the winner in CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(MegaView view) {
        view.ShowWinner();
    }
}
