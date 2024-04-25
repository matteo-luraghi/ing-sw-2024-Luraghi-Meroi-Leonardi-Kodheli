package it.polimi.ingsw.connection;

import it.polimi.ingsw.connection.message.connectionMessage.Disconnection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class
 * @author Matteo Leonardo Luraghi
 */
public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private final int TIMEOUT = 10000;
    private final Map<Integer, Integer> games; // Keys as controllers
    private final ExecutorService executor;
    private final Object lobbyLock = new Object();

    public Server(int port) {
        this.port = port;
        this.games = new ConcurrentHashMap<>();
        this.executor = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.out.println("Unable to connect!");
            return;
        }

        try {
            while(true) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(this.TIMEOUT);
                ClientHandler clientConnection = new ClientHandler(this, clientSocket);
                executor.submit(clientConnection);
            }
        } catch (IOException e) {
            System.out.println("Error connecting to the client");
        }
    }

    public void removeClient(ClientHandler clientHandler) {
        Optional<Controller> controller = games.keySet().stream().filter(c -> c.getHandlers().contains(clientHandler)).findFirst();
        if (controller.isPresent()) {
            synchronized (lobbyLock) {
                games.replace(controller.get(), games.get(controller.get()) - 1);
                if (games.get(controller.get()) <= 0 || !controller.get().isGameStarted())
                    games.remove(controller.get());
                else if (!controller.get().isGameEnded())
                    controller.get().broadcastMessage(new Disconnection(clientHandler.getClientNickname()));
            }
        }
    }

}
