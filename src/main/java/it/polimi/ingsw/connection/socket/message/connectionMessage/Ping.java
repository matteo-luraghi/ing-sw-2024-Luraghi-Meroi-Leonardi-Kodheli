package it.polimi.ingsw.connection.socket.message.connectionMessage;

import java.io.Serial;
import java.io.Serializable;

/**
 * Ping class
 * used to keep alive the socket connection
 * @author Matteo Leonardo Luraghi
 */
public class Ping  implements Serializable {
    @Serial
    private static final long serialVersionUID = -1366111626343826190L;

    /**
     * Ping message
     */
    public Ping() {
    }
}
