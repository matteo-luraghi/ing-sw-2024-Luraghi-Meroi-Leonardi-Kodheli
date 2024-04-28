package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
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
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        for(ConnectionHandler c: connectionHandler.getController().getHandlers()) {
            if (c.getClientColor().equals(this.color) && !c.getClientNickname().equals(connectionHandler.getClientNickname())) {
                connectionHandler.sendMessageClient(new TextMessage("Color unavailable, please choose another color"));
                connectionHandler.sendMessageClient(new ColorRequest());
                return;
            }
        }
        connectionHandler.setClientColor(this.color);
        if (!connectionHandler.getController().isGameStarted()) {
            connectionHandler.sendMessageClient(new WaitingForPlayers());
        }
    }
}
