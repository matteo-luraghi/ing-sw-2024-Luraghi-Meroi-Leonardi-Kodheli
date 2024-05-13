package it.polimi.ingsw.connection;

import java.io.Serial;

public class ConnectionClosedException extends Exception{

    @Serial
    private static final long serialVersionUID = -7321881596842292109L;
    public ConnectionClosedException(String message) {
        super(message);
    }
}
