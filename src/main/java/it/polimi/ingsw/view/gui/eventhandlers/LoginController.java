package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.Util;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * Login.fxml event handler, used to make the player login (chose a nickname and color)
 */
public class LoginController extends EventHandler{

    public TextField username;
    public ChoiceBox color;
    public Label colorLabel;
    public Button colorButton;
    public Button loginButton;
    public Label gameName;
    public Label waitingLabel;
    private boolean isJoin;
    private String gameNameString;

    /**
     * Function that gets called when the page loads, hides the color part of the form
     */
    public void initialize(){
        colorLabel.setVisible(false);
        colorButton.setVisible(false);
        color.setVisible(false);
    }

    /**
     * Sets certain parameters passed from JoinGameController
     * @param isJoin true if the player is joining a game, false if they're creating one
     * @param gameNameString the game that the player is joining/creating
     */
    public void setParameters(boolean isJoin, String gameNameString){
        this.isJoin = isJoin;
        this.gameNameString = gameNameString;
        gameName.setText(gameNameString+"!");
    }

    /**
     * Sets the currently available colors to the ChoiceBox and shows the color choosing part of the form
     * @param availableColors the colors that are currently available
     */
    public void setAvailableColors(ArrayList<Color> availableColors){
        ObservableList<String> list = FXCollections.observableArrayList();
        color.setItems(list);
        for(Color c : availableColors){
            color.getItems().add(c.toStringGUI());
        }
        color.setValue(list.getFirst());
        //Makes the color part of the form visible
        color.setVisible(true);
        colorLabel.setVisible(true);
        colorButton.setVisible(true);
    }

    /**
     * Function that gets called when you click the correlated button
     * Checks input data and tries to insert the client into the chosen game with the chosen nickname
     */
    public void loginPlayer() {

        String user = username.getText();
        if(user == null || user.isEmpty()){
            showPopup("Invalid input data: username is empty");
            return;
        }
        try{
            view.getClient().gameChoice(isJoin, gameNameString, user);
            username.setEditable(false);
            username.setStyle("-fx-background-color: #2e821b;" +
                    "-fx-background-radius: 100;" +
                    "-fx-border-color: #000000;" +
                    "-fx-border-radius: 100;" +
                    "-fx-border-width: 2;");
            loginButton.setDisable(true);
        }catch (Exception e){
            showPopup("Invalid input data: username is already present");
        }

    }

    /**
     * Function that gets called when you click the correlated button
     * Checks input data and tries to set the color chosen by the player
     */
    public void chooseColor() {
        Color chosenColor;
        if(color == null){
            showPopup("Invalid input data: color is null");
            return;
        }

        chosenColor = Util.stringToColor(color.getValue().toString());
        if(chosenColor == null){
            showPopup("Invalid input data: color is not valid");
            return;
        }

        view.getClient().colorResponse(chosenColor);
        color.setDisable(true);
        colorButton.setDisable(true);
    }

    /**
     * Function that gets called when you click the correlated button
     * Makes the label "waiting for players" visible
     */
    public void showWaitingForPlayers(){ waitingLabel.setVisible(true); }
}
