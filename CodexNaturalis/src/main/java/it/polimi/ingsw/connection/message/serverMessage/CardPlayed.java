package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.MegaView;

/**
 * CardPlayed class
 * used to update the player's playerfield
 * @author Matteo Leonardo Luraghi
 */
public class CardPlayed extends ServerMessage {
    private static final long serialVersionUID = 4461150114615086324L;
    private final Player player;

    /**
     * Constructor, sets the message type as CARD_PLAYED
     * @param player player who has just played a card
     */
    public CardPlayed(Player player) {
        super(Message.CARD_PLAYED);
        this.player = player;
    }

    /**
     * Show the updated playerfield via CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(MegaView view) {
        view.ShowPlayerField(player);
    }
}
