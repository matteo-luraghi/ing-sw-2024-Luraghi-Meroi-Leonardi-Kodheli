package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

import java.rmi.RemoteException;

/**
 * DrawCardRequest class
 * used to notify the player that they need to draw
 * @author Matteo Leonardo Luraghi
 */
public class DrawCardRequest extends ServerMessage {
    private static final long serialVersionUID = 4055891453804268070L;

    /**
     * Constructor
     */
    public DrawCardRequest() {
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
