package it.polimi.ingsw.connection;

import it.polimi.ingsw.connection.message.connectionMessage.Disconnection;
import it.polimi.ingsw.connection.message.serverMessage.*;
import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class
 * used to manage the games and the clients
 * @author Matteo Leonardo Luraghi
 */
public class Server {
    private final int port;
    private ServerSocket serverSocket;
    private final ExecutorService executor;
    private final Map<Controller, Integer> games;
    private final Object gameLock = new Object();

    /**
     * Constructor
     * @param port server port
     */
    public Server(int port) {
        this.port = port;
        this.games = new ConcurrentHashMap<>();
        this.executor = Executors.newCachedThreadPool();
    }

    public void start() throws IOException {
        // throws if the socket init fails
        this.serverSocket = new ServerSocket(this.port);
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(10000);
                ConnectionHandler clientConnection = new ConnectionHandler(this, clientSocket);
                // start the clientConnection thread
                executor.submit(clientConnection);
            }
        } catch (IOException e) {
            System.out.println("Error connecting to the client");
        }
    }

    /**
     * Add a client to a game via its connectionHandler
     * @param connectionHandler the connectionHandler to save in a game
     */
    public void addToGame(ConnectionHandler connectionHandler) {
        synchronized (this.gameLock) {
            // find the first free game and try to add the player
            Optional<Controller> optionalController = games.keySet().stream().filter(g -> !g.isGameStarted()).findFirst();
            if (optionalController.isPresent()) {
                Controller gameController = optionalController.get();
                gameController.addHandler(connectionHandler);
                connectionHandler.setController(gameController);
                checkGame(gameController);
                connectionHandler.sendMessageClient(new ColorRequest());
            } else { // no free games available -> wait for user input of number of players
                connectionHandler.sendMessageClient(new PlayersNumberRequest());
            }
        }
    }

    /**
     * Overloading of addToGame with number of players, used in PlayerNumberResponse
     * @param connectionHandler the client handler
     * @param numberOfPlayers the number of players for the new game
     */
    public void addToGame(ConnectionHandler connectionHandler, int numberOfPlayers) {
        synchronized (this.gameLock) {
            Controller controller = new Controller();
            this.games.put(controller, numberOfPlayers);
            controller.addHandler(connectionHandler);
            connectionHandler.setController(controller);
            connectionHandler.sendMessageClient(new ColorRequest());
            checkGame(controller);
        }
    }

    /**
     * Check if the game is ready to start
     * @param controller the controller related to the game
     */
    public void checkGame(Controller controller) {
        synchronized (this.gameLock) {
            // check if the number of player is sufficient for the game
            if(games.get(controller) == controller.getHandlers().size()) {
                controller.start();
            }
        }
    }

    /**
     * Remove and disconnect a client
     * @param connectionHandler the connectionHandler relative to the client
     */
    public void removeClient(ConnectionHandler connectionHandler) {
        Optional<Controller> optionalController = this.games.keySet().stream().filter(c -> c.getHandlers().contains(connectionHandler)).findFirst();
        if (optionalController.isPresent()) {
            synchronized (this.gameLock) {
                Controller controller = optionalController.get();
                this.games.replace(controller, this.games.get(controller) - 1);
                if (this.games.get(controller) <= 0 || !controller.isGameStarted())
                    this.games.remove(controller);
                else if (!controller.isGameEnded())
                    controller.broadcastMessage(new Disconnection(connectionHandler.getClientNickname()));
            }
        }
    }

}
