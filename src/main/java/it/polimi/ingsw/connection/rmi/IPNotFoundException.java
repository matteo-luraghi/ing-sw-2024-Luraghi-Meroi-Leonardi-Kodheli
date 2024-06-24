package it.polimi.ingsw.connection.rmi;

import java.io.Serial;

/**
 * IPNotFoundException class
 * used when unable to find the ip address to start the server/client
 * @author Matteo Leonardo Luraghi
 */
public class IPNotFoundException extends Exception{
    @Serial
    private static final long serialVersionUID = 4778381258123509184L;

    /**
     * Constructor
     * @param errorMessage the message to be displayed
     */
    public IPNotFoundException(String errorMessage) {
        super(errorMessage);
    }

}
