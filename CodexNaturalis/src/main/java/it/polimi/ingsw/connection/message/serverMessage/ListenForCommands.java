package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

public class ListenForCommands extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 665505482784672425L;

    public ListenForCommands() {}

    @Override
    public void show(View view) {
        if (view.getClass() == CLI.class) {
            ((CLI) view).getCommands();
        }
    }
}
