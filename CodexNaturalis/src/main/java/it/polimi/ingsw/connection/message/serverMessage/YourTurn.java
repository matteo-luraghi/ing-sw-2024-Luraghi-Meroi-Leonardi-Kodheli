package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

/**
 * YourTurn class
 * used to tell the player that it's its turn
 * @author Matteo Leonardo Luraghi
 */
public class YourTurn extends ServerMessage {
    private static final long serialVersionUID = -532540375599572593L;
    /**
     * Constructor
     */
    public YourTurn() {
    }

    /**
     * Sets the view's private variable yourTurn to true
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        if(view.getClass() == CLI.class) {
            ((CLI) view).setMyTurn(true);
        }
    }
}
