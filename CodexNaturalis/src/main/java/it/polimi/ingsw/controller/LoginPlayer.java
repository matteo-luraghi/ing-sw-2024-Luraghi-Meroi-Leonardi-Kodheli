package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.Player;

/**
 * LoginPlayer state, allows players to login
 * @author Gabriel Leonardi
 */
public class LoginPlayer extends ControllerState{

    private boolean firstPlayer;

    /**
     * LoginPlayer state constructor;
     */
    public LoginPlayer(){
        this.firstPlayer = true;
    }

    /**
     * Function to make the player set how many players will play this game
     * @return the number of players chosen by the firstPlayer, returning the default value of 2 if the number chosen isn't 2, 3 or 4, and -1 if it's already set
     */
    private int giveNumberPlayers(){
        int num = 0;
        if(firstPlayer){
            firstPlayer = false;
            //TODO: make controller ask to the player for the numbers of players to the game
            if(num < 2 || num > 4){
                System.err.println("InvalidNumberOfPlayers, using default value of 2");
                num = 2;
            }
            return num;
        } else {
            System.err.println("You aren't the first player, you cannot set how many players there are, returning -1");
            return -1;
        }
    }

    /**
     * Method to let a user register himself as a player
     * @return the player object constructed by the user
     */
    private Player setPlayer(){
        String nick = "";
        Color color = Color.YELLOW;
        do {
            //TODO: ask the player for nickname and color
        }while (!checkUniqueNickname(nick));
        return new Player(nick, color);
    }

    /**
     * Method that checks if a player nickname is already present in the game
     * @param nick the nickname that needs checking
     * @return true if it's NOT already present, false if it is
     */
    private boolean checkUniqueNickname(String nick){
        return !this.controller.getGame().getPlayers().contains(nick);
    }
    /**
     * Implementation of abstract method HandleState
     */
    @Override
    public void HandleState() {

    }
}
