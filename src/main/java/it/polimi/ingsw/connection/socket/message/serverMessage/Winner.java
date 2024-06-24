package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * Winner class
 * notifies all the players about the winner of the game
 * @author Matteo Leonardo Luraghi
 */
public class Winner extends ServerMessage {
    @Serial
    private static final long serialVersionUID = -9101701077881585L;
    private final GameState game;

    /**
     * Constructor
     * @param game the game
     */
    public Winner(GameState game) {
        this.game = game;
    }

    /**
     * Show the winner
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        try {
            view.ShowWinner(this.game);
        } catch (RemoteException e) {
            System.err.println("Error showing winner");
        }
    }
}
