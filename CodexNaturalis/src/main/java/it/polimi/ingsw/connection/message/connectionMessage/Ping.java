package it.polimi.ingsw.connection.message.connectionMessage;

import it.polimi.ingsw.connection.message.Message;

public class Ping extends ConnectionMessage {
    private static final long serialVersionUID = -1366111626343826190L;

    /**
     * Ping message
     */
    public Ping() {
        super(Message.PING);
    }
}
