package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.view.mainview.View;

public class DecksResponse extends ServerMessage {
    private static final long serialVersionUID = -7310367604146508721L;
    private final GameState game;

    public DecksResponse(GameState game) {
        this.game = game;
    }

    @Override
    public void show(View view) {
        view.ShowDecks(game);
    }
}
