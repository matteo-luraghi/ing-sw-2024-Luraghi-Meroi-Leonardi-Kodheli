package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.gamelogic.Coordinates;
import it.polimi.ingsw.model.gamelogic.Player;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.util.Optional;

public class PlayerFieldController extends EventHandler{

    public ListView chat;
    public ScrollPane scrollPane;
    public ImageView hand0;
    public ImageView hand1;
    public ImageView hand2;
    public ImageView privateGoal;
    public ImageView commonGoal0;
    public ImageView commonGoal1;
    public ImageView resourceDeck;
    public ImageView resourceUncovered0;
    public ImageView resourceUncovered1;
    public ImageView goldDeck;
    public ImageView goldUncovered0;
    public ImageView goldUncovered1;

    private ResourceCard chosenCard;
    private ImageView chosenImage;
    private int chosenIndex;
    private boolean isFront;
    public void initialize(){
        ObservableList<String> items = FXCollections.observableArrayList();
        chat.setItems(items);
        chosenIndex = -1;
        chosenImage = null;
    }

    public void addChatMessage(String s) {
        chat.getItems().add(s);
    }

    public void showYourField() {
        Player currentPlayer = view.getUser();
        view.ShowPlayerField(currentPlayer, currentPlayer);
    }

    public void playCard(MouseEvent mouseEvent) {
        if(chosenIndex == -1){
            view.showMessage("Select a card to play first");
            return;
        }
        String coordinateString = ((Button) mouseEvent.getSource()).getText();
        int x = 0, y = 0;
        coordinateString = coordinateString.substring(1, coordinateString.length() - 1);
        x = Integer.parseInt(coordinateString.split(",")[0]);
        y = Integer.parseInt(coordinateString.split(",")[1]);
        view.playCard(chosenIndex, new Coordinates(x,y), isFront);
    }

    public void selectPlayCard(MouseEvent mouseEvent) {
        chosenImage = (ImageView) mouseEvent.getSource();
        if(chosenImage.equals(hand0)){
            chosenIndex = 0;
        } else if(chosenImage.equals(hand1)){
            chosenIndex = 1;
        } else if(chosenImage.equals(hand2)){
            chosenIndex = 2;
        } else {
            view.showMessage("How are you trying to play a card that is not in your hand");
            return;
        }

        //Let the user select the side they prefer
        TextInputDialog dialog = new TextInputDialog("Choose a side");
        dialog.setTitle("Choose a side");
        dialog.setHeaderText("Which side do you want to play this card on?");
        dialog.setContentText("Only type front or back");
        boolean correct = false;
        while(!correct){
            Optional<String> optionalSide = dialog.showAndWait();
            if(optionalSide.isPresent()){
                String side = optionalSide.get();
                if(side.equalsIgnoreCase("front") || side.equalsIgnoreCase("back")){
                    isFront = side.equalsIgnoreCase("front");
                    correct = true;
                } else {
                    showPopup("Error", "Insert either front or back");
                }
            }
        }

        if(!isFront){
            //TODO:Take the current card and flip it
        }
    }

    public void drawCard(MouseEvent mouseEvent) {
    }
}
