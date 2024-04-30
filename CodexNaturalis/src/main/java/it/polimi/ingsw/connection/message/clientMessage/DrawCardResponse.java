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

    /**
     * Constructor
     * @param which card to be drawn
     */
    public DrawCardResponse(int which) {
        this.which = which;
    }

    /**
     * Try to draw, if success go to the next state, otherwise ask for drawing again
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        Controller controller = connectionHandler.getController();
        if (controller.drawCard(which)) {
            // set the controller state to next player's turn
            controller.changeTurnState();
        } else {
            connectionHandler.sendMessageClient(new TextMessage("Unable to draw the card, try again"));
            // set the controller state to draw card
            controller.drawCardState();
        }
    }
}
