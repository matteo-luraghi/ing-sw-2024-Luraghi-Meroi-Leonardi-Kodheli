package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.gui.GUI;
import it.polimi.ingsw.view.mainview.View;

import java.rmi.RemoteException;

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
        try {
            view.showMessage("You now have to draw a card!");
        } catch (RemoteException e) {
            System.err.println("Error sending message");
        }
            new Thread(() -> {
                try {
                    view.setPlayPhase(false);
                } catch (RemoteException e) {
                    System.err.println("Error sending message");
                }
            }).start();

    }
}
