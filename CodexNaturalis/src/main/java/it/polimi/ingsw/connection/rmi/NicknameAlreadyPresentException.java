package it.polimi.ingsw.connection.rmi;

import java.io.Serial;

/**
 * NicknameAlreadyPresentException class
 * used to notify the user that the nickname they have chosen has already been taken
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
