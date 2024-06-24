package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

/**
 * ListenForCommands class
 * make the player be able to use commands
 * @author Matteo Leonardo Luraghi
 */
public class ListenForCommands extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 665505482784672425L;

    /**
     * Constructor
     */
    public ListenForCommands() {}

    /**
     * Start a thread to get the player's commands
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        if (view.getClass() == CLI.class) {
            new Thread(() -> ((CLI) view).getCommands()).start();
        }
    }
}
