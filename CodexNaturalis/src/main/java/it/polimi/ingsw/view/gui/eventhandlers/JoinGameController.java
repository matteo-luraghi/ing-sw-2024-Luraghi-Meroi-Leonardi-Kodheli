package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.view.gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

/**
 * JoinGame.fxml event handler, used to make the player join a game or create a new one
 */
public class JoinGameController  extends EventHandler{

    public ListView gamesList;
    public TextField gameName;
    public ChoiceBox numOfPlayers;
    /**
     * Sets the names of the currently available games and adds it to the listView
     * @param gameNames the name of the games
     */
    public void setGameNames(ArrayList<String> gameNames){
        ObservableList<String> items = FXCollections.observableArrayList(gameNames);
        gamesList.setItems(items);
    }

    /**
     * Function that gets called when the page loads, sets the choices for the numberOfPlayers ChoiceBox
     */
    public void initialize(){
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(2);
        nums.add(3);
        nums.add(4);
        ObservableList<Integer> choices = FXCollections.observableArrayList(nums);
        numOfPlayers.setItems(choices);
        numOfPlayers.setValue(nums.getFirst());
    }

    /**
     * Function that gets called when you click the correlated button
     * Joins the selected game
     */
    public void joinGame() {
        String selectedGameName = gamesList.getSelectionModel().getSelectedItem().toString();

        //Send data to the view
        view.setIsJoining(false);
        view.setGameName(selectedGameName);
        view.changeScene("Login.fxml");
        view.setLoginParameters();
    }

    /**
     * Function that gets called when you click the correlated button
     * Tries to create a new game with the given name and capacity
     */
    public void createGame() {
        //Initialize alert
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid input data");
        alert.setHeaderText("Invalid input data");

        String game = gameName.getText();
        String numOfPlayersText = numOfPlayers.getValue().toString();
        int numberOfPlayers;
        //checks game name validity
        if(game == null || game.isEmpty()){
            alert.setContentText("Game name is null");
            alert.showAndWait();
            return;
        }

        //checks numberOfPlayers validity
        try{
            numberOfPlayers = Integer.parseInt(numOfPlayersText);
        }catch(NumberFormatException e){
            alert.setContentText("Number of players is null or not a number");
            alert.showAndWait();
            return;
        }

        //Send data to view
        view.setIsJoining(false);
        view.setGameName(game);
        view.setNumOfPlayersChosen(numberOfPlayers);
        view.changeScene("Login.fxml");
        view.setLoginParameters();
    }

    /**
     * Function that gets called when you click the correlated button
     * Refreshed the list of currently available games
     */
    public void refreshGame() {
        //TODO:Implement after controller method
    }
}
