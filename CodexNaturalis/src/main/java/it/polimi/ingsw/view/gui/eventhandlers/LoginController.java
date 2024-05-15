package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.view.gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

import java.util.ArrayList;
import java.util.Collections;

public class LoginController extends EventHandler{

    public TextField Username;
    public ChoiceBox Color;
    private ArrayList<Color> availableColors;
    public void setAvailableColors(ArrayList<Color> availableColors){
        ObservableList<Color> list = FXCollections.observableArrayList(availableColors);
        Color.setItems(list);
        Color.setValue(list.getFirst());
        Color.setVisible(true);
    }
    public void initialize(){
        Color.setVisible(false);
    }

    public void loginPlayer(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid input data");
        alert.setHeaderText("Invalid input data");

        String user = Username.getText();
        if(user == null || user.isEmpty()){
            alert.setContentText("Username is empty");
            alert.showAndWait();
            return;
        }
        try{
            view.getClient().loginResponse(user);
            //tick beside the textField?
            Username.setEditable(false);
            Username.setStyle("-fx-background-color: green;");
        }catch (Exception e){
            alert.setContentText("Username is already present");
            alert.showAndWait();
        }

    }

    public void chooseColor(MouseEvent mouseEvent) {

    }
}
