package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.socket.message.serverMessage.RefreshGamesNamesResponse;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * RefreshGamesNamesRequest class
 * used to get the updated list of names of available games
 * @author Matteo Leonardo Luraghi
 */
public class RefreshGamesNamesRequest extends ClientMessage{
    @Serial
    private static final long serialVersionUID = 512033551693565894L;

    /**
     * Get the names of the games from the server and send them to the client
     * @param server            server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        ArrayList<String> gamesNames;
        try {
             gamesNames = server.getGamesNames();
        } catch (RemoteException e) {
            gamesNames = null;
        }
        connectionHandler.sendMessageClient(new RefreshGamesNamesResponse(gamesNames));
    }
}
