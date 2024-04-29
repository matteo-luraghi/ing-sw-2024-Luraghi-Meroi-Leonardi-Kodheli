package it.polimi.ingsw.connection;

import it.polimi.ingsw.connection.message.serverMessage.LoginRequest;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.connection.message.clientMessage.ClientMessage;
import it.polimi.ingsw.connection.message.connectionMessage.Ping;
import it.polimi.ingsw.model.gamelogic.Color;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * ConnectionHandler class
 * @author Matteo Leonardo Luraghi
 */
public class ConnectionHandler implements Runnable{
    private final Server server;
    private final Socket socket;
    private final Thread pingThread;
    private final AtomicBoolean active = new AtomicBoolean(false);
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String clientNickname;
    private Color clientColor;
    private Controller controller;

    /**
     * Constructor that initializes a ping thread
     * @param server a server
     * @param socket a socket
     */
    public ConnectionHandler(Server server, Socket socket) {
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
            this.active .set(false);
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
     * Get the client's nickname
     * @return nickname of the client
     */
    public String getClientNickname(){
        return this.clientNickname;
    }

    /**
     * Set the client's nickname
     * @param nickname nickname to set
     */
    public void setClientNickname(String nickname) {
        this.clientNickname = nickname;
    }

    /**
     * Get the client's color
     * @return the color
     */
    public Color getClientColor() {
        return this.clientColor;
    }

    /**
     * Set the client's color
     * @param color the color to set
     */
    public void setClientColor(Color color) {
        this.clientColor = color;
    }

    /**
     * Controller getter
     * @return the client's controller
     */
    public Controller getController() {
        return this.controller;
    }

    /**
     * Controller setter
     * @param controller to be set
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }
}
