package it.polimi.ingsw.connection;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.connection.message.clientMessage.ClientMessage;
import it.polimi.ingsw.connection.message.connectionMessage.Ping;
import it.polimi.ingsw.connection.message.serverMessage.ServerMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * ClientHandler class
 * @author Matteo Leonardo Luraghi
 */
public class ClientHandler implements Runnable{
    private final int PING_TIME = 5000;
    private final Server server;
    private final Socket socket;
    private final Thread pingThread;
    private boolean activeClient;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String clientNickname;
    private Controller controller;

    /**
     * Constructor that initializes a ping thread
     * @param server a server
     * @param socket a socket
     */
    public ClientHandler(Server server, Socket socket) {
        this.server = server;
        this.socket = socket;
        pingThread = new Thread(() -> {
            while(activeClient) {
                try {
                    Thread.sleep(this.PING_TIME);
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
            activeClient = true;

            pingThread.start();

            while(activeClient) {
                try {
                    Object object = inputStream.readObject();
                    if(!(object instanceof Ping)) {
                        ClientMessage msg = (ClientMessage) object;
                        msg.execute(server, this);
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
        assert (msg instanceof ServerMessage) || (msg instanceof Ping);
        try {
            outputStream.writeObject(msg);
            outputStream.flush();
            outputStream.reset();
        } catch (IOException e) {
            disconnect();
        }
    }

    /**
     * Disconnects the server closing input and output stream and socket
     */
    public void disconnect() {
        if (activeClient){
            activeClient = false;
            server.removeClient(this);
            try {
                inputStream.close();
            } catch (IOException ignored){}
            try {
                outputStream.close();
            } catch (IOException ignored){}
            try {
                socket.close();
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
