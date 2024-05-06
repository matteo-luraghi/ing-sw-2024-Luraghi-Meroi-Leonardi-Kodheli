package it.polimi.ingsw.connection;

import it.polimi.ingsw.connection.message.connectionMessage.Disconnection;
import it.polimi.ingsw.connection.message.connectionMessage.Ping;
import it.polimi.ingsw.connection.message.serverMessage.NotYourTurn;
import it.polimi.ingsw.connection.message.serverMessage.ServerMessage;
import it.polimi.ingsw.view.mainview.View;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Client class
 * used to manage the connection and communication of a client
 * @author Matteo Leonardo Luraghi
 */
public class Client {
    private final View view;
    private final AtomicBoolean connected = new AtomicBoolean();
    private final Socket clientSocket;
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final Thread messageReceiver;
    private Thread getCommands = null;

    /**
     * Constructor, builds the threads needed for messages
     * @param ip the ip address
     * @param port the port of the connection
     * @param view View interface
     * @throws IOException if the connection with the server fails
     */
    public Client(String ip, int port, View view) throws IOException {
        this.view = view;
        this.messageReceiver = new Thread(this::readMessages);
        this.clientSocket = new Socket();
        this.clientSocket.connect(new InetSocketAddress(ip, port));
        this.outputStream = new ObjectOutputStream(this.clientSocket.getOutputStream());
        this.inputStream = new ObjectInputStream(this.clientSocket.getInputStream());
        this.connected.set(true);
        messageReceiver.start();

        // if the server is not online, disconnect the client
        Thread pingThread = new Thread(() -> {
            while (this.connected.get()) {
                try {
                    Thread.sleep(5000);
                    sendMessageServer(new Ping());
                } catch (InterruptedException e) {
                    // if the server is not online, disconnect the client
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
    public void sendMessageServer(Serializable message) {
        if(this.connected.get()) {
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
            while(this.connected.get()) {
                Object msg = this.inputStream.readObject();
                if(msg instanceof ServerMessage) {
                    // view the message via the CLI or GUI
                    if (msg instanceof NotYourTurn) {
                        if (this.getCommands != null && this.getCommands.isAlive()) {
                            this.getCommands.interrupt();
                        }
                        this.getCommands = new Thread(() -> ((NotYourTurn) msg).show(this.view));
                        this.getCommands.start();
                    } else {
                        if (this.getCommands != null && this.getCommands.isAlive()) {
                            this.getCommands.interrupt();
                        }
                        ((ServerMessage) msg).show(this.view);
                    }
                }
                else if (msg instanceof Disconnection) {
                    ((Disconnection) msg).show(view);
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
        if(this.connected.get()) {
            this.connected.set(false);
            if(this.messageReceiver.isAlive()) this.messageReceiver.interrupt();

            try {
                this.inputStream.close();
                this.outputStream.close();
                this.clientSocket.close();
            } catch (IOException ignored) {}
        }
    }

}
