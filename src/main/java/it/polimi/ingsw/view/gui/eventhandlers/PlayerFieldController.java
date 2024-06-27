package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.gamelogic.*;
import it.polimi.ingsw.model.gamelogic.gamechat.GameChat;
import it.polimi.ingsw.model.gamelogic.gamechat.Message;
import it.polimi.ingsw.view.gui.GUI;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * PlayerField.fxml event handler, used to process user input during the actual game
 * * @author Gabriel Leonardi
 */
public class PlayerFieldController extends EventHandler{

    public ListView listOfMessages;
    public ImageView handImage0;
    public ImageView handImage1;
    public ImageView handImage2;
    public ImageView privateGoalImage;
    public ImageView commonGoalImage0;
    public ImageView commonGoalImage1;
    public ImageView resourceDeckImage;
    public ImageView resourceUncoveredImage0;
    public ImageView resourceUncoveredImage1;
    public ImageView goldDeckImage;
    public ImageView goldUncoveredImage0;
    public ImageView goldUncoveredImage1;
    public Group playerFieldContainer;
    public Pane playerPane1;
    public Pane playerPane2;
    public Pane playerPane3;
    public Pane playerPane4;
    public Label yourName;
    public Circle yourColor;
    public TextField currentMessage;
    public ChoiceBox chosenRecipient;
    public ImageView tutorialImage;
    public AnchorPane tutorialImageContainer;
    public AnchorPane rootPane;

    private Player playerFieldOwner;
    private ArrayList<Pane> playerPanes;
    private ImageView chosenImage;
    private int chosenIndex;
    private ArrayList<Boolean> isFrontList;
    private int tutorialImageIndex;

    /**
     * method to initialize the controller's parameters
     */
    public void initialize(){
        //Initiate the chat
        ObservableList<String> items = FXCollections.observableArrayList();
        listOfMessages.setItems(items);
        listOfMessages.getItems().add("Game started!");
        
        //Initiate the local variables
        chosenIndex = -1;
        chosenImage = null;
        isFrontList = new ArrayList<>(3);
        isFrontList.add(0, true);
        isFrontList.add(1, true);
        isFrontList.add(2, true);
        tutorialImageIndex = 0;
        
        //initiate the player panes
        playerPanes = new ArrayList<>();
        playerPanes.add(playerPane1);
        playerPanes.add(playerPane2);
        playerPanes.add(playerPane3);
        playerPanes.add(playerPane4);
    }

    /**
     * method to initialize all the graphic fields though the view
     * @param view the view of the player field owner
     */
    @Override
    public void setView(GUI view){
        this.view = view;

        //Initialize the current player and the player field owner
        setPlayerFieldOwner(view.getUser());

        //Shows player zone, hand and private goal
        view.ShowPlayerField(view.getUser(), view.getUser());

        //Shows both resource and gold decks
        view.ShowDecks();

        //Set the static fields of the players
        view.showStaticContent();

        //Shows the 2 common goals;
        view.showCommonGoals();

        //Set the chat with the messages it already has
        view.showChat();

        //Sets the scoreboard;
        view.showScoreBoard();
    }

    /**
     * Method to set the player field owner
     * @param playerFieldOwner the player who owns the player field
     */
    public void setPlayerFieldOwner(Player playerFieldOwner){
        this.playerFieldOwner = playerFieldOwner;
    }

    /**
     * Sets the images of the common goal
     * @param commonGoals the common goals for this game
     */
    public void setCommonGoals(GoalCard[] commonGoals) {
        Image temp;
        temp = new Image(Util.getImageFromID(commonGoals[0].getId(), true));
        commonGoalImage0.setImage(temp);
        temp = new Image(Util.getImageFromID(commonGoals[1].getId(), true));
        commonGoalImage1.setImage(temp);
    }

    /**
     * Sets the images for the decks
     * @param resourceDeck1 resource deck to be set
     * @param goldDeck1 gold deck to be set
     */
    public void setDecks(Deck resourceDeck1, Deck goldDeck1) {
        Image temp;
        //Set resource deck
        setSpecificDeck(resourceDeck1, resourceDeckImage, resourceUncoveredImage0, resourceUncoveredImage1);

        //Set gold deck
        setSpecificDeck(goldDeck1, goldDeckImage, goldUncoveredImage0, goldUncoveredImage1);
    }

