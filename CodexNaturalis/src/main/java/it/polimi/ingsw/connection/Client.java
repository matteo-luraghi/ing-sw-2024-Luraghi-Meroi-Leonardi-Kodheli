package it.polimi.ingsw.connection;

import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.Coordinates;
import it.polimi.ingsw.view.mainview.View;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Client class
 * used to manage the connection and communication of a client
 * @author Matteo Leonardo Luraghi
 */
public abstract class Client {
    private final View view;
    private final AtomicBoolean connected = new AtomicBoolean();

    /**
     * Constructor, sets the view interface and sets the connected value as true
     * @param view View interface
     */
    public Client(View view) {
        this.view = view;
        this.connected.set(true);
    }

    /**
     * connected getter
     * @return the value of connected
     */
    public boolean getConnected() {
        return connected.get();
    }

    /**
     * connected setter
     * @param value the new value of connected
     */
    public void setConnected(boolean value) {
        this.connected.set(value);
    }

    /**
     * view getter
     * @return the view interface
     */
    public View getView() {
        return this.view;
    }

    /**
     * Make the user choose to join or create a game
     * @param isJoin true if the user wants to join a game
     * @param gameName game name
     * @param nickname user nickname
     */
    public void gameChoice(boolean isJoin, String gameName, String nickname) throws Exception {};

    /**
     * Send the selected color to the server
     * @param color the color
     */
    public void colorResponse(Color color) {}

    /**
     * Send the number of players to the server
     * @param number the number of players
     */
    public void playersNumberResponse(int number, String gameName) {}

    /**
     * Send the starting card on the correct side
     * @param card the starting card
     * @param isFront the side
     */
    public void playStartingCardResponse(StartingCard card, boolean isFront) {}

    /**
     * Send the selected goal card to the server
     * @param card the goal card
     */
    public void goalCardResponse(GoalCard card) {}

    /**
     * Send the server a message to ensure the client is aware it's its player's turn
     */
    public void yourTurnOk() {}

    /**
     * Send the selected card to be played, the coordinates and the side
     * @param card the card
     * @param where the coordinates
     * @param isFront the side
     */
    public void playCardResponse(ResourceCard card, Coordinates where, boolean isFront) {}

    /**
     * Send the reference of which card to draw
     * @param which the card
     * @param isGold which deck to draw from
     */
    public void drawCardResponse(int which, boolean isGold) {}

    /**
     * Get the available games from the server
     */
    public abstract void refreshGamesNames();

    /**
     * Disconnect the client
     */
    public void disconnect() {}
}
