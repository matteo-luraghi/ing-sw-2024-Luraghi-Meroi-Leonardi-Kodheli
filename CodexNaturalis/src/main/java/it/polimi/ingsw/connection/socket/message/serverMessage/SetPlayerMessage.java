package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

public class SetPlayerMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 1128446556116587818L;
    private final Player player;

    public SetPlayerMessage(Player player) {
        this.player = player;
    }

    @Override
    public void show(View view) {
        if (view.getClass() == CLI.class) {
            ((CLI) view).setUser(player);
        }
    }
}
