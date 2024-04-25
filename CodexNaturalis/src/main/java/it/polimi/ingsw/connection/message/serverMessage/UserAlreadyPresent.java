package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.View;

/**
 * UserAlreadyPresent class
 * used as a message to ask for a new nickname
 * @author Matteo Leonardo Luraghi
 */
public class UserAlreadyPresent extends ServerMessage {
    private static final long serialVersionUID = -5600698207138090264L;

    /**
     * Constructor thad assigns the message type as USER_ALREADY_PRESENT
     */
    public UserAlreadyPresent() {
        super(Message.USER_ALREADY_PRESENT);
    }

    /**
     * Show error in CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowUserAlreadyPresent();
        view.ShowLogin();
    }
}
