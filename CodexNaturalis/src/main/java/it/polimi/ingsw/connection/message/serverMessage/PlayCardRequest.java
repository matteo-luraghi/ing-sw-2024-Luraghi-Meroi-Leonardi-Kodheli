package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.View;

public class PlayCardRequest extends ServerMessage {
    private static final long serialVersionUID = 4132994003320223706L;
    private Player player;

    public PlayCardRequest(Player player) {
        super(Message.PLAY_CARD_REQUEST);
        this.player = player;
    }

    @Override
    public void show(View view) {
        view.ShowPlayerField(player);
    }
}
