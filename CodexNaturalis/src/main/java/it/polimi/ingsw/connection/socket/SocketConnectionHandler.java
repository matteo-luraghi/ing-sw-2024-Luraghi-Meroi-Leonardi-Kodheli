package it.polimi.ingsw.connection.socket;

import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.socket.message.clientMessage.ClientMessage;
import it.polimi.ingsw.connection.socket.message.connectionMessage.Ping;
import it.polimi.ingsw.connection.socket.message.serverMessage.*;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.ScoreBoard;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * SocketConnectionHandler class
 * @author Matteo Leonardo Luraghi
 */
public class SocketConnectionHandler extends ConnectionHandler implements Runnable {
    private final Server server;
    private final Socket socket;
    private final Thread pingThread;
    private final AtomicBoolean active = new AtomicBoolean(false);
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    /**
     * Constructor that initializes a ping thread
     * @param server a server
     * @param socket a socket
     */
    public SocketConnectionHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        this.pingThread = new Thread(() -> {
            while(active.get()) {
                try {
                    Thread.sleep(5000);
                    sendMessageClient(new Ping());
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
    }

    @Override
    public void run() {
        try {
            this.outputStream = new ObjectOutputStream(socket.getOutputStream());
            this.inputStream = new ObjectInputStream(socket.getInputStream());
            this.active.set(true);

            this.pingThread.start();

            ArrayList<String> gameNames = (ArrayList<String>) this.server.getGames().stream()
                    .filter(c -> !c.isGameStarted())
                    .map(Controller::getGameName)
                    .collect(Collectors.toList());

            sendMessageClient(new JoinGameRequest(gameNames));

            while(this.active.get()) {
                try {
                    Object object = this.inputStream.readObject();
                    if(!(object instanceof Ping)) {
                        ClientMessage msg = (ClientMessage) object;
                        msg.execute(this.server, this);
                    }
                } catch (ClassNotFoundException | SocketTimeoutException e) {
                    disconnect();
                }
            }
        } catch(IOException e) {
            disconnect();
        }
    }

    /**
     * Send a message to the client
     * @param msg message to be sent
     */
    public void sendMessageClient(Serializable msg){
        try {
            this.outputStream.writeObject(msg);
            this.outputStream.flush();
            this.outputStream.reset();
        } catch (IOException e) {
            disconnect();
        }
    }

    /**
     * Disconnects the handler closing input and output stream and socket
     */
    @Override
    public void disconnect() {
        if (this.active.get()){
            this.active.set(false);
            this.server.removeClient(this);
            try {
                this.inputStream.close();
            } catch (IOException ignored){}
            try {
                this.outputStream.close();
            } catch (IOException ignored){}
            try {
                this.socket.close();
            } catch (IOException ignored){}
        }
    }

    /**
     * Ask the player for their color using a ColorRequest server message
     * @param availableColors the possible colors
     */
    @Override
    public void colorRequest(ArrayList<Color> availableColors) {
        sendMessageClient(new ColorRequest(availableColors));
    }

    /**
     * Ask the player for the number of players in the new game
     * using a PlayersNumberRequest server message
     */
    @Override
    public void playersNumberRequest() {
        sendMessageClient(new PlayersNumberRequest());
    }

    /**
     * Send the player a message using a TextMessage server message
     * @param message the message
     */
    @Override
    public void sendTextMessage(String message) {
        sendMessageClient(new TextMessage(message));
    }

    /**
     * Tell the player that the game will start when there are enough players
     * using a WaitingForPlayers server message
     */
    @Override
    public void waitingForPlayers() {
        sendMessageClient(new WaitingForPlayers());
    }

    /**
     * Set the player on the client using a SetPlayerMessage server message
     * @param player the player
     */
    @Override
    public void setPlayer(Player player) {
        sendMessageClient(new SetPlayerMessage(player));
    }

    /**
     * Ask the player to place the starting card (front or back) using a PlayStaringCardRequest server message
     * @param startingCard the starting card
     */
    @Override
    public void playStartingCardRequest(StartingCard startingCard) {
        sendMessageClient(new PlayStartingCardRequest(startingCard));
    }

    /**
     * Ask the player to choose one goal card using a GoalCardRequest server message
     * @param goalCards the goal cards
     */
    @Override
    public void goalCardRequest(GoalCard[] goalCards) {
        sendMessageClient(new GoalCardRequest(goalCards));
    }

    /**
     * Update the game when a card is played or drawn using a UpdateGameMessage server message
     * @param game the current game
     */
    @Override
    public void updateGame(GameState game) {
        sendMessageClient(new UpdateGameMessage(game));
    }

    /**
     * Tell the player that it's not their turn using a NotYourTurn server message
     * @param message the message
     */
    @Override
    public void notYourTurn(String message) {
        sendMessageClient(new NotYourTurn(message));
    }

    /**
     * Tell the player that it's their turn to play using a YourTurn message
     */
    @Override
    public void yourTurn() {
        sendMessageClient(new YourTurn());
    }

    /**
     * Wait for user commands using a ListenForCommands server message
     */
    @Override
    public void listenForCommands() {
        sendMessageClient(new ListenForCommands());
    }

    /**
     * Ask the player to play a card using a PlayCardRequest server message
     * @param player the player
     */
    @Override
    public void playCardRequest(Player player) {
        sendMessageClient(new PlayCardRequest(player));
    }

    /**
     * Ask the player to draw a card using a DrawCardRequest server message
     * @param player the player
     */
    @Override
    public void drawCardRequest(Player player) {
        sendMessageClient(new DrawCardRequest(player));
    }

    /**
     * Show the winner using a Winner server message
     * @param game the current game
     */
    @Override
    public void showWinner(GameState game) {
        sendMessageClient(new Winner(game));
    }

    /**
     * Show the scoreboard of the game using a ScoreBoardMessage server message
     * @param scoreBoard the scoreboard
     */
    @Override
    public void showScoreBoard(ScoreBoard scoreBoard) {
        sendMessageClient(new ScoreBoardMessage(scoreBoard));
    }

    /**
     * Send a message to the client
     * @param msg the message
     */
    @Override
    public void sendMessage(Serializable msg) {
        sendMessageClient(msg);
    }
}
