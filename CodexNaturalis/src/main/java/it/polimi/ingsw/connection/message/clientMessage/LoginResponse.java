package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ClientHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.connection.message.serverMessage.LoginRequest;

public class LoginResponse extends ClientMessage {
    private static final long serialVersionUID = 3624677593154184852L;
    private String nickname;

    public LoginResponse(String nickname) {
        super(Message.LOGIN_RESPONSE);
        this.nickname = nickname;
    }

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
