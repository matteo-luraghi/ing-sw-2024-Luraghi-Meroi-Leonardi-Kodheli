package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * PlayStartingCardRequest class
 * used to ask the player which side they want their startingCard to be played
 * @author Gabriel Leonardi
 */
public class PlayStartingCardRequest extends ServerMessage {
    @Serial
    private static final long serialVersionUID = -535519638608657463L;
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
        try {
            view.ChooseStartingCardSide(card);
        } catch (RemoteException e) {
            System.err.println("Error choosing starting card side");
        }
    }
}
