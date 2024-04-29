package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.View;

public class PlayerFieldResponse extends ServerMessage {
    private static final long serialVersionUID = -8477163459835257932L;
    private final Player playerToSee;
    private final Player playerAsking;
    private final GameState game;

    public PlayerFieldResponse(Player playerToSee, Player playerAsking, GameState game) {
        this.playerToSee = playerToSee;
        this.playerAsking = playerAsking;
        this.game = game;
    }

    @Override
    public void show(View view) {
        view.ShowPlayerField(playerToSee, playerAsking, game);
    }
}
