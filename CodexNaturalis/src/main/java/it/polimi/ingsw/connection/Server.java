package it.polimi.ingsw.connection;

import it.polimi.ingsw.connection.socket.message.connectionMessage.Disconnection;
import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.RemoteController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Server class
 * used to manage the games and the clients
 * @author Matteo Leonardo Luraghi
 */
public class Server implements RemoteServer {
    private final int port;
    private ServerSocket serverSocket;
    private Registry registry;
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
        this.registry = LocateRegistry.createRegistry(1099);

        try {
            RemoteServer serverStub = (RemoteServer) UnicastRemoteObject.exportObject(this, 0);
            registry.rebind("server", serverStub);
        } catch (Exception e) {
            System.err.println("Error exposing the server");
            throw new IOException();
        }

        this.serverSocket = new ServerSocket(this.port);
        System.out.println("Server running...");
        try {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                clientSocket.setSoTimeout(0);
                SocketConnectionHandler clientConnection = new SocketConnectionHandler(this, clientSocket);
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
    @Override
    public void addToGame(ConnectionHandler connectionHandler) {
        synchronized (this.gameLock) {
            // find the first free game and try to add the player
            Optional<Controller> optionalController = games.keySet().stream().filter(g -> !g.isGameStarted()).findFirst();
            if (optionalController.isPresent()) {
                Controller gameController = optionalController.get();
                gameController.addHandler(connectionHandler);
                connectionHandler.setController(gameController);
                connectionHandler.getController().chooseColorState(connectionHandler);
                return;
            }
        }
        // no free games available -> wait for user input of number of players
        connectionHandler.playersNumberRequest();
    }

    /**
     * Overloading of addToGame with number of players, used in PlayerNumberResponse
     * @param connectionHandler the client handler
     * @param numberOfPlayers the number of players for the new game
     */
    @Override
    public void addToGame(ConnectionHandler connectionHandler, int numberOfPlayers) {
        Controller controller = new Controller(numberOfPlayers);
        synchronized (this.gameLock) {
            this.games.put(controller, numberOfPlayers);
        }
            controller.addHandler(connectionHandler);
            connectionHandler.setController(controller);
            connectionHandler.getController().chooseColorState(connectionHandler);
            try {
                RemoteController stub = (RemoteController) UnicastRemoteObject.exportObject(controller, 0);
                this.registry.rebind("controller", stub);
            } catch (Exception e) {
                System.err.println("Error exposing the controller");
                System.out.println(e);
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
                    //this.games.remove(controller);
            }
        }
    }

    public boolean checkUniqueNickname(String nickname){
        Optional<Controller> optionalController = games.keySet().stream().filter(g -> !g.isGameStarted()).findFirst();
        if (optionalController.isPresent()) {
            //There is at least one player
            Controller gameController = optionalController.get();
            for(ConnectionHandler c : gameController.getHandlers()){
                if(c.getClientNickname().equalsIgnoreCase(nickname)){
                    return false;
                }
            }
            return true;
        } else {
            //There are no open games -> this is the first player to login
            return true;
        }

    }

    /**
     * Get all the games
     * @return the game's controllers
     */
    public Set<Controller> getGames() {
        return games.keySet();
    }
}
