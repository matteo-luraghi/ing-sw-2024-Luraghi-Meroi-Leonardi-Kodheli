package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.MegaView;

/**
 * WaitingForPlayers class
 * @author Matteo Leonardo Luraghi
 */
public class WaitingForPlayers extends ServerMessage {
    private static final long serialVersionUID = -2339838021795743307L;

    /**
     * constructor
     */
    public WaitingForPlayers() {
        super(Message.WAITING_FOR_PLAYERS);
    }

    /**
     * Shows the waiting for players window in CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(MegaView view) {
        view.ShowWaitingForPlayers();
    }
}