    /**
     * Helper function for setDecks, sets the images to the correct image view if the card is present, hides it otherwise
     * @param deck the current deck
     * @param deckImage the image that corresponds to the top card of the deck
     * @param uncoveredImage0 the image that corresponds to the first uncovered card of the deck
     * @param uncoveredImage1 the image that corresponds to the second uncovered card of the deck
     */
    private void setSpecificDeck(Deck deck, ImageView deckImage, ImageView uncoveredImage0, ImageView uncoveredImage1) {
        Image temp;
        try{
            temp = new Image(Util.getImageFromID(deck.getTopCard().getId(), false));
            deckImage.setImage(temp);
        } catch (NullPointerException e) {
            deckImage.setDisable(true);
            deckImage.setVisible(false);
        }

        try{
            temp = new Image(Util.getImageFromID(deck.getUncoveredCards()[0].getId(), true));
            uncoveredImage0.setImage(temp);
        } catch (NullPointerException e) {
            uncoveredImage0.setDisable(true);
            uncoveredImage0.setVisible(false);
        }

        try{
            temp = new Image(Util.getImageFromID(deck.getUncoveredCards()[1].getId(), true));
            uncoveredImage1.setImage(temp);
        } catch (NullPointerException e) {
            uncoveredImage1.setDisable(true);
            uncoveredImage1.setVisible(false);
        }
    }

    /**
     * scoreboard setter
     * @param scoreBoard to be set
     */
    public void setScoreBoard(ScoreBoard scoreBoard) {
        for(Pane pane : playerPanes) {
            String playerNick = ((Label) getChildrenFromID(pane, "playerName")).getText();
            for (Player player : scoreBoard.getBoard().keySet()) {
                if (player.getNickname().equalsIgnoreCase(playerNick)) {
                    ((Label) getChildrenFromID(pane, "points")).setText("Points: " + scoreBoard.getBoard().getOrDefault(player, 0).toString());
                }
            }
        }
    }

    /**
     * resource map setter
     * @param resourceMaps to be set
     */
    public void setResourceMaps(Map<Player, Map<Resource, Integer>> resourceMaps) {
        for(Player player: resourceMaps.keySet()){
            Pane currentPlayerPane = null;
            for(Pane pane : playerPanes){
                if(((Label) getChildrenFromID(pane, "playerName")).getText().equalsIgnoreCase(player.getNickname())){
                    currentPlayerPane = pane;
                    break;
                }
            }
            Map<Resource, Integer> resourceMap = resourceMaps.get(player);
            if(currentPlayerPane != null) {
                //Set the text of the labels to the current value contained
                ((Label) getChildrenFromID(currentPlayerPane, "animal")).setText(resourceMap.get(Resource.ANIMAL).toString());
                ((Label) getChildrenFromID(currentPlayerPane, "fungi")).setText(resourceMap.get(Resource.FUNGI).toString());
                ((Label) getChildrenFromID(currentPlayerPane, "insect")).setText(resourceMap.get(Resource.INSECT).toString());
                ((Label) getChildrenFromID(currentPlayerPane, "plant")).setText(resourceMap.get(Resource.PLANT).toString());
                ((Label) getChildrenFromID(currentPlayerPane, "potion")).setText(resourceMap.get(Resource.POTION).toString());
                ((Label) getChildrenFromID(currentPlayerPane, "scroll")).setText(resourceMap.get(Resource.SCROLL).toString());
                ((Label) getChildrenFromID(currentPlayerPane, "feather")).setText(resourceMap.get(Resource.FEATHER).toString());
            }
        }
    }

    /**
     * player field setter
     * @param playerField to be set
     * @param isYourPlayerfield a boolean telling whether it's the client's field or not
     */
    public void setPlayerField(PlayerField playerField, boolean isYourPlayerfield) {
        //Delete the previous player field
        playerFieldContainer.getChildren().clear();

        //Create the player field
        for(GameCard card: playerField.getInPlayOrderList()){
            Coordinates coordinate = Util.getKeyByValue(playerField.getGameZone(), card);
            createCard(playerField, coordinate);
        }

        //Set hand
        setHand(playerField.getHand(), isYourPlayerfield);

        //Set private goal
        Image temp = new Image(Util.getImageFromID(playerField.getPrivateGoal().getId(), isYourPlayerfield));
        privateGoalImage.setImage(temp);
    }

