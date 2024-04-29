package it.polimi.ingsw;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.connection.ConnectionHandler;
import it.polimi.ingsw.connection.Server;
import it.polimi.ingsw.view.cli.CLI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;

public class ConnectToServerTest {

    Client client = null;
    CLI cli = null;
    Server server = null;

    ConnectionHandler connectionHandler = null;

    Thread serverThread = null;

    @After
    public void tearDown() {
        serverThread.interrupt();
    }

    @Before
    public void SetUp() {
        cli = new CLI();
        serverThread = new Thread(() -> {
            try {
                Server server = new Server(8080);
                server.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Test
    public void CheckConnection () throws IOException {
        serverThread.start();
        client = new Client(InetAddress.getLocalHost().getHostAddress(), 8080, cli); //to get your
        // private ip
    }
}
