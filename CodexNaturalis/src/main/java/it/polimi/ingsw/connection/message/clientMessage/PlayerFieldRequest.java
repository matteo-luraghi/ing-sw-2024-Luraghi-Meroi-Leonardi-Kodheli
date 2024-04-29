package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.serverMessage.PlayerFieldResponse;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;

public class PlayerFieldRequest extends ClientMessage{
    private static final long serialVersionUID = -2636450715519879863L;
    private final Player playerToSee;
    private final Player playerAsking;

    public PlayerFieldRequest(Player playerToSee, Player playerAsking) {
        this.playerToSee = playerToSee;
        this.playerAsking = playerAsking;
    }

    @Override
    public void execute(Server server, ConnectionHandler connectionHandler) {
        connectionHandler.sendMessageClient(new PlayerFieldResponse(playerToSee, playerAsking, connectionHandler.getController().getGame()));
    }
}
