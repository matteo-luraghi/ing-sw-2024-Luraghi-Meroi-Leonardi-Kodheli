package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ClientHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.connection.message.serverMessage.ColorRequest;
import it.polimi.ingsw.connection.message.serverMessage.TextMessage;
import it.polimi.ingsw.connection.message.serverMessage.WaitingForPlayers;
import it.polimi.ingsw.model.gamelogic.Color;

/**
 * ColorResponse class
 * used to tell the server the chosen color
 * @author Matteo Leonardo Luraghi
 */
public class ColorResponse extends ClientMessage{
    private static final long serialVersionUID = 2193088654980431024L;
    private final Color color;

    /**
     * Constructor, sets the message typ as COLOR_RESPONSE and the color
     * @param color the chosen color
     */
    public ColorResponse(Color color) {
        super(Message.COLOR_RESPONSE);
        this.color = color;
    }

    /**
     * Check if the selected color is not already chosen by another player
     * @param server server to use
     * @param clientHandler client handler
     */
    @Override
    public void execute(Server server, ClientHandler clientHandler) {
        for(ClientHandler c: clientHandler.getController().getHandlers()) {
            if (c.getClientColor().equals(this.color) && !c.getClientNickname().equals(clientHandler.getClientNickname())) {
                clientHandler.sendMessageClient(new TextMessage("Color unavailable, please choose another color"));
                clientHandler.sendMessageClient(new ColorRequest());
                return;
            }
        }
        clientHandler.setClientColor(this.color);
        if (!clientHandler.getController().isGameStarted()) {
            clientHandler.sendMessageClient(new WaitingForPlayers());
        }
    }
}
