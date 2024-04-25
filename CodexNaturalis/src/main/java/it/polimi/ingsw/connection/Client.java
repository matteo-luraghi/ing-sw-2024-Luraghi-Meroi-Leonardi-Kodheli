package it.polimi.ingsw.connection;

import it.polimi.ingsw.connection.message.connectionMessage.Disconnection;
import it.polimi.ingsw.connection.message.connectionMessage.Ping;
import it.polimi.ingsw.connection.message.serverMessage.ServerMessage;
import it.polimi.ingsw.view.mainview.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Client class
 * used to manage the connection and communication of a client
 * @author Matteo Leonardo Luraghi
 */
public class Client {
    private final String ip;
    private final int port;
    private final View view;
    private final int PING_TIME = 5000;
    private final Thread pingThread;
    private final AtomicBoolean clientConnected = new AtomicBoolean();
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private final Queue<ServerMessage> messageQueue = new LinkedList<>();
    private final Thread messageListener;
    private final Thread messageHandler;
    private String nickname;

    /**
     * Constructor, builds the threads needed for messages
     * @param ip the ip address
     * @param port the port of the connection
     * @param view View interface
     */
    public Client(String ip, int port, View view) {
        this.ip = ip;
        this.port = port;
        this.view = view;

        this.messageListener = new Thread(this::readMessages);
        this.messageHandler = new Thread(this::handleMessages);

        this.pingThread = new Thread(() -> {
            while (this.clientConnected.get()) {
                try {
                    Thread.sleep(PING_TIME);
                    sendMessageServer(new Ping());
                } catch (InterruptedException e) {
                    disconnect(true);
                }
            }
        });
    }

    /**
     * Send a message to the server
     * @param message the message to be sent
     */
    public void sendMessageServer(Serializable message) {
        if(this.clientConnected.get()) {
            try {
                outputStream.writeObject(message);
                outputStream.flush();
                outputStream.reset();
            } catch (IOException e) {
                disconnect(true);
            }
        }
    }

    /**
     * Initialize the client
     * @throws IOException if errors occour
     */
    public void init() throws IOException {
        this.clientSocket = new Socket();
        this.clientSocket.connect(new InetSocketAddress(ip, port));
        this.outputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(this.clientSocket.getInputStream());
        this.clientConnected.set(true);
        pingThread.start();
        messageHandler.start();
        messageListener.start();
    }

    /**
     * Read the messages and add them in the queue
     */
    public void readMessages() {
        try {
            while(this.clientConnected.get()) {
                Object msg = this.inputStream.readObject();
                if(msg instanceof ServerMessage) {
                    if (msg instanceof Disconnection) {
                        this.messageQueue.clear();
                        ((Disconnection) msg).show(view);
                        disconnect(false);
                    }

                    this.messageQueue.add((ServerMessage) msg);

                    switch (((ServerMessage) msg).getType()) {
                        case LOGIN_REQUEST -> {

                        }
                        case PLAYERS_NUMBER_REQUEST -> {

                        }
                        case WAITING_FOR_PLAYERS -> {

                        }
                        case YOUR_TURN -> {

                        }
                        case CARD_NOT_PLAYABLE -> {

                        }
                        case CARD_PLAYED -> {

                        }
                        case PICK_A_CARD -> {

                        }
                        case CANNOT_DRAW -> {

                        }
                        case CARD_DRAWN -> {

                        }
                        case TURN_ENDED -> {

                        }
                        case WINNER -> {

                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            disconnect(true);
        }
    }

    /**
     * Handles the messages by removing them from the queue and showing them
     */
    public void handleMessages() {
        while(this.clientConnected.get()) {
            if(!messageQueue.isEmpty()) {
                ServerMessage msg = messageQueue.poll();
                assert msg != null;
                msg.show(this.view);
            }
        }
    }

    /**
     * Disconnect the client
     * @param error if the disconnection emerges from an error the client is notified
     */
    public void disconnect(boolean error) {
        if(this.clientConnected.get()) {
            this.clientConnected.set(false);
            if(this.messageListener.isAlive()) this.messageListener.interrupt();
            if(this.messageHandler.isAlive()) this.messageHandler.interrupt();

            try {
                this.inputStream.close();
            } catch(IOException e) {}
            try {
                this.outputStream.close();
            } catch(IOException e) {}
            try {
                this.clientSocket.close();
            } catch(IOException e) {}

            if(error) {
                this.view.showMessage("An error occurred, you'll be disconnected from the server");
            }

        }
    }

    /**
     * Nickname getter
     * @return the current nickname
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Nickname setter
     * @param nickname the nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
