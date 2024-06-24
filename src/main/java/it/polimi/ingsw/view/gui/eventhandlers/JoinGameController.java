package it.polimi.ingsw.view.gui.eventhandlers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.util.ArrayList;

/**
 * JoinGame.fxml event handler, used to make the player join a game or create a new one
 * @author Gabriel Leonardi
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
        ObservableList<Integer> choices = FXCollections.observableArrayList();
        numOfPlayers.setItems(choices);
        numOfPlayers.getItems().addAll(2, 3, 4);
        numOfPlayers.setValue(2);
    }

    /**
     * Function that gets called when you click the correlated button
     * Joins the selected game
     */
    public void joinGame() {
        if(gamesList.getSelectionModel().getSelectedItem() != null){
            String selectedGameName = gamesList.getSelectionModel().getSelectedItem().toString();

            //Send data to the view
            view.setIsJoining(true);
            view.setGameName(selectedGameName);
            view.changeScene("Login.fxml");
            view.setLoginParameters();
        }
    }

    /**
     * Function that gets called when you click the correlated button
     * Tries to create a new game with the given name and capacity
     */
    public void createGame() {
        String game = gameName.getText();
        String numOfPlayersText = numOfPlayers.getValue().toString();
        int numberOfPlayers;

        //checks game name validity
        if(game == null || game.isEmpty()){
            showPopup("Invalid input data: game name is null");
            return;
        }

        //checks numberOfPlayers validity
        try{
            numberOfPlayers = Integer.parseInt(numOfPlayersText);
        }catch(NumberFormatException e){
            showPopup("Invalid input data: number of players is null or not a number");
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
        view.refreshGameNames();
    }
}
