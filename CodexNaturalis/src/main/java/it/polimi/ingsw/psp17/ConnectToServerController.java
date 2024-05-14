package it.polimi.ingsw.psp17;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.util.ArrayList;

public class ConnectToServerController {
    @FXML
    public ChoiceBox connectionChoice;
    @FXML
    public TextField serverIP;
    @FXML
    public TextField serverPort;
    public void initialize() {
        ArrayList<String> connections = new ArrayList<>();
        connections.add("Socket");
        connections.add("RMI");
        ObservableList<String> list = FXCollections.observableArrayList(connections);
        connectionChoice.setItems(list);
        connectionChoice.setValue("Socket");
    }
    @FXML
    protected void ConnectToServer() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid input data!!!");
        alert.setHeaderText("Invalid input data");
        String ip = serverIP.getText();
        String portText = serverPort.getText();
        String connection = connectionChoice.toString();
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
        if(connection == null || connection.isEmpty() || (connection.equalsIgnoreCase("Socket") || connection.equalsIgnoreCase("RMI"))){
            alert.setContentText("Connection is not set or is an invalid value");
            alert.showAndWait();
            return;
        }

        System.out.println("IP: " + serverIP.getText() + " Port:" + serverPort.getText() + " connection: " + connectionChoice.getValue().toString());
    }
}