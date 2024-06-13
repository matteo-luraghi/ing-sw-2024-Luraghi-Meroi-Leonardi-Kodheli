package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * SetPlayerMessage class
 * used to set the player in the view
 * @author Matteo Leonardo Luraghi
 */
public class SetPlayerMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 1128446556116587818L;
    private final Player player;

    /**
     * Constructor
     * @param player the player to be set
     */
    public SetPlayerMessage(Player player) {
        this.player = player;
    }

    /**
     * Set the player in the view
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        try {
            view.setUser(player);
        } catch (RemoteException e) {
            System.err.println("Error setting the player");
        }
    }
}
