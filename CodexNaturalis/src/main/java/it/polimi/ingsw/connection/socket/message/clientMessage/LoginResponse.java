package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.socket.message.serverMessage.LoginRequest;
import it.polimi.ingsw.connection.socket.message.serverMessage.TextMessage;

import java.io.Serial;

/**
 * LoginResponse class
 * used to set the clientHandler nickname or ask for a new nickname
 */
public class LoginResponse extends ClientMessage {
    @Serial
    private static final long serialVersionUID = 6913153965860413805L;
    private final String nickname;
    private final boolean isJoin;
    private final String gameName;

    /**
     * Constructor
     * @param nickname the user's input for a nickname
     */
    public LoginResponse(boolean isJoin, String gameName, String nickname) {
        this.isJoin = isJoin;
        this.gameName = gameName;
        this.nickname = nickname;
    }

    /**
     * Ask for a new nickname or set the nickname and try to add the client handler to a game
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        if(!server.checkUniqueNickname(this.nickname)){
            connectionHandler.sendMessageClient(new TextMessage("Username already present"));
            connectionHandler.sendMessageClient(new LoginRequest(this.isJoin, this.gameName));
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
