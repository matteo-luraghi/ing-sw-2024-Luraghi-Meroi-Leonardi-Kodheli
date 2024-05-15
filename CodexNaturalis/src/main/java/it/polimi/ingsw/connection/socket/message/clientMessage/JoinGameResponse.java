package it.polimi.ingsw.connection.socket.message.clientMessage;

import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.socket.message.serverMessage.JoinGameRequest;
import it.polimi.ingsw.connection.socket.message.serverMessage.TextMessage;
import it.polimi.ingsw.controller.Controller;

import java.io.Serial;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class JoinGameResponse extends ClientMessage{
    @Serial
    private static final long serialVersionUID = 3108231749238138153L;
    private final boolean isJoin;
    private final String gameName;
    private final String nickname;

    public JoinGameResponse(boolean isJoin, String gameName, String nickname) {
        this.isJoin = isJoin;
        this.gameName = gameName;
        this.nickname = nickname;
    }

    /**
     * Execute the specific action based on message
     *
     * @param server            server to use
     * @param connectionHandler client handler
     */
    @Override
    public void execute(Server server, SocketConnectionHandler connectionHandler) {
        if(!server.checkUniqueNickname(this.nickname)){
            connectionHandler.sendMessageClient(new TextMessage("Username already present"));
            ArrayList<String> gameNames = (ArrayList<String>) server.getGames().stream()
                    .filter(c -> !c.isGameStarted())
                    .map(Controller::getGameName)
                    .collect(Collectors.toList());
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
