package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.Util;
import it.polimi.ingsw.view.gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;

import java.util.ArrayList;
import java.util.Collections;

public class LoginController extends EventHandler{

    public TextField Username;
    public ChoiceBox Color;
    public Label ColorLabel;
    public Button ColorButton;
    public Button LoginButton;
    private boolean isJoin;
    private String gameName;

    public void setAvailableColors(ArrayList<Color> availableColors){
        ArrayList<String> colors = new ArrayList<>();
        for(Color color : availableColors){
            colors.add(color.toStringGUI());
        }
        ObservableList<String> list = FXCollections.observableArrayList(colors);
        Color.setItems(list);
        Color.setValue(list.getFirst());
        Color.setVisible(true);
        ColorLabel.setVisible(true);
        ColorButton.setVisible(true);
    }

    public void setParameters(boolean isJoin, int numOfPlayers, String gameName){
        this.isJoin = isJoin;
        this.gameName = gameName;
        //TODO: Update label "GAME!" to reflect gameName
    }

    public void initialize(){
        ColorLabel.setVisible(false);
        ColorButton.setVisible(false);
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
            view.getClient().gameChoice(isJoin, gameName, user);
            //tick beside the textField?
            Username.setEditable(false);
            Username.setStyle("-fx-background-color: #77f545;");
            LoginButton.setDisable(true);
        }catch (Exception e){
            alert.setContentText("Username is already present");
            alert.showAndWait();
        }

    }

    public void chooseColor(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid input data");
        alert.setHeaderText("Invalid input data");

        Color chosenColor;
        if(Color == null){
            alert.setContentText("Color is null");
            alert.showAndWait();
            return;
        }

        chosenColor = Util.stringToColor(Color.getValue().toString());
        if(chosenColor == null){
            alert.setContentText("Color is not valid");
            alert.showAndWait();
        }

        view.getClient().colorResponse(chosenColor);
        Color.setDisable(true);
        ColorButton.setDisable(true);
    }

    public void showWaitingForPlayers(){
        //TODO: implement once the label is created
    }
}
