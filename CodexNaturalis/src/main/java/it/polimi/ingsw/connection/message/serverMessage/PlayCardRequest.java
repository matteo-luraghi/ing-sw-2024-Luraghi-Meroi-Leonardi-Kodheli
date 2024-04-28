package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.View;

public class PlayCardRequest extends ServerMessage {
    private static final long serialVersionUID = 4132994003320223706L;
    private final Player player;
    private final GameState game;

    public PlayCardRequest(Player player, GameState game) {
        super(Message.PLAY_CARD_REQUEST);
        this.player = player;
        this.game = game;
    }

    @Override
    public void show(View view) {
        view.ShowPlayerField(player, player, game);
    }
}
