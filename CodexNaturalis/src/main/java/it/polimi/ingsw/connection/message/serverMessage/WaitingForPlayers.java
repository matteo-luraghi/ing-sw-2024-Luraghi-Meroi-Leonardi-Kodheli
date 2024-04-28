package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

/**
 * WaitingForPlayers class
 * used to notify the players that more players are needed to start the game
 * @author Matteo Leonardo Luraghi
 */
public class WaitingForPlayers extends ServerMessage {
    private static final long serialVersionUID = 4830636966703229889L;

    /**
     * Constructor
     */
    public WaitingForPlayers() {
    }

    /**
     * Shows the waiting for players window in CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowWaitingForPlayers();
    }
}