    /**
     * Updates the currently viewed player field when it gets updated
     * @param game the current game state
     */
    public void setCurrentPlayerField(GameState game) {
        boolean isYourPlayerField = playerFieldOwner.equals(view.getUser());
        PlayerField currentPlayerField = game.getGameTable().getPlayerZoneForUser(playerFieldOwner.getNickname());
        setPlayerField(currentPlayerField, isYourPlayerField);
    }

    /**
     * hand setter
     * @param hand to be set
     * @param isYourPlayerField a boolean telling whether it's the client's field or not
     */
    public void setHand(ArrayList<ResourceCard> hand, boolean isYourPlayerField){
        Image temp;
        if(!hand.isEmpty() && hand.getFirst() != null){
            temp = new Image(Util.getImageFromID(hand.getFirst().getId(), isYourPlayerField && isFrontList.get(0)), 300, 200, true, true);
            handImage0.setImage(temp);
        } else {
            handImage0.setVisible(false);
            handImage0.setDisable(true);
        }

        if(hand.size() >= 2 && hand.get(1) != null){
            temp = new Image(Util.getImageFromID(hand.get(1).getId(), isYourPlayerField && isFrontList.get(1)), 300, 200, true, true);
            handImage1.setImage(temp);
        } else {
            handImage1.setVisible(false);
            handImage1.setDisable(true);
        }

        if(hand.size() == 3 && hand.get(2) != null){
            temp = new Image(Util.getImageFromID(hand.get(2).getId(), isYourPlayerField && isFrontList.get(2)), 300, 200, true, true);
            handImage2.setImage(temp);
            handImage2.setVisible(true);
            handImage2.setDisable(false);
        } else {
            handImage2.setVisible(false);
            handImage2.setDisable(true);
        }
    }

    /**
     * method to set the scene's static content
     * @param players that are playing the game
     */
    public void setStaticContent(ArrayList<Player> players) {
        ObservableList<String> items = FXCollections.observableArrayList();
        items.add("all");
        for(int i = 0; i < 4; i++){
            Pane currentPane = playerPanes.get(i);
            if(i >= players.size()){
                currentPane.setVisible(false);
                currentPane.setDisable(true);
                currentPane.setMaxHeight(0);
            } else {
                //Set the nickname and the color of each playerPane
                ((Label) getChildrenFromID(currentPane, "playerName")).setText(players.get(i).getNickname());
                Color color;
                switch (players.get(i).getColor()){
                    case RED -> color = Color.RED;
                    case BLUE -> color = Color.BLUE;
                    case GREEN -> color = Color.GREEN;
                    case YELLOW -> color = Color.YELLOW;
                    case null, default -> color = Color.WHITE;
                }
                ((Circle) getChildrenFromID(currentPane, "color")).setFill(color);
                if(players.get(i).equals(view.getUser())){
                    yourName.setText(view.getUser().getNickname());
                    yourColor.setFill(color);
                } else {
                    items.add(players.get(i).getNickname());
                }
            }
        }
        chosenRecipient.setItems(items);
        chosenRecipient.setValue("all");
    }

    /**
     * method to display the last message sent
     * @param gameChat the updated game chat
     */
    public void setListOfMessages(GameChat gameChat){
        if(gameChat == null){
            return;
        }
        try{
            addChatMessage(gameChat.getLastMessage().toStringGUI());
        } catch (NoSuchElementException ignored){};

    }

    /**
     * Save the message in the chat
     * @param text the message
     */
    public void addChatMessage(String text){
        listOfMessages.getItems().add(text);
        int index = listOfMessages.getItems().size()-1;
        listOfMessages.scrollTo(index);
    }

    /**
     * Method that is called when clicking the enter button
     * Send the written message to the desired recipient if the text is not null
     */
    public void sendChatMessage() {

        if(view.getUser().getNickname().equals(playerFieldOwner.getNickname()) && !currentMessage.getText().trim().equals("")){
            //Get the message
            String message = currentMessage.getText().trim();

            //Get the desired
            String user = chosenRecipient.getValue().toString();

            //Send the message
            Message msg = new Message(message, playerFieldOwner, user);
            view.getClient().sendMessageInChat(msg);
        }
        currentMessage.setText("");
    }

