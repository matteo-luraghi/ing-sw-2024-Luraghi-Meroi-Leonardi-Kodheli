package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.MegaView;

/**
 * LoginRequest class
 * @author Matteo Leonardo Luraghi
 */
public class LoginRequest extends ServerMessage {
    private static final long serialVersionUID = 6670755359860541301L;

    /**
     * constructor
     */
    public LoginRequest() {
        super(Message.LOGIN_REQUEST);
    }

    /**
     * Shows the login request via CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(MegaView view) {
        view.ShowLogin();
    }
}
