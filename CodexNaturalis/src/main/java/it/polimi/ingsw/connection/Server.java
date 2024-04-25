package it.polimi.ingsw.connection;

import it.polimi.ingsw.connection.message.connectionMessage.Disconnection;
import it.polimi.ingsw.connection.message.serverMessage.TextMessage;
import it.polimi.ingsw.connection.message.serverMessage.WaitingForPlayers;
import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
    private final int TIMEOUT = 10000;
    private final Map<Controller, Integer> games;
    private final ExecutorService executor;
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

    /**
     * Initialize the socket and accept the clients' connections
     */
    public void start() {
        //TODO: limit the number of players
        try {
            this.serverSocket = new ServerSocket(this.port);
        } catch (IOException e) {
            System.out.println("Unable to connect!");
            return;
        }

        try {
            while(true) {
                Socket clientSocket = this.serverSocket.accept();
                clientSocket.setSoTimeout(this.TIMEOUT);
                ClientHandler clientConnection = new ClientHandler(this, clientSocket);
                this.executor.submit(clientConnection);
            }
        } catch (IOException e) {
            System.out.println("Error connecting to the client");
        }
    }

    public void addToGame(ClientHandler clientHandler) {
        synchronized (this.gameLock) {
            Controller gameController = games.keySet().stream().findFirst().get();
            gameController.addHandler(clientHandler);
            clientHandler.setController(gameController);
            checkGame(gameController);
        }

        if(!clientHandler.getController().isGameStarted()
                && clientHandler.getClientHandlerPhase() != ClientHandlerPhase.GAME_WAIT
                && clientHandler.getClientHandlerPhase() != ClientHandlerPhase.WAITING_FOR_PLAYERS) {
            clientHandler.setClientHandlerPhase(ClientHandlerPhase.WAITING_FOR_PLAYERS);
            clientHandler.sendMessageClient(new WaitingForPlayers());
        }
    }

    public void checkGame(Controller controller) {
        synchronized (this.gameLock) {
            // check if the number of player is sufficient for the game
            if(games.get(controller) == controller.getHandlers().size()) {
                controller.start();
                return;
            }
            if(games.get(controller) != -1 && (games.get(controller) < controller.getHandlers().size())) {
                ArrayList<ClientHandler> removed = new ArrayList<>();
                for (ClientHandler c: controller.getHandlers()) {
                    if(controller.getHandlers().indexOf(c)>= games.get(controller)) {
                        removed.add(c);
                    }
                }

                for(ClientHandler c: removed) {
                    controller.getHandlers().remove(c);
                    c.sendMessageClient(new TextMessage("The game is full, you're disconnected"));
                    c.disconnect();
                }
            }
            controller.start();
        }
    }

    /**
     * Remove and disconnect a client
     * @param clientHandler
     */
    public void removeClient(ClientHandler clientHandler) {
        Optional<Controller> optionalCcontroller = this.games.keySet().stream().filter(c -> c.getHandlers().contains(clientHandler)).findFirst();
        if (optionalCcontroller.isPresent()) {
            synchronized (this.gameLock) {
                Controller controller = optionalCcontroller.get();
                this.games.replace(controller, this.games.get(controller) - 1);
                if (this.games.get(controller) <= 0 || !controller.isGameStarted())
                    this.games.remove(controller);
                else if (!controller.isGameEnded())
                    controller.broadcastMessage(new Disconnection(clientHandler.getClientNickname()));
            }
        }
    }

}
