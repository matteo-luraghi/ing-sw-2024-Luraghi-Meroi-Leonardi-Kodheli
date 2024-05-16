package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

public class UpdateGameMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 5383660042861452692L;
    private final GameState game;

    public UpdateGameMessage(GameState game) {
        this.game = game;
    }

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
