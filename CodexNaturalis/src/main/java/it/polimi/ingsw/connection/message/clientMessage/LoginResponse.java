package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.SocketConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.serverMessage.LoginRequest;
import it.polimi.ingsw.connection.message.serverMessage.TextMessage;

import java.io.Serial;

/**
 * LoginResponse class
 * used to set the clientHandler nickname or ask for a new nickname
 */
public class LoginResponse extends ClientMessage {
    @Serial
    private static final long serialVersionUID = 6913153965860413805L;
    private final String nickname;

    /**
     * Constructor
     * @param nickname the user's input for a nickname
     */
    public LoginResponse(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Ask for a new nickname or set the nickname and try to add the client handler to a game
     * @param server server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        if (this.nickname == null || this.nickname.isEmpty()) {
            connectionHandler.sendMessageClient(new LoginRequest());
        } else if(!server.checkUniqueNickname(nickname)){
            connectionHandler.sendMessageClient(new TextMessage("Username already present"));
            connectionHandler.sendMessageClient(new LoginRequest());
        }else {
            connectionHandler.setClientNickname(nickname);
            server.addToGame(connectionHandler);
        }
    }
}
