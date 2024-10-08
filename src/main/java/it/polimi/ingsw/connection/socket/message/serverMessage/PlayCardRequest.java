package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * PlayCardRequest class
 * used to ask a client to play a card
 * @author Matteo Leonardo Luraghi
 */
public class PlayCardRequest extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 4132994003320223706L;
    private final Player player;

    /**
     * Constructor
     * @param player player that has to play a card
     */
    public PlayCardRequest(Player player) {
        this.player = player;
    }

    /**
     * Show the player's playerfield and starts listening for view commands
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        if (view.getClass() == CLI.class) {
            new Thread(() -> {
                try {
                    view.showMessage("Play a card!");
                } catch (RemoteException e) {
                    System.err.println("Error sending message");
                }
                ((CLI) view).setPlayPhase(true);
            }).start();
        }
    }
}
