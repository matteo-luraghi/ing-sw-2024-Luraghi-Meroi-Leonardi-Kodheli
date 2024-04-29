package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

/**
 * LoginRequest class
 * used to make the user select a nickname
 * @author Matteo Leonardo Luraghi
 */
public class LoginRequest extends ServerMessage {
    private static final long serialVersionUID = -4505766063795951757L;

    /**
     * Constructor
     */
    public LoginRequest() {
    }

    /**
     * Shows the login request via CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.insertNickname();
    }
}
