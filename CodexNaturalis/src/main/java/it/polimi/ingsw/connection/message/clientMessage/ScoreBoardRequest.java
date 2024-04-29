package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.serverMessage.ScoreBoardResponse;
import it.polimi.ingsw.model.gamelogic.ScoreBoard;

public class ScoreBoardRequest extends ClientMessage{
    private static final long serialVersionUID = 5342085476950311290L;

    public ScoreBoardRequest() {}

    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        connectionHandler.sendMessageClient(new ScoreBoardResponse());
    }
}
