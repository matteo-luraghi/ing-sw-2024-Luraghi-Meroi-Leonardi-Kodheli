package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

public class SetPlayerMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 1128446556116587818L;
    private final Player player;

    public SetPlayerMessage(Player player) {
        this.player = player;
    }

    @Override
    public void show(View view) {
        try {
            view.setUser(player);
        } catch (RemoteException e) {
            System.err.println("Error setting the player");
        }
    }
}
