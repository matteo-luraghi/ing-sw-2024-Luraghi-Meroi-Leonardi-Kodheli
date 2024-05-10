package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.view.cli.CLI;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

public class UpdateGameMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 5383660042861452692L;
    private final GameState game;

    public UpdateGameMessage(GameState game) {
        this.game = game;
    }

    @Override
    public void show(View view) {
        if(view.getClass() == CLI.class) {
            new Thread(() -> ((CLI) view).setGame(game)).start();
        }
    }
}
