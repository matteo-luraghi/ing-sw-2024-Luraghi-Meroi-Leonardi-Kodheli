package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.connection.ConnectionClosedException;
import it.polimi.ingsw.view.gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

/**
 * ConnectToServer.fxml event handler, used to make the player connect ot a server
 */
public class ConnectToServerController extends EventHandler{
    @FXML
    public ChoiceBox connectionChoice;
    @FXML
    public TextField serverIP;
    @FXML
    public TextField serverPort;

    /**
     * Function that gets called when the page loads, sets the choices for the connections ChoiceBox
     */
    public void initialize() {

        ArrayList<String> connections = new ArrayList<>();
        connections.add("Socket");
        connections.add("RMI");
        ObservableList<String> list = FXCollections.observableArrayList(connections);
        connectionChoice.setItems(list);
        connectionChoice.setValue("Socket");

    }

    /**
     * Function that gets called when you click the correlated button
     * Checks input data and tries to connect to the server
     * @throws ConnectionClosedException If a client can no longer connect to the server
     */
    @FXML
    protected void ConnectToServer() throws ConnectionClosedException {
        //Initialize alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid input data");
        alert.setHeaderText("Invalid input data");

        String ip = serverIP.getText();
        String portText = serverPort.getText();
        String connection = connectionChoice.getValue().toString();
        int port;

        //Check ip validity
        if(ip == null || ip.isEmpty()){
            alert.setContentText("IP is empty");
            alert.showAndWait();
            return;
        }

        //Check port validity
        try{
            if(portText == null || portText.isEmpty()){
                throw new NumberFormatException();
            }
            port = Integer.parseInt(portText);
        }catch(NumberFormatException e){
            alert.setContentText("Port is empty or not a number");
            alert.showAndWait();
            return;
        }

        //Check connection validity (Should always be true)
        if(connection == null || connection.isEmpty() || !(connection.equalsIgnoreCase("Socket") || connection.equalsIgnoreCase("RMI"))){
            alert.setContentText("Connection is not set or is an invalid value");
            alert.showAndWait();
            return;
        }

        //Tries to connect to the server (if it returns true, messages will be sent automatically)
        if(!view.connectToServer(ip, port, connection)){
            alert.setTitle("Error");
            alert.setHeaderText("Server error");
            alert.setContentText("Error connecting to the server, try again");
            alert.showAndWait();
        }
    }
}