    /**
     * When the enter key is pressed on the chat box, send the message
     * @param keyEvent the event that triggered this function call
     */
    public void sendChatMessageEnter(KeyEvent keyEvent){
        if(keyEvent.getCode().equals(KeyCode.ENTER)){
            sendChatMessage();
        }
    }

    /**
     * method to display the player field of the user
     */
    public void showYourField() {
        view.ShowPlayerField(view.getUser(), view.getUser());
    }


    /**
     * Method that is used to visually create a card present in a player field
     * @param playerZone The player zone where the card got played
     * @param where the position where the card got played
     */
    private void createCard(PlayerField playerZone, Coordinates where){
        /*
        [0,0] is at x:0 y:0
        x+1 = +116
        y+1 = -60
         */

        GameCard card = playerZone.getGameCardByEqualCoordinate(where);

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
        Image image = new Image(Util.getImageFromID(card.getId(), card.getIsFront()));
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
        playerFieldContainer.getChildren().add(pane);
    }

    /**
     * Method to create the invisible button necessary to be able to play cards
     * @param x the x coordinate that the button points too
     * @param y the x coordinate that the button points too
     * @param isRight true if the button is on the right side of the card, false otherwise
     * @param isDown true if the button is on the bottom side of the card, false otherwise
     * @return the created button
     */
    private Button createButton(int x, int y, boolean isRight, boolean isDown){
        Button button = new Button();
        String text = "[" + x + "," + y + "]";
        button.setText(text);
        button.setStyle("-fx-cursor: hand; -fx-background-color: transparent; -fx-text-fill: transparent;");
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
        button.setOnMouseClicked(this::playCardClick);

        //Gets called when the image is getting dragged over
        button.setOnDragOver(new javafx.event.EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                if(event.getDragboard().hasImage()){
                    event.acceptTransferModes(TransferMode.MOVE);
                }
                event.consume();
            }
        });

        //gets called when the image is dropped onto a button
        button.setOnDragDropped(new javafx.event.EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if(db.hasImage()){
                    playCardDrop(button.getText());
                } else {
                    view.showMessage("No image chosen");
                }
                event.consume();
            }
        });

        return button;
    }


    /**
     * method that handles the selection of a card to play
     * @param mouseEvent that triggers the event
     */
    public void selectPlayCardClick(MouseEvent mouseEvent) {
        if(!view.getUser().equals(playerFieldOwner)){
            view.showMessage("You aren't the owner of this player field");
            return;
        }

        chosenImage = (ImageView) mouseEvent.getSource();
        if(chosenImage.equals(handImage0)){
            chosenIndex = 0;
        } else if(chosenImage.equals(handImage1)){
            chosenIndex = 1;
        } else if(chosenImage.equals(handImage2)){
            chosenIndex = 2;
        }

        if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
            //The user clicked with the right click, flip the card

            isFrontList.set(chosenIndex, !isFrontList.get(chosenIndex));
            String cardPath = chosenImage.getImage().getUrl();
            Image image = new Image(Util.getImageFromID(Integer.parseInt(cardPath.substring(cardPath.lastIndexOf("/")+1, cardPath.lastIndexOf("."))), isFrontList.get(chosenIndex)), 300, 200, true, true);
            if(chosenIndex==0){
                handImage0.setImage(image);
            }else if (chosenIndex==1){
                handImage1.setImage(image);
            }else if (chosenIndex==2){
                handImage2.setImage(image);
            }
        }
    }

    /**
     * Method that gets called when the user starts dragging a card in his hand
     * @param event the mouse event that triggered this function call
     */
    public void startDragCard(MouseEvent event){
        chosenImage = (ImageView) event.getSource();
        if(chosenImage.equals(handImage0)){
            chosenIndex = 0;
        } else if(chosenImage.equals(handImage1)){
            chosenIndex = 1;
        } else if(chosenImage.equals(handImage2)){
            chosenIndex = 2;
        }

        Dragboard db = chosenImage.startDragAndDrop(TransferMode.MOVE);

        ClipboardContent content = new ClipboardContent();

        content.putImage(chosenImage.getImage());
        db.setContent(content);
        event.consume();
    }

    /**
     * method that handles the play of a card
     * @param mouseEvent that triggered the event
     */
    public void playCardClick(MouseEvent mouseEvent) {
        if(!view.getUser().equals(playerFieldOwner)){
            view.showMessage("You aren't the owner of this player field");
            return;
        }
        if(chosenIndex == -1){
            view.showMessage("Select a card to play first");
            return;
        }
        
        String coordinateString = ((Button) mouseEvent.getSource()).getText();
        Coordinates chosenCords = parseCoordinateFromString(coordinateString);

        view.playCard(chosenIndex, chosenCords, isFrontList.get(chosenIndex));
        isFrontList.remove(chosenIndex);
        chosenIndex = -1;
        isFrontList.add(true);
    }

    /**
     * Plays the dragged card in the dropped coordinate
     * @param coordinateString the coordinate where you want to play the card
     */
    private void playCardDrop(String coordinateString) {
        Coordinates chosenCords = parseCoordinateFromString(coordinateString);
        view.playCard(chosenIndex, chosenCords, isFrontList.get(chosenIndex));
        isFrontList.remove(chosenIndex);
        chosenIndex = -1;
        isFrontList.add(true);
    }


    /**
     * method that handles the drawing of a card
     * @param mouseEvent that triggers the event
     */
    public void drawCard(MouseEvent mouseEvent) {
        if(!view.getUser().equals(playerFieldOwner)){
            view.showMessage("You aren't the owner of this player field");
            return;
        }

        chosenImage = (ImageView) mouseEvent.getSource();
        boolean isGold;

        //Select the chosen card
        if(chosenImage.equals(resourceDeckImage)){
            chosenIndex = 0;
            isGold = false;
        } else if(chosenImage.equals(resourceUncoveredImage0)){
            chosenIndex = 1;
            isGold = false;
        } else if(chosenImage.equals(resourceUncoveredImage1)){
            chosenIndex = 2;
            isGold = false;
        } else if(chosenImage.equals(goldDeckImage)){
            chosenIndex = 0;
            isGold = true;
        } else if(chosenImage.equals(goldUncoveredImage0)){
            chosenIndex = 1;
            isGold = true;
        } else if(chosenImage.equals(goldUncoveredImage1)){
            chosenIndex = 2;
            isGold = true;
        } else {
            return;
        }

        view.getClient().drawCardResponse(chosenIndex, isGold);
    }


    /**
     * Method that is called when clicking on another player's scoreboard
     * Shows the player field of the other player
     * @param mouseEvent the event that called this function
     */
    public void requestPlayerField(MouseEvent mouseEvent) {
        String targetUser = ((Label)getChildrenFromID((Pane) mouseEvent.getSource(), "playerName")).getText();
        if(!targetUser.equals(playerFieldOwner.getNickname())){
            view.ShowPlayerFieldFromName(targetUser, view.getUser());
        }
    }

    /**
     * Method that gets called when clicking on the tutorial button
     * Initializes the tutorial sequence and shows the first image
     */
    public void startTutorialSequence() {
        tutorialImageIndex = 0;
        tutorialImageContainer.setVisible(true);
        tutorialImageContainer.setDisable(false);
        nextTutorialSequence();
    }

    /**
     * Method that gets called when clicking on a tutorial image or from startTutorialSequence
     * Shows the current tutorial image or finishes the tutorial sequence
     */
    public void nextTutorialSequence() {
        tutorialImageIndex++;
        if(tutorialImageIndex <= 5){
            String tutorialPath = Util.getTutorialImageByID(tutorialImageIndex);
            Image temp = new Image(tutorialPath);
            tutorialImage.setImage(temp);
            tutorialImage.setFitWidth(rootPane.getScene().getWidth());
            tutorialImage.setFitHeight(rootPane.getScene().getHeight());
            tutorialImage.setPreserveRatio(false);
        } else {
            tutorialImageContainer.setVisible(false);
            tutorialImageContainer.setDisable(true);
        }
    }

    /**
     * Function to convert a button text to a coordinate
     * @param coordinateString the text of the button
     * @return the coordinate that got referenced by the button
     */
    private Coordinates parseCoordinateFromString(String coordinateString){
        int x, y;
        coordinateString = coordinateString.substring(1, coordinateString.length() - 1);
        x = Integer.parseInt(coordinateString.split(",")[0]);
        y = Integer.parseInt(coordinateString.split(",")[1]);
        return new Coordinates(x,y);
    }
}