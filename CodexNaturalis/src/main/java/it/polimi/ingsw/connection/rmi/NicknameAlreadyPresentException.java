package it.polimi.ingsw.connection.rmi;

import java.io.Serial;

/**
 * NicknameAlreadyPresentException class
 * @author Matteo Leonardo Luraghi
 */
public class NicknameAlreadyPresentException extends Exception {
    @Serial
    private static final long serialVersionUID = -740366887000447608L;

    /**
     * Constructor, set the error message
     * @param errorMessage the error message
     */
    public NicknameAlreadyPresentException(String errorMessage) {
        super(errorMessage);
    }
}
