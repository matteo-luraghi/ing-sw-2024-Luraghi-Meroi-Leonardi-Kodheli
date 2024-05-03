package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

/**
 * PlayStartingCardRequest class
 * used to ask the player which side they want their startingCard to be played
 * @author Gabriel Leonardi
 */
public class PlayStartingCardRequest extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 6899284951355020884L;
    private final StartingCard card;
    /**
     * Constructor
     * @param card The starting card the player chose
     */
    public PlayStartingCardRequest(StartingCard card) {
        this.card = card;
    }

    /**
     * Shows the request via CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ChooseStartingCardSide(card);
    }
}
