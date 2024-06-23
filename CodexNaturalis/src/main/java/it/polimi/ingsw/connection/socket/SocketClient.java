package it.polimi.ingsw.connection.socket;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.connection.socket.message.clientMessage.*;
import it.polimi.ingsw.connection.socket.message.connectionMessage.Disconnection;
import it.polimi.ingsw.connection.socket.message.connectionMessage.Ping;
import it.polimi.ingsw.connection.socket.message.serverMessage.ServerMessage;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.Coordinates;
import it.polimi.ingsw.model.gamelogic.gamechat.Message;
import it.polimi.ingsw.view.mainview.View;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * SocketClient class
 * used to receive and send messages from and to the server
 * @author Matteo Leonardo Luraghi
 */
public class SocketClient extends Client {
    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Thread messageReceiver;
    private final Thread pingThread;

    /**
     * Constructor, builds the threads needed for messages
     * @param ip the server's ip address
     * @param port the server's port of the connection
     * @param view View interface
     * @throws IOException if the connection with the server fails
     */
    public SocketClient(String ip, int port, View view) throws IOException {
        super(view);
        this.messageReceiver = new Thread(this::readMessages);
        this.clientSocket = new Socket();
        this.clientSocket.connect(new InetSocketAddress(ip, port));
        this.outputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(this.clientSocket.getInputStream());
        messageReceiver.start();

        // if the server is not online, disconnect the client
        this.pingThread = new Thread(() -> {
            while (this.getConnected()) {
                try {
                    Thread.sleep(5000);
                    sendMessageServer(new Ping());
                } catch (InterruptedException e) {
                    // if the server is not online, disconnect the client
                    System.err.println("Error connecting to the server");
                    disconnect();
                }
            }
        });
        pingThread.start();
    }

    /**
     * Send a message to the server
     * @param message the message to be sent
     */
    private void sendMessageServer(Serializable message) {
        if(this.getConnected()) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
                outputStream.reset();
            } catch (IOException e) {
                disconnect();
            }
        }
    }

    /**
     * Read the messages and add them in the queue
     */
    public void readMessages() {
        try {
            while(this.getConnected()) {
                Object msg = this.inputStream.readObject();
                if(msg instanceof ServerMessage) {
                    // view the message via the CLI or GUI
                    ((ServerMessage) msg).show(this.getView());
                }
                else if (msg instanceof Disconnection) {
                    ((Disconnection) msg).show(this.getView());
                    disconnect();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error reading message:" + e);
            disconnect();
        }
    }

    /**
     * Disconnect the client
     */
    public void disconnect() {
        if(this.getConnected()) {
            this.setConnected(false);
            if(this.messageReceiver.isAlive()) this.messageReceiver.interrupt();
            if(this.pingThread.isAlive()) this.pingThread.interrupt();
            try {
                this.inputStream.close();
                this.outputStream.close();
                this.clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

    /**
     * Send the chosen game name to the server
     * @param isJoin true if the user wants to join a game
     * @param gameName game name
     * @param nickname user nickname
     */
    @Override
    public void gameChoice(boolean isJoin, String gameName, String nickname) {
        sendMessageServer(new JoinGameResponse(isJoin, gameName, nickname));
    }

    /**
     * Send the selected color to the server using a ColorResponse client message
     * @param color the color
     */
    @Override
    public void colorResponse(Color color) {
        sendMessageServer(new ColorResponse(color));
    }

    /**
     * Send the number of players to the server
     * @param number the number of players
     */
    @Override
    public void playersNumberResponse(int number, String gameName) {
        sendMessageServer(new PlayersNumberResponse(number, gameName));
    }

    /**
     * Send the starting card on the correct side
     * @param card the starting card
     * @param isFront the side
     */
    @Override
    public void playStartingCardResponse(StartingCard card, boolean isFront) {
        sendMessageServer(new PlayStartingCardResponse(card, isFront));
    }

    /**
     * Send the selected goal card to the server
     * @param card the goal card
     */
    @Override
    public void goalCardResponse(GoalCard card) {
        sendMessageServer(new GoalCardResponse(card));
    }

    /**
     * Send the server a message to ensure the client is aware it's its player's turn
     */
    @Override
    public void yourTurnOk() {
        sendMessageServer(new YourTurnOk());
    }

    /**
     * Send the selected card to be played, the coordinates and the side
     * @param card the card
     * @param where the coordinates
     * @param isFront the side
     */
    @Override
    public void playCardResponse(ResourceCard card, Coordinates where, boolean isFront) {
        sendMessageServer(new PlayCardResponse(card, where, isFront));
    }

    /**
     * Send the reference of which card to draw
     * @param which the card
     * @param isGold which deck to draw from
     */
    @Override
    public void drawCardResponse(int which, boolean isGold) {
        sendMessageServer(new DrawCardResponse(which, isGold));
    }

    /**
     * Get the available games from the server
     */
    @Override
    public void refreshGamesNames() {
        sendMessageServer(new RefreshGamesNamesRequest());
    }

    /**
     * Send a message in the chat
     * @param message the message
     */
    @Override
    public void sendMessageInChat(Message message) {
        sendMessageServer(new AddMessageToChat(message));
    }
}
