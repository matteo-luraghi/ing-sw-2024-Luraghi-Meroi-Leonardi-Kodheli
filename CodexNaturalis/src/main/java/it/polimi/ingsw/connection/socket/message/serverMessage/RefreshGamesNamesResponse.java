package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * RefreshGamesNamesResponse class
 * used to set the list of names of available games in the view
 * @author Matteo Leonardo Luraghi
 */
public class RefreshGamesNamesResponse extends ServerMessage{
    @Serial
    private static final long serialVersionUID = -7449258385989902785L;
    private final ArrayList<String> gamesNames;

    /**
     * Constructor
     * @param gamesNames the available games' names
     */
    public RefreshGamesNamesResponse(ArrayList<String> gamesNames) {
        this.gamesNames = gamesNames;
    }

    /**
     * Show the message
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        try {
            view.setGameNames(this.gamesNames);
        } catch (RemoteException e) {
            System.err.println("Error sending games' names");
        }
    }
}
