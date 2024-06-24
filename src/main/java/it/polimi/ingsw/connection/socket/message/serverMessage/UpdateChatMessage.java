package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.gamechat.GameChat;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * UpdateChatMessage class
 * used to send the updated chat to the players
 * @author Matteo Leonardo Luraghi
 */
public class UpdateChatMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = 634665160366703255L;
    private final GameChat chat;

    /**
     * Constructor
     * @param chat the updated chat
     */
    public UpdateChatMessage(GameChat chat) {
        this.chat = chat;
    }

    /**
     * Show the chat
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        try {
            view.setGameChat(this.chat);
        } catch (RemoteException e) {
            System.err.println("Error sending updated chat");
        }
    }
}
