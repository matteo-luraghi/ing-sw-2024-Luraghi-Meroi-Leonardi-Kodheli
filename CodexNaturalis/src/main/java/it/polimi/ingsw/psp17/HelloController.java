package it.polimi.ingsw.psp17;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class HelloController {
    @FXML
    public ChoiceBox connectionChoice;
    @FXML
    private Label welcomeText;
    public void initialize() {
        ArrayList<String> connections = new ArrayList<>();
        connections.add("Socket");
        connections.add("RMI");
        ObservableList<String> list = FXCollections.observableArrayList(connections);
        connectionChoice.setItems(list);
    }
    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void ConnectToServer() {
        System.out.println("Join the game");
    }
}