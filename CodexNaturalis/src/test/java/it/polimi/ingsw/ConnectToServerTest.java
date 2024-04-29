package it.polimi.ingsw;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.connection.message.clientMessage.LoginResponse;
import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.view.cli.CLI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;

public class ConnectToServerTest {

    Client client = null;
    CLI cli = null;
    Thread serverThread = null;
    Server server = null;

    @After
    public void tearDown() {
        serverThread.interrupt();
    }

    @Before
    public void SetUp() {
        cli = new CLI();
        serverThread = new Thread(() -> {
            try {
                server = new Server(5004);
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void CheckConnection () throws IOException {
        serverThread.start();
        client = new Client(InetAddress.getLocalHost().getHostAddress(), 5004, cli); //to get your

        client.sendMessageServer(new LoginResponse("Giovanni"));

        for(Controller c : server.getGames()) {
            System.out.println(c.isGameStarted());
            for (ConnectionHandler ch : c.getHandlers()) {
                System.out.println(ch.getClientNickname());
            }
        }

        // private ip
    }
}
