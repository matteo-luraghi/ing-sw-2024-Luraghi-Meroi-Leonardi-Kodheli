package it.polimi.ingsw.connection;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.RemoteController;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.StartingCard;
import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.ScoreBoard;
import it.polimi.ingsw.model.gamelogic.gamechat.GameChat;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * ConnectionHandler class
 * used to handle the different types of connection
 * @author Matteo Leonardo Luraghi
 */
public abstract class ConnectionHandler implements Serializable {
    @Serial
    private static final long serialVersionUID = 4169861712628723407L;
    private String clientNickname = null;
    private Color clientColor = null;
    private RemoteController controller;

    /**
     * Get the client's nickname
     * @return nickname of the client
     */
    public String getClientNickname(){
        return this.clientNickname;
    }

    /**
     * Set the client's nickname
     * @param nickname nickname to set
     */
    public void setClientNickname(String nickname) {
        this.clientNickname = nickname;
    }

    /**
     * Get the client's color
     * @return the color
     */
    public Color getClientColor() {
        return this.clientColor;
    }

    /**
     * Set the client's color
     * @param color the color to set
     */
    public void setClientColor(Color color) {
        this.clientColor = color;
    }

    /**
     * Controller getter
     * @return the client's controller
     */
    public Controller getController() {
        return (Controller) this.controller;
    }

    /**
     * Controller setter
     * @param controller to be set
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Ask the player to choose a game to join or create a new game
     * @param gameNames the names of not started games
     */
    public abstract void joinGameRequest(ArrayList<String> gameNames);

    /**
     * Ask the player for their color
     * @param availableColors the possible colors
     */
    public void colorRequest(ArrayList<Color> availableColors) {}

    /**
     * Ask the player for the number of players in the new game
     */
    public void playersNumberRequest() {}

    /**
     * Send the player a message
     * @param message the message
     */
    public void sendTextMessage(String message) {}

    /**
     * Tell the player that the game will start when there are enough players
     */
    public void waitingForPlayers() {}

    /**
     * Set the player on the client
     * @param player the player
     */
    public void setPlayer(Player player) {}

    /**
     * Ask the player to place the starting card (front or back)
     * @param startingCard the starting card
     */
    public void playStartingCardRequest(StartingCard startingCard) {}

    /**
     * Ask the player to choose one goal card
     * @param goalCards the goal cards
     */
    public void goalCardRequest(GoalCard[] goalCards) {}

    /**
     * Update the game when a card is played or drawn
     * @param game the current game
     */
    public void updateGame(GameState game) {}

    /**
     * Tell the player that it's not their turn
     * @param message the message
     */
    public void notYourTurn(String message) {}

    /**
     * Tell the player that it's their turn to play
     */
    public void yourTurn() {}

    /**
     * Wait for user commands
     */
    public void listenForCommands() {}

    /**
     * Ask the player to play a card
     * @param player the player
     */
    public void playCardRequest(Player player) {}

    /**
     * Ask the player to draw a card
     * @param player the player
     */
    public void drawCardRequest(Player player) {}

    /**
     * Show the winner
     * @param game the current game
     */
    public void showWinner(GameState game) {};

    /**
     * Show the scoreboard of the game
     * @param scoreBoard the scoreboard
     */
    public void showScoreBoard(ScoreBoard scoreBoard) {}

    /**
     * Send the updated chat
     * @param chat the chat
     */
    public abstract void updateChat(GameChat chat);

    /**
     * Send a message to the client
     * @param msg the message
     */
    public void sendMessage(Serializable msg) {}

    /**
     * Disconnects the connection handler
     */
    public void disconnect() {}

    /**
     * Equality based on nickname
     * @param o the connectionHanlder to check equality with
     * @return true if the nickname is the same in both the connectionHandlers
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (this.getClass() != o.getClass()) return false;
        return getClientNickname().equals(((ConnectionHandler) o).getClientNickname());
    }

}
