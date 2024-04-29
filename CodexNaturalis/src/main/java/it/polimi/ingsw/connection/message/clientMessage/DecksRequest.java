package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.serverMessage.DecksResponse;
import it.polimi.ingsw.model.gamelogic.Deck;

public class DecksRequest extends ClientMessage {
    private static final long serialVersionUID = -5560924019122711349L;

    public DecksRequest() {
    }

    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        connectionHandler.sendMessageClient(new DecksResponse(connectionHandler.getController().getGame()));
    }
}
