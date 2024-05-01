package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.serverMessage.TextMessage;
import it.polimi.ingsw.controller.Controller;

/**
 * DrawCardResponse class
 * used to send the controller the chosen card to draw
 * @author Matteo Leonardo Luraghi
 */
public class DrawCardResponse extends ClientMessage {
    private static final long serialVersionUID = 408226450774679976L;
    private final int which;
    private final boolean isGold;

    /**
     * Constructor
     * @param which card to be drawn
     */
    public DrawCardResponse(int which, boolean isGold) {
        this.which = which;
        this.isGold = isGold;
    }

    /**
     * Try to draw, if success go to the next state, otherwise ask for drawing again
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        connectionHandler.getController().drawCard(connectionHandler, which, isGold);
    }
}
