package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.connection.ConnectionClosedException;
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

    public TextField Username;
    public ChoiceBox Color;
    public Label ColorLabel;
    public Button ColorButton;
    public Button LoginButton;
    public Label gameName;
    public Label waitingLabel;
    private boolean isJoin;
    private String gameNameString;

    @Override
    public void showPopup(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(text);
        alert.showAndWait();
        alert = null;
        Color.setDisable(false);
        ColorButton.setDisable(false);
    }


    /**
     * Sets the currently available colors to the ChoiceBox and shows the color choosing part of the form
     * @param availableColors the colors that are currently available
     */
    public void setAvailableColors(ArrayList<Color> availableColors){
        //Convert the Colors to Strings
        ArrayList<String> colors = new ArrayList<>();
        for(Color color : availableColors){
            colors.add(color.toStringGUI());
        }
        ObservableList<String> list = FXCollections.observableArrayList(colors);
        Color.setItems(list);
        Color.setValue(list.getFirst());
        //Makes the color part of the form visible
        Color.setVisible(true);
        ColorLabel.setVisible(true);
        ColorButton.setVisible(true);
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
     * Function that gets called when the page loads, hides the color part of the form
     */
    public void initialize(){
        ColorLabel.setVisible(false);
        ColorButton.setVisible(false);
        Color.setVisible(false);
    }

    /**
     * Function that gets called when you click the correlated button
     * Checks input data and tries to insert the client into the chosen game with the chosen nickname
     */
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
            view.getClient().gameChoice(isJoin, gameNameString, user);
            //tick beside the textField?
            Username.setEditable(false);
            Username.setStyle("-fx-background-color: #77f545;");
            LoginButton.setDisable(true);
        }catch (Exception e){
            alert.setContentText("Username is already present");
            alert.showAndWait();
        }

    }

    /**
     * Function that gets called when you click the correlated button
     * Checks input data and tries to set the color chosen by the player
     */
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
            return;
        }

        view.getClient().colorResponse(chosenColor);
        Color.setDisable(true);
        ColorButton.setDisable(true);
    }

    /**
     * Function that gets called when you click the correlated button
     * Makes the label "waiting for players" visible
     */
    public void showWaitingForPlayers(){ waitingLabel.setVisible(true); }
}
