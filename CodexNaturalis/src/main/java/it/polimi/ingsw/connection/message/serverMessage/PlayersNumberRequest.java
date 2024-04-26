package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.View;

/**
 * PlayerNumberRequest class
 * used to ask the player how many players will be playing the game
 * @author Matteo Leonardo Luraghi
 */
public class PlayersNumberRequest extends ServerMessage {
    private static final long serialVersionUID = 6899284951355020884L;

    /**
     * Constructor, sets the message type as PLAYERS_NUMBER_REQUEST
     */
    public PlayersNumberRequest() {
        super(Message.PLAYERS_NUMBER_REQUEST);
    }

    /**
     * Shows the request via CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.askPlayersNumber();
    }
}
