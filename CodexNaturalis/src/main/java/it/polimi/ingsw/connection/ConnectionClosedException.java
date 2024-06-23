package it.polimi.ingsw.connection;

import java.io.Serial;

/**
 * ConnectionClosedException class
 * used to notify the client of connection errors
 * @author Matteo Leonardo Luraghi
 */
public class ConnectionClosedException extends Exception{
    @Serial
    private static final long serialVersionUID = -7321881596842292109L;

    /**
     * Constructor, set the error message
     * @param message the error message
     */
    public ConnectionClosedException(String message) {
        super(message);
    }
}
