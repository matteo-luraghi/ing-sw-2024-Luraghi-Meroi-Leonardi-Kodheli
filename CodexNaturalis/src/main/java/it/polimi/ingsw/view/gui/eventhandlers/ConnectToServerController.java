package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.connection.ConnectionClosedException;
import it.polimi.ingsw.view.gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.StageStyle;

import java.util.ArrayList;

/**
 * ConnectToServer.fxml event handler, used to make the player connect ot a server
 * @author Gabriel Leonardi
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
        ObservableList<String> list = FXCollections.observableArrayList();
        connectionChoice.setItems(list);
        connectionChoice.getItems().addAll("Socket", "RMI");
        connectionChoice.setValue("Socket");

    }

    /**
     * Function that gets called when you click the correlated button
     * Checks input data and tries to connect to the server
     * @throws ConnectionClosedException If a client can no longer connect to the server
     */
    @FXML
    protected void ConnectToServer() throws ConnectionClosedException {
        String ip = serverIP.getText();
        String portText = serverPort.getText();
        String connection = connectionChoice.getValue().toString();
        int port;

        //Check ip validity
        if(ip == null || ip.isEmpty()){
            showPopup("Invalid input data: IP is empty");
            return;
        }

        //Check port validity
        try{
            if(portText == null || portText.isEmpty()){
                throw new NumberFormatException();
            }
            port = Integer.parseInt(portText);
        }catch(NumberFormatException e){
            showPopup("Invalid input data: Port is empty or not a number");
            return;
        }

        //Check connection validity (Should always be true)
        if(connection == null || connection.isEmpty() || !(connection.equalsIgnoreCase("Socket") || connection.equalsIgnoreCase("RMI"))){
            showPopup("Connection is not set or is an invalid value");
            return;
        }
        try{
            //Tries to connect to the server (if it returns true, messages will be sent automatically)
            view.connectToServer(ip, port, connection);
        }catch (ConnectionClosedException e){
            showPopup("Error connecting to the server, try again");
        }

    }
}