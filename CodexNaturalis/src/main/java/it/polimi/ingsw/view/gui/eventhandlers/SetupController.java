package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.StartingCard;
import javafx.scene.control.RadioButton;
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

    private StartingCard startingCard;
    private GoalCard[] goalCards;

    /**
     * Sets the randomly assigned starting card and shows both side to the player
     * @param startingCard the starting card
     */
    public void setStartingCard(StartingCard startingCard){
        this.startingCard = startingCard;
        //TODO: Set the starting card images
    }

    /**
     * Sets the randomly assigned goal cards and shows both of them to the player
     * @param goalCards the goal cards
     */
    public void setGoalCard(GoalCard[] goalCards){
        this.goalCards = goalCards;
        //TODO: Set the goal cards images
    }

    /**
     * Function that gets called when the page loads
     */
    public void initialize(){

    }

    public void SelectStartingAndGoal() {
        //Should probably be split up in SelectStarting e selectGoal
    }
}
