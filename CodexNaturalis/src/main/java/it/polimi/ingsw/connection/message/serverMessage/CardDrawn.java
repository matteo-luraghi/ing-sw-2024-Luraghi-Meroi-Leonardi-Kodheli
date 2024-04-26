package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.View;

/**
 * CardDrawn class
 * used to notify the player that the card they selected has been added to their hand
 * @author Matteo Leonardo Luraghi
 */
public class CardDrawn extends ServerMessage {
    private static final long serialVersionUID = 360746976821865126L;
    private final Player player;

    /**
     * Constructor, sets the message type as CARD_DRAWN
     * @param player the player who just drew a card
     */
    public CardDrawn(Player player) {
        super(Message.CARD_DRAWN);
        this.player = player;
    }

    /**
     * Show the playerfield with the hand updated with the card drawn
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowPlayerField(player);
    }
}
