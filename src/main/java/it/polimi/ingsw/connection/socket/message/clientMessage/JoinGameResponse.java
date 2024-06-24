package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.socket.message.serverMessage.JoinGameRequest;
import it.polimi.ingsw.connection.socket.message.serverMessage.TextMessage;

import java.io.Serial;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * JoinGameResponse class
 * used to add the player to the selected game or create a new game
 * @author Matteo Leonardo Luraghi
 */
public class JoinGameResponse extends ClientMessage{
    @Serial
    private static final long serialVersionUID = 3108231749238138153L;
    private final boolean isJoin;
    private final String gameName;
    private final String nickname;

    /**
     * Constructor
     * @param isJoin select the join or create option
     * @param gameName the game's name
     * @param nickname the player's nickname
     */
    public JoinGameResponse(boolean isJoin, String gameName, String nickname) {
        this.isJoin = isJoin;
        this.gameName = gameName;
        this.nickname = nickname;
    }

    /**
     * Add the player to the selected game or create a new game
     * @param server            server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        if(!server.checkUniqueNickname(this.nickname)){
            connectionHandler.sendMessageClient(new TextMessage("Username already present"));
            ArrayList<String> gameNames = null;
            try {
                gameNames = server.getGamesNames();
            } catch (RemoteException e) {
                System.err.println("Error getting games names");
            }
            connectionHandler.sendMessageClient(new JoinGameRequest(gameNames));
        }else {
            connectionHandler.setClientNickname(nickname);
            if (this.isJoin) {
                server.joinGame(connectionHandler, this.gameName);
            } else {
                connectionHandler.playersNumberRequest();
            }
        }
    }
}
