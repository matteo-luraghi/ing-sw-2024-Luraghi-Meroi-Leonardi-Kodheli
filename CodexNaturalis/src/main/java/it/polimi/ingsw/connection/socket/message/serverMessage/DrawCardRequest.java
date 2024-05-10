package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

/**
 * DrawCardRequest class
 * used to notify the player that they need to draw
 * @author Matteo Leonardo Luraghi
 */
public class DrawCardRequest extends ServerMessage {
    private static final long serialVersionUID = 4055891453804268070L;
    private final Player player;

    /**
     * Constructor, sets the message type as PICK_A_CARD
     */
    public DrawCardRequest(Player player) {
        this.player = player;
    }

    /**
     * Show the decks to the player in the CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.showMessage("You now have to draw a card!");
        if (view.getClass() == CLI.class) {
            new Thread(() -> ((CLI) view).setPlayPhase(false)).start();
        }
    }
}
