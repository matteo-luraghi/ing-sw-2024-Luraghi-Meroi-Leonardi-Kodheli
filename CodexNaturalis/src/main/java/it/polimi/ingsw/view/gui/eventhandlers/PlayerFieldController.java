package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.model.card.*;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

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
    public Pane player2;
    public Pane player3;
    public Pane player4;
    public Label YourName;
    public Circle YourColor;

    private Player playerFieldOwner;
    private Player playerRequesting;
    private ArrayList<Pane> playerPanes;
    private ResourceCard chosenCard;
    private ImageView chosenImage;
    private int chosenIndex;
    private Coordinates chosenCords;
    private ArrayList<Boolean> isFrontList;

    /**
     * method to initialize the controller's parameters
     */
    public void initialize(){
        ObservableList<String> items = FXCollections.observableArrayList();
        chat.setItems(items);
        chosenIndex = -1;
        chosenImage = null;
        chosenCords = null;
        playerPanes = new ArrayList<>();
        playerPanes.add(player1);
        playerPanes.add(player2);
        playerPanes.add(player3);
        playerPanes.add(player4);
        isFrontList = new ArrayList<>(3);
        isFrontList.add(0, true);
        isFrontList.add(1, true);
        isFrontList.add(2, true);
    }

    /**
     * method to show all the scene's elements
     * @param view the view we want to set
     */
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
        //Set the static fields of the players
        view.showStaticContent();
    }

    /**
     * Common goals setter
     * @param commonGoals an array of GoalCards
     */
    public void setCommonGoals(GoalCard[] commonGoals) {
        Image temp;
        temp = new Image(Util.getImageFromID(commonGoals[0].getId(), true));
        commonGoal0.setImage(temp);
        temp = new Image(Util.getImageFromID(commonGoals[1].getId(), true));
        commonGoal1.setImage(temp);
    }

    /**
     * decks setter
     * @param resourceDeck1 resource deck to be set
     * @param goldDeck1 gold deck to be set
     */
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
        //TODO: PlayerField will probably need a List<ResourceCard> to understand in which order to play them in
        //TODO: THIS WILL STACK IMAGES ON TOP OF ONE ANOTHER; CALL THIS ONLY ON NEW PLAYER FIELD

        //Set player zone
        Image temp;
        for(Coordinates c: playerField.getGameZone().keySet()){
            createCard(playerField, c);
        }

        setHand(playerField.getHand(), isYourPlayerfield);

        //Set private goal
        temp = new Image(Util.getImageFromID(playerField.getPrivateGoal().getId(), isYourPlayerfield));
        privateGoal.setImage(temp);
    }

    /**
     * hand setter
     * @param hand to be set
     * @param isYourPlayerfield a boolean telling whether it's the client's field or not
     */
    public void setHand(ArrayList<ResourceCard> hand, boolean isYourPlayerfield){
        Image temp;
        if(hand.getFirst() != null){
            temp = new Image(Util.getImageFromID(hand.getFirst().getId(), isYourPlayerfield && isFrontList.get(0)));
            hand0.setImage(temp);
        }
        if(hand.get(1) != null){
            temp = new Image(Util.getImageFromID(hand.get(1).getId(), isYourPlayerfield && isFrontList.get(1)));
            hand1.setImage(temp);
        }
        if(hand.size() == 3 && hand.get(2) != null){
            temp = new Image(Util.getImageFromID(hand.get(2).getId(), isYourPlayerfield && isFrontList.get(2)));
            hand2.setImage(temp);
            hand2.setVisible(true);
            hand2.setDisable(false);
        } else {
            hand2.setVisible(false);
            hand2.setDisable(true);
        }
    }

    /**
     * method to set the scene's static content
     * @param players that are playing the game
     */
    public void setStaticContent(ArrayList<Player> players) {
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
                if(players.get(i).equals(playerRequesting)){
                    YourName.setText(playerFieldOwner.getNickname());
                    YourColor.setFill(color);
                }
            }
        }
    }

    /**
     * to add a message to the chat
     * @param s the message
     */
    public void addChatMessage(String s) {
        chat.getItems().add(s);
    }

    /**
     * method to display the client's field
     */
    public void showYourField() {
        Player currentPlayer = view.getUser();
        view.ShowPlayerField(currentPlayer, currentPlayer);
    }

    /**
     * method that handles the play of a card
     * @param mouseEvent that triggered the event
     */
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
        view.playCard(chosenIndex, chosenCords, isFrontList.get(chosenIndex));
        isFrontList.remove(chosenIndex);
        chosenIndex = -1;
        isFrontList.add(true);
    }

    /**
     * method that handles the selection of a card to play
     * @param mouseEvent that triggers the event
     */
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

        if(mouseEvent.getButton().equals(MouseButton.SECONDARY)){
            //The user clicked with the right click, flip the card
            isFrontList.set(chosenIndex, !isFrontList.get(chosenIndex));
            String cardPath = chosenImage.getImage().getUrl();
            Image image = new Image(Util.getImageFromID(Integer.parseInt(cardPath.substring(cardPath.lastIndexOf("/")+1, cardPath.lastIndexOf("."))), isFrontList.get(chosenIndex)));
            if(chosenIndex==0){
                hand0.setImage(image);
            }else if (chosenIndex==1){
                hand1.setImage(image);
            }else if (chosenIndex==2){
                hand2.setImage(image);
            }
        }
    }

    /**
     * method that handles the drawing of a card
     * @param mouseEvent that triggers the event
     */
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

    /**
     * method that is called when a card is successfully played
     */
    public void cardPlayOK() {
        //The card got played successfully
        //This only creates the new card
        createCard(view.getYourPlayerField(), chosenCords);
    }

    /**
     * Method that is used to visually create the card that just got played
     * @param playerZone The player zone where the card got played
     * @param where the position where the card got played
     */
    private void createCard(PlayerField playerZone, Coordinates where){
        /*
        [0,0] is at x:0 y:0
        x+1 = +116
        y+1 = -60
         */
        //Find the resource card that just got played
        GameCard card = null;

        // if this thread is faster than the update game one
        // wait until the game is updated and then display the played card
        while(card == null) {
            try {
                playerZone = view.getYourPlayerField();
                card = playerZone.getGameCardByEqualCoordinate(where);
            } catch (NullPointerException ignored) {}
        }

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
        playerField.getChildren().add(pane);
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

    public void setPlayers(Player playerFieldOwner, Player playerRequesting){
        this.playerFieldOwner = playerFieldOwner;
        this.playerRequesting = playerRequesting;
    }
}
