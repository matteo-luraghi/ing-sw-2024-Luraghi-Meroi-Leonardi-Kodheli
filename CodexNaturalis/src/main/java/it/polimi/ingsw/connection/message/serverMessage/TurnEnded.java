package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

/**
 * TurnEnded class
 * used to end the current player's turn
 * @author Matteo Leonardo Luraghi
 */
public class TurnEnded extends ServerMessage {
    private static final long serialVersionUID = 2741292733883597798L;

    /**
     * Constructor
     */
    public TurnEnded() {
    }

    /**
     * Show the turn ended message
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.showMessage("Your turn has ended!");
    }
}
