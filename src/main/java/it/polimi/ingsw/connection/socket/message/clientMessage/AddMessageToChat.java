package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.model.gamelogic.gamechat.Message;

import java.io.Serial;

/**
 * AddMessageToChat class
 * used to save a new message in the chat
 * @author Matteo Leonardo Luraghi
 */
public class AddMessageToChat extends ClientMessage{
    @Serial
    private static final long serialVersionUID = 4608999502010196270L;
    private final Message message;

    /**
     * Constructor
     * @param message the new message
     */
    public AddMessageToChat(Message message) {
        this.message = message;
    }

    /**
     * Save the new message in the chat
     * @param server            server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        connectionHandler.getController().addMessageToChat(message);
    }
}
