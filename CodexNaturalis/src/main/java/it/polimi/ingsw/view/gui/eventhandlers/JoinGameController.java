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

public class JoinGameController  extends EventHandler{

    public ListView gamesList;
    public TextField gameName;
    public ChoiceBox numOfPlayers;
    private ArrayList<String> gameNames;
    public void setGameNames(ArrayList<String> gameNames){
        this.gameNames = gameNames;
        ObservableList<String> items = FXCollections.observableArrayList(gameNames);
        gamesList.setItems(items);
    }

    public void initialize(){
        ArrayList<Integer> nums = new ArrayList<>();
        nums.add(2);
        nums.add(3);
        nums.add(4);
        ObservableList<Integer> choices = FXCollections.observableArrayList(nums);
        numOfPlayers.setItems(choices);
        numOfPlayers.setValue(nums.getFirst());
    }

    public void joinGame(MouseEvent mouseEvent) {
        System.out.println("clicked on " + gamesList.getSelectionModel().getSelectedItem());
    }

    public void createGame(MouseEvent mouseEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Invalid input data");
        alert.setHeaderText("Invalid input data");

        String game = gameName.getText();
        String numOfPlayersText = numOfPlayers.getValue().toString();
        int numberOfPlayers;
        if(game == null || game.isEmpty()){
            alert.setContentText("Game name is null");
            alert.showAndWait();
            return;
        }
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
    }

    public void refreshGame(MouseEvent mouseEvent) {
        //TODO:Implement after controller method
    }
}
