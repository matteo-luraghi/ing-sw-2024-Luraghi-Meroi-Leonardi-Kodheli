package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.Util;

import java.util.Scanner;

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
        int num;
        if(firstPlayer){
            firstPlayer = false;
            //TODO: view the message "You are the first player"
            num = controller.scanner.nextInt();
            do{
                //TODO: view the message "Choose the number of players for this game"
                num = controller.scanner.nextInt();
                if(num < 2 || num > 4){
                    //TODO: view the message "Invalid number of players, choose again"
                }
            }while(num < 2 || num > 4);
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
        String nick;
        String colorString;
        boolean correct;
        Color color = null; //Will always get initialized in the second do-while loop

        //Ask the player for his username
        do {
            //TODO: view the message "Select an username"
            nick = controller.scanner.nextLine();
            correct = checkUniqueNickname(nick);
            if(!correct){
                //TODO: view the message "The nickname is already present, select another one"
            }
        }while (!correct);

        //Ask the player for his chosen color
        do {
            //TODO: view the message "Select a color" + Available colors
            colorString = controller.scanner.nextLine();
            try{
                color = Util.stringToColor(colorString);
                correct = checkUniqueColor(color);
            } catch (NullPointerException e){
                //TODO: view the message "Invalid color, select a valid color"
                correct = false;
            }
        }while (!correct);

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
     * Method that checks if a player chosen color is already present in the game
     * @param color the color that needs checking
     * @return true if it's NOT already present, false if it is
     */
    private boolean checkUniqueColor(Color color){
        for(Player p : controller.getGame().getPlayers()){
            if(p.getColor() == color) return false;
        }
        return true;
    }
    /**
     * Implementation of abstract method HandleState
     */
    @Override
    public void HandleState() {
        //TODO: Needs to be reimplemented with threads once server gets going
        controller.setNumOfPlayers(giveNumberPlayers());
        for(int i = 0; i < controller.getNumOfPlayers(); i++){
            setPlayer();
        }
        transition(new GameActions());
    }
}
