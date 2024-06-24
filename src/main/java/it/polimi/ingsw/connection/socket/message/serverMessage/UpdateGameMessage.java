package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * UpdateGameMessage class
 * used to send the updated game to the players
 * @author Matteo Leonardo Luraghi
 */
public class UpdateGameMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 5383660042861452692L;
    private final GameState game;

    /**
     * Constructor
     * @param game the updated game
     */
    public UpdateGameMessage(GameState game) {
        this.game = game;
    }

    /**
     * Set the updated game in the view
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        new Thread(() -> {
            try {
                view.setGame(game);
            } catch (RemoteException e) {
                System.err.println("Error setting game");
            }
        }).start();
    }
}
