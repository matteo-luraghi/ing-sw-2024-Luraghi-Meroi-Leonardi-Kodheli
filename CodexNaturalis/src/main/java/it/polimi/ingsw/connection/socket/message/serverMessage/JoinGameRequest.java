package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.util.ArrayList;

/**
 * JoinGameRequest class
 * used to make the user choose to join or create a game
 * @author Matteo Leonardo Luraghi
 */
public class JoinGameRequest extends ServerMessage{
    @Serial
    private static final long serialVersionUID = 6927640204965936037L;
    private final ArrayList<String> gameNames;

    /**
     * Constructor
     * @param gameNames the list of the active games' names
     */
    public JoinGameRequest(ArrayList<String> gameNames) {
        super();
        this.gameNames = gameNames;
    }

    /**
     * show the message in the CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.showJoinOrCreate(this.gameNames);
    }
}
