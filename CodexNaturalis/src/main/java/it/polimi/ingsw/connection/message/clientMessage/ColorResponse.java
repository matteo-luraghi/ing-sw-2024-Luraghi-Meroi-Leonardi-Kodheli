package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.SocketConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.model.gamelogic.Color;

import java.io.Serial;

/**
 * ColorResponse class
 * used to tell the server the chosen color
 * @author Matteo Leonardo Luraghi
 */
public class ColorResponse extends ClientMessage{
    @Serial
    private static final long serialVersionUID = 2193088654980431024L;
    private final Color color;

    /**
     * Constructor
     * @param color the chosen color
     */
    public ColorResponse(Color color) {
        this.color = color;
    }

    /**
     * Check if the selected color is not already chosen by another player
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        connectionHandler.getController().setColor(connectionHandler, color);
    }
}
