package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ClientHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.connection.message.serverMessage.DrawCardRequest;
import it.polimi.ingsw.connection.message.serverMessage.TextMessage;
import it.polimi.ingsw.connection.message.serverMessage.TurnEnded;
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
     * Constructor, sets the message type as DRAW_CARD_RESPONSE
     * @param which card to be drawn
     */
    public DrawCardResponse(int which) {
        super(Message.DRAW_CARD_RESPONSE);
        this.which = which;
    }

    /**
     * Try to draw, if success go to the next state, otherwise ask for drawing again
     * @param server server to use
     * @param clientHandler client handler
     */
    @Override
    public void execute(Server server, ClientHandler clientHandler) {
        Controller controller = clientHandler.getController();
        if (controller.drawCard(which)) {
            controller.setTurnState(ENDED);
            controller.setState(NEXT_PLAYER);
            clientHandler.sendMessageClient(new TurnEnded());
        } else {
            clientHandler.sendMessageClient(new TextMessage("Unable to draw the card, try again"));
            clientHandler.sendMessageClient(new DrawCardRequest());
        }
    }
}
