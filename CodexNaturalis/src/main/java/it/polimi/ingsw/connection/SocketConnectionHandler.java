package it.polimi.ingsw.connection;

import it.polimi.ingsw.connection.message.serverMessage.*;
import it.polimi.ingsw.connection.message.clientMessage.ClientMessage;
import it.polimi.ingsw.connection.message.connectionMessage.Ping;
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
            sendMessageClient(new LoginRequest());

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
     * Disconnects the server closing input and output stream and socket
     */
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
     * Ask the player for their color
     * @param availableColors the possible colors
     */
    @Override
    public void colorRequest(ArrayList<Color> availableColors) {
        sendMessageClient(new ColorRequest(availableColors));
    }

    /**
     * Send the player a message
     * @param message the message
     */
    @Override
    public void sendTextMessage(String message) {
        sendMessageClient(new TextMessage(message));
    }

    @Override
    public void waitingForPlayers() {
        sendMessageClient(new WaitingForPlayers());
    }

    @Override
    public void setPlayer(Player player) {
        sendMessageClient(new SetPlayerMessage(player));
    }

    @Override
    public void playStartingCardRequest(StartingCard startingCard) {
        sendMessageClient(new PlayStartingCardRequest(startingCard));
    }

    @Override
    public void goalCardRequest(GoalCard[] goalCards) {
        sendMessageClient(new GoalCardRequest(goalCards));
    }

    @Override
    public void updateGame(GameState game) {
        sendMessageClient(new UpdateGameMessage(game));
    }

    @Override
    public void notYourTurn(Player player, String message) {
        sendMessageClient(new NotYourTurn(player, message));
    }

    @Override
    public void yourTurn() {
        sendMessageClient(new YourTurn());
    }

    @Override
    public void listenForCommands() {
        sendMessageClient(new ListenForCommands());
    }

    @Override
    public void playCardRequest(Player player) {
        sendMessageClient(new PlayCardRequest(player));
    }

    @Override
    public void drawCardRequest(Player player) {
        sendMessageClient(new DrawCardRequest(player));
    }

    @Override
    public void showWinner(GameState game) {
        sendMessageClient(new Winner(game));
    }

    @Override
    public void showScoreBoard(ScoreBoard scoreBoard) {
        sendMessageClient(new ScoreBoardMessage(scoreBoard));
    }

    @Override
    public void sendMessage(Serializable msg) {
        sendMessageClient(msg);
    }
}
