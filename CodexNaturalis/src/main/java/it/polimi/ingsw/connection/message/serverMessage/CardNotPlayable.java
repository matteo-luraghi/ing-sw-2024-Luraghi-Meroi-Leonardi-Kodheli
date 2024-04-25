package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.View;

/**
 * CardNotPlayable class
 * used to notify the player that they need to play another card in a valid position
 * @author Matteo Leonardo Luraghi
 */
public class CardNotPlayable extends ServerMessage {
    private static final long serialVersionUID = 4873171479370449853L;
    private final Player player;

    /**
     * Constructor, sets the message type as CARD_NOT_PLAYABLE
     * and the player as the one who will try to play again
     */
    public CardNotPlayable(Player player) {
        super(Message.CARD_NOT_PLAYABLE);
        this.player = player;
    }

    /**
     * Show the player the error and its playerzone
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.showMessage("Error: the card can't be played there!");
        view.ShowPlayerField(player);
    }
}
