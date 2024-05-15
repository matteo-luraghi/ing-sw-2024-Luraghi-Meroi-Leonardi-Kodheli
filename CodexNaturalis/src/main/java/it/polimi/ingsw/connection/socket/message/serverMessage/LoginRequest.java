package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * LoginRequest class
 * used to make the user select a nickname
 * @author Matteo Leonardo Luraghi
 */
public class LoginRequest extends ServerMessage {
    @Serial
    private static final long serialVersionUID = -4505766063795951757L;
    private final boolean isJoin;
    private final String gameName;

    /**
     * Constructor
     */
    public LoginRequest(boolean isJoin, String gameName) {
        this.isJoin = isJoin;
        this.gameName = gameName;
    }

    /**
     * Shows the login request via CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        try {
            view.insertNickname(this.isJoin, this.gameName);
        } catch (RemoteException e) {
            System.err.println("Error asking for nickname");
        }
    }
}
