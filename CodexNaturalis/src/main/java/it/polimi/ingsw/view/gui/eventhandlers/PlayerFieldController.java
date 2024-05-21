package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.model.card.GameCard;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.Resource;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.gamelogic.*;
import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.Map;
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
    public ImageView startingCard;
    public Group playerField;
    public Pane player1;

    private ResourceCard chosenCard;
    private ImageView chosenImage;
    private int chosenIndex;
    private boolean isFront;
    private Coordinates chosenCords;
    public void initialize(){
        ObservableList<String> items = FXCollections.observableArrayList();
        chat.setItems(items);
        chosenIndex = -1;
        chosenImage = null;
        chosenCords = null;

    }

    @Override
    public void setView(GUI view){
        this.view = view;
        //Initialize all the graphic fields though the view
        showYourField(); //Shows player zone, hand and private goal
        //Shows the resource maps of all players
        view.showResourceMaps();
        //Shows the points that every player has
        view.ShowScoreBoard();
        //Shows both resource and gold decks
        view.ShowDecks();
        //Shows the 2 common goals;
        view.showCommonGoals();
        //Set the static part of the player areas
        Label playerName = (Label) getChildrenFromID(player1, "playerName");
        playerName.setText(view.getUser().getNickname());

    }

    public void setCommonGoals(GoalCard[] commonGoals) {
        Image temp;
        temp = new Image(Util.getImageFromID(commonGoals[0].getId(), true));
        commonGoal0.setImage(temp);
        temp = new Image(Util.getImageFromID(commonGoals[1].getId(), true));
        commonGoal1.setImage(temp);
    }

    public void setDecks(Deck resourceDeck1, Deck goldDeck1) {
        Image temp;
        //Set resource deck
        if(!resourceDeck1.isDeckEmpty()){
            temp = new Image(Util.getImageFromID(resourceDeck1.getTopCard().getId(), false));
            resourceDeck.setImage(temp);
        }
        if(resourceDeck1.getUncoveredCards()[0] != null){
            temp = new Image(Util.getImageFromID(resourceDeck1.getUncoveredCards()[0].getId(), true));
            resourceUncovered0.setImage(temp);
        }
        if(resourceDeck1.getUncoveredCards()[1] != null){
            temp = new Image(Util.getImageFromID(resourceDeck1.getUncoveredCards()[1].getId(), true));
            resourceUncovered1.setImage(temp);
        }

        //Set gold deck
        if(!goldDeck1.isDeckEmpty()){
            temp = new Image(Util.getImageFromID(goldDeck1.getTopCard().getId(), false));
            goldDeck.setImage(temp);
        }
        if(goldDeck1.getUncoveredCards()[0] != null){
            temp = new Image(Util.getImageFromID(goldDeck1.getUncoveredCards()[0].getId(), true));
            goldUncovered0.setImage(temp);
        }
        if(goldDeck1.getUncoveredCards()[1] != null){
            temp = new Image(Util.getImageFromID(goldDeck1.getUncoveredCards()[1].getId(), true));
            goldUncovered1.setImage(temp);
        }
    }

    public void setScoreBoard(ScoreBoard scoreBoard) {
        //TODO: Implement when fxml is finished
    }

    public void setResourceMaps(Map<Player, Map<Resource, Integer>> resourceMaps) {
        //TODO: Implement when fxml is finished
    }

    public void setPlayerField(PlayerField playerField, boolean isYourPlayerfield) {
        //TODO: PlayerField will probably need a List<ResourceCard> to understand in which order to play them in
        //TODO: THIS WILL STACK IMAGES ON TOP OF ONE ANOTHER; CALL THIS ONLY ON NEW PLAYER FIELD

        //Set player zone
        Image temp;
        for(Coordinates c: playerField.getGameZone().keySet()){
            if(c.getX() == 0 && c.getY() == 0){
                temp = new Image(Util.getImageFromID(playerField.getGameZone().get(c).getId(), playerField.getGameZone().get(c).getIsFront()));
                startingCard.setImage(temp);
            } else {
                createCard(playerField, c);
            }
        }

        setHand(playerField.getHand(), isYourPlayerfield);

        //Set private goal
        temp = new Image(Util.getImageFromID(playerField.getPrivateGoal().getId(), isYourPlayerfield));
        privateGoal.setImage(temp);
    }

    public void setHand(ArrayList<ResourceCard> hand, boolean isYourPlayerfield){
        Image temp;
        if(hand.getFirst() != null){
            temp = new Image(Util.getImageFromID(hand.getFirst().getId(), isYourPlayerfield));
            hand0.setImage(temp);
        }
        if(hand.get(1) != null){
            temp = new Image(Util.getImageFromID(hand.get(1).getId(), isYourPlayerfield));
            hand1.setImage(temp);
        }
        if(hand.size() == 3 && hand.get(2) != null){
            temp = new Image(Util.getImageFromID(hand.get(2).getId(), isYourPlayerfield));
            hand2.setImage(temp);
            hand2.setVisible(true);
            hand2.setDisable(false);
        } else {
            hand2.setVisible(false);
            hand2.setDisable(true);
        }
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
        chosenCords = new Coordinates(x,y);
        view.playCard(chosenIndex, chosenCords, isFront);
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
    }

    public void drawCard(MouseEvent mouseEvent) {
        chosenImage = (ImageView) mouseEvent.getSource();
        boolean isGold;

        //Select the chosen card
        if(chosenImage.equals(resourceDeck)){
            chosenIndex = 0;
            isGold = false;
        } else if(chosenImage.equals(resourceUncovered0)){
            chosenIndex = 1;
            isGold = false;
        } else if(chosenImage.equals(resourceUncovered1)){
            chosenIndex = 2;
            isGold = false;
        } else if(chosenImage.equals(goldDeck)){
            chosenIndex = 0;
            isGold = true;
        } else if(chosenImage.equals(goldUncovered0)){
            chosenIndex = 1;
            isGold = true;
        } else if(chosenImage.equals(goldUncovered1)){
            chosenIndex = 2;
            isGold = true;
        } else {
            view.showMessage("How are you trying to draw a card that is not in a deck");
            return;
        }

        view.getClient().drawCardResponse(chosenIndex, isGold);
    }

    public void cardPlayOK() {
        //The card got played succesfully, either i create a new card and add it to the screen or i request the whole playerzone again
        createCard(view.getYourPlayerField(), chosenCords); //This only creates the new card
    }

    private void createCard(PlayerField playerZone, Coordinates where){
        /*
        [0,0] is at x:0 y:0
        x+1 = +116
        y+1 = -60  I dont know why -60
         */

        //Find the resource card that just got played
        ResourceCard card = (ResourceCard) playerZone.getGameCardByEqualCoordinate(where);

        //Create the pane
        int newX = where.getX() * 116;
        int newY = where.getY() * -60;
        Pane pane = new Pane();
        pane.setLayoutX(newX);
        pane.setLayoutY(newY);
        pane.setPrefWidth(150);
        pane.setPrefHeight(100);

        //Create the ImageView
        ImageView imageView = new ImageView();
        Image image = new Image(Util.getImageFromID(card.getId(), isFront));
        imageView.setImage(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(150);
        imageView.setPickOnBounds(true);
        imageView.setPreserveRatio(true);

        //Create the 4 buttons (if needed)
        ArrayList<Button> buttons = new ArrayList<>();
        //Top left
        if((!card.getIsFront() || (card.getCorner(0) != Resource.COVERED && card.getCorner(0) != Resource.HIDDEN)) && playerZone.getUpLeft(card) == null){
            buttons.add(createButton(where.getX()-1, where.getY()+1, false, false));
        }
        //Top right
        if((!card.getIsFront() || (card.getCorner(1) != Resource.COVERED && card.getCorner(1) != Resource.HIDDEN)) && playerZone.getUpRight(card) == null){
            buttons.add(createButton(where.getX()+1, where.getY()+1, true, false));
        }
        //Bottom left
        if((!card.getIsFront() || (card.getCorner(2) != Resource.COVERED && card.getCorner(2) != Resource.HIDDEN)) && playerZone.getDownLeft(card) == null){
            buttons.add(createButton(where.getX()-1, where.getY()-1, false, true));
        }
        //Bottom right
        if((!card.getIsFront() || (card.getCorner(3) != Resource.COVERED && card.getCorner(3) != Resource.HIDDEN)) && playerZone.getDownRight(card) == null){
            buttons.add(createButton(where.getX()+1, where.getY()-1, true, true));
        }

        //Add everything to the pane
        pane.getChildren().add(imageView);

        for(Button button : buttons){
            pane.getChildren().add(button);
        }
        //Append the pane to the group
        playerField.getChildren().add(pane);
    }

    private Button createButton(int x, int y, boolean isRight, boolean isDown){
        Button button = new Button();
        String text = "[" + x + "," + y + "]";
        button.setText(text);
        button.setOpacity(0);
        if(isRight){
            button.setLayoutX(116);
        }
        if(isDown){
            button.setLayoutY(62);
        }
        button.setPrefHeight(38);
        button.setPrefWidth(34);
        button.setMaxHeight(38);
        button.setMaxWidth(34);
        button.setMinHeight(38);
        button.setMinWidth(34);
        button.setOnMouseClicked(this::playCard);
        button.setStyle("-fx-cursor: hand;");

        return button;
    }
}
