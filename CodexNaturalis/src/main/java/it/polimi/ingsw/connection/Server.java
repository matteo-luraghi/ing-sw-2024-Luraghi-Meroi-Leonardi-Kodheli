package it.polimi.ingsw.connection;

import it.polimi.ingsw.connection.socket.message.connectionMessage.Disconnection;
import it.polimi.ingsw.connection.socket.SocketConnectionHandler;
import it.polimi.ingsw.connection.socket.message.serverMessage.JoinGameRequest;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.RemoteController;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

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
        // export the registry to the same ip as the server
        System.setProperty("java.rmi.server.hostname", InetAddress.getLocalHost().getHostAddress());
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
                clientSocket.setSoTimeout(10000);
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
    public void joinGame(ConnectionHandler connectionHandler, String gameName) {
        Optional<Controller> optionalController;
        synchronized (this.gameLock) {
            System.out.println("Active Games:" + games.keySet().size());
            // find the first free game and try to add the player
            optionalController = games.keySet().stream().
                    filter(g -> !g.isGameStarted()
                            && g.getGameName().equals(gameName)
                            && g.getHandlers().size() < games.get(g))
                    .findFirst();
        }
        if (optionalController.isPresent()) {
            Controller gameController = optionalController.get();
            gameController.addHandler(connectionHandler);
            connectionHandler.setController(gameController);
            connectionHandler.getController().chooseColorState(connectionHandler);
            return;
        }

        ArrayList<String> gameNames = null;
        try {
            gameNames = getGamesNames();
        } catch (RemoteException e) {
            System.err.println("Error getting game names");
        }
        // no game found -> game already started or game deleted
        connectionHandler.joinGameRequest(gameNames);
    }

    /**
     * Overloading of addToGame with number of players
     * @param connectionHandler the client handler
     * @param numberOfPlayers the number of players for the new game
     */
    @Override
    public void createGame(ConnectionHandler connectionHandler, int numberOfPlayers, String gameName) {
        Controller controller = new Controller(gameName, numberOfPlayers);
        synchronized (this.gameLock) {
            this.games.put(controller, numberOfPlayers);
        }
        controller.addHandler(connectionHandler);
        connectionHandler.setController(controller);
        try {
            RemoteController stub = (RemoteController) UnicastRemoteObject.exportObject(controller, 0);
            this.registry.rebind("controller"+controller.getGameName(), stub);
        } catch (Exception e) {
            System.err.println("Error exposing the controller");
            System.out.println(e);
        }
        connectionHandler.getController().chooseColorState(connectionHandler);
    }

    /**
     * Remove and disconnect a client
     * @param connectionHandler the connectionHandler relative to the client
     */
    @Override
    public void removeClient(ConnectionHandler connectionHandler) {
        Optional<Controller> optionalController = this.games.keySet().stream().filter(c -> c.getHandlers().contains(connectionHandler)).findFirst();
        if (optionalController.isPresent()) {
            Controller controller = optionalController.get();
            controller.getHandlers().remove(connectionHandler);
            ArrayList<ConnectionHandler> handlers = controller.getHandlers();
            synchronized (this.gameLock) {
                this.games.remove(controller);
            }
            // remove controller from registry
            try {
                RemoteController stub = (RemoteController) this.registry.lookup("controller"+controller.getGameName());
                UnicastRemoteObject.unexportObject(stub, true);
            } catch (Exception ignored) {}
            try {
                this.registry.unbind("controller"+controller.getGameName());
            } catch (Exception ignored) {}

            controller.broadcastMessage(new Disconnection(connectionHandler.getClientNickname()), handlers);

        }
    }

    /**
     * Check if the nickname is unique in the group
     * @param nickname the nickname
     * @return true if no other player with the same nickname
     */
    @Override
    public boolean checkUniqueNickname(String nickname){
        for(Controller c: games.keySet()) {
            for (ConnectionHandler ch : c.getHandlers()) {
                if (ch.getClientNickname().equals(nickname)) {
                    return false;
                }
            }
        }
        // no game with the same nickname
        return true;
    }

    /**
     * Get all the games names
     * @return the names
     */
    @Override
    public ArrayList<String> getGamesNames() throws RemoteException {
        return (ArrayList<String>) this.games.keySet().stream()
                .filter(c -> !c.isGameStarted() && c.getHandlers().size() < games.get(c))
                .map(Controller::getGameName)
                .collect(Collectors.toList());
    }
}
