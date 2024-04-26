package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ClientHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.connection.message.serverMessage.LoginRequest;

/**
 * LoginResponse class
 * used to set the clientHandler nickname or ask for a new nickname
 */
public class LoginResponse extends ClientMessage {
    private static final long serialVersionUID = 6913153965860413805L;
    private final String nickname;

    /**
     * Constructor, sets the message type as LOGIN_RESPONSE
     * @param nickname the user's input for a nickname
     */
    public LoginResponse(String nickname) {
        super(Message.LOGIN_RESPONSE);
        this.nickname = nickname;
    }

    /**
     * Ask for a new nickname or set the nickname and try to add the client handler to a game
     * @param server server to use
     * @param clientHandler client handler
     */
    @Override
    public void execute(Server server, ClientHandler clientHandler) {
        String REGEX = "^([a-zA-Z]\\w{2,10})$";
        if (this.nickname == null || !this.nickname.matches(REGEX)) {
            clientHandler.sendMessageClient(new LoginRequest());
        } else {
            clientHandler.setClientNickname(nickname);
            server.addToGame(clientHandler);
        }
    }
}
