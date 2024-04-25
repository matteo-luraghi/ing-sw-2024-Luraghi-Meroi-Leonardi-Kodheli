package it.polimi.ingsw.connection.message.clientMessage;

import it.polimi.ingsw.connection.ClientHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.connection.message.serverMessage.WaitingForPlayers;

public class PlayersNumberResponse extends ClientMessage{
    private static final long serialVersionUID = -7752965836181499280L;
    private int numPlayers;

    public PlayersNumberResponse(int numPlayers) {
        super(Message.PLAYERS_NUMBER_RESPONSE);
        this.numPlayers = numPlayers;
    }

    @Override
    public void execute(Server server, ClientHandler clientHandler) {
        server.addToGame(clientHandler, this.numPlayers);
        clientHandler.sendMessageClient(new WaitingForPlayers());
    }
}
