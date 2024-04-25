package it.polimi.ingsw.connection.message.connectionMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.MegaView;

/**
 * Disconnection class
 * @author Matteo Leonardo Luraghi
 */
public class Disconnection extends ConnectionMessage {
    private static final long serialVersionUID = 3339787176056507046L;
    private final String clientNickname;

    /**
     * constructor
     * @param clientNickname nickname of the disconnected player
     */
    public Disconnection(String clientNickname) {
        super(Message.DISCONNECTION);
        this.clientNickname = clientNickname;
    }

    /**
     * show the message
     * @param view view interface
     */
    public void show(MegaView view) {
        view.displayDisconnectionMessage(clientNickname);
    }
}
