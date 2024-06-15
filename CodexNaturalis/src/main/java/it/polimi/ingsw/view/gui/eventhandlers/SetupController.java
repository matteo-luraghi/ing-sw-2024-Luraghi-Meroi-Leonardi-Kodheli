package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Util;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

/**
 * Setup.fxml event handler, used to make the player chose a starting card side and a private goal card
 */
public class SetupController extends EventHandler{
    public ImageView startingImageFront;
    public ImageView startingImageBack;
    public RadioButton startingFront;
    public RadioButton startingBack;
    public ImageView goalImage1;
    public ImageView goalImage2;
    public RadioButton goal1;
    public RadioButton goal2;
    public Button submitButton;

    private StartingCard startingCard;
    private GoalCard[] goalCards;

    /**
     * Sets the randomly assigned starting card and shows both side to the player
     * @param startingCard the starting card
     */
    public void setStartingCard(StartingCard startingCard){
        this.startingCard = startingCard;

        Image image1 = new Image(Util.getImageFromID(startingCard.getId(), true));
        startingImageFront.setImage(image1);
        Image image2 = new Image(Util.getImageFromID(startingCard.getId(), false));
        startingImageBack.setImage(image2);
    }

    /**
     * Sets the randomly assigned goal cards and shows both of them to the player
     * @param goalCards the goal cards
     */
    public void setGoalCards(GoalCard[] goalCards){
        this.goalCards = goalCards;
        //Set the images
        Image image1 = new Image(Util.getImageFromID(goalCards[0].getId(), true));
        goalImage1.setImage(image1);
        Image image2 = new Image(Util.getImageFromID(goalCards[1].getId(), true));
        goalImage2.setImage(image2);
    }

    /**
     * Function that gets called when the page loads
     */
    public void initialize(){
        startingFront.setSelected(true);
        goal1.setSelected(true);
    }

    /**
     * Function that gets called when you click the correlated button
     * Selects the starting card side and the preferred goal card at the same time
     */
    public void SelectStartingAndGoal() {
        if(startingFront.isSelected() == startingBack.isSelected() || goal1.isSelected() == goal2.isSelected()){
            //If both radio buttons or neither are selected, throw an alert
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Invalid input data");
            alert.setHeaderText("Invalid input data");
            alert.setContentText("Select one between every group of radio buttons");
            alert.getDialogPane().setStyle(" -fx-background-color: #ede3ba;" +
                    "-fx-font-family: Cambria;" +
                    "-fx-font-style: italic;" +
                    "-fx-font-size: large;" +
                    "-fx-font-weight: bold;");
            alert.showAndWait();
        } else {
            view.getClient().playStartingCardResponse(startingCard, startingFront.isSelected());
            int goalIndex = goal1.isSelected() ? 0 : 1;
            view.getClient().goalCardResponse(goalCards[goalIndex]);

            //Disable everything to disallow overrides
            goal1.setDisable(true);
            goal2.setDisable(true);
            startingFront.setDisable(true);
            startingBack.setDisable(true);
            goalImage1.setDisable(true);
            goalImage2.setDisable(true);
            startingImageBack.setDisable(true);
            startingImageFront.setDisable(true);
            submitButton.setDisable(true);
        }
    }

    /**
     * Function that gets called when you click the correlated button
     * Selects this starting card side as the preferred one
     * @param mouseEvent the event that generated this function call
     */
    public void selectStartingCard(MouseEvent mouseEvent) {
        if(mouseEvent.getSource().equals(startingImageFront)){
            startingBack.setSelected(false);
            startingFront.setSelected(true);
        } else {
            startingBack.setSelected(true);
            startingFront.setSelected(false);
        }
    }

    /**
     * Function that gets called when you click the correlated button
     * Selects this goal card as the preferred one
     * @param mouseEvent the event that generated this function call
     */
    public void selectGoalCard(MouseEvent mouseEvent) {
        if(mouseEvent.getSource().equals(goalImage1)){
            goal2.setSelected(false);
            goal1.setSelected(true);
        } else {
            goal2.setSelected(true);
            goal1.setSelected(false);
        }
    }
}
