package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.model.gamelogic.GameState;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.Util;
import it.polimi.ingsw.model.states.SetUpState;

import java.util.Scanner;

/**
 * MegaController class, manages player input and sends messages to the model
 * @author Gabriel Leonardi
 */
public class MegaController {
    private GameState game;
    protected Scanner scanner;
    private int numOfPlayers;
    /**
     * MegaController constructor
     * @param game Game that the megaController manages
     */
    public MegaController(GameState game){
        this.game = game;
        this.scanner = new Scanner(System.in);
        numOfPlayers = -1;
    }

    /**
     * gameState getter
     * @return the current gameState;
     */
    public GameState getGame() {
        return game;
    }

    /**
     * Number of players getter
     * @return the numbers of players that this match has
     */
    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    /**
     * Number of players setter
     * @param numOfPlayers the numbers of players for this game decided by the first user
     */
    public void setNumOfPlayers(int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    /**
     * Asks the first player for the number of players that will be playing this game
     */
    protected void giveNumberPlayers(){
        int num;
        if(numOfPlayers == -1){
            //TODO: view the message "You are the first player"
            do{
                //TODO: view the message "Choose the number of players for this game"
                num = scanner.nextInt();
                if(num < 2 || num > 4){
                    //TODO: view the message "Invalid number of players, choose again"
                }
            }while(num < 2 || num > 4);

            //TODO: Set numberOfPlayers somewhere to the number chosen by the player
        } else {
            System.err.println("You aren't the first player, you cannot set how many players there are");
        }
    }

    /**
     * Method to let a user register himself as a player, passing the created player to the model
     */
    protected void setPlayer(){
        String nick;
        String colorString;
        boolean correct;
        Color color = null; //Will always get initialized in the second do-while loop

        //Ask the player for his username
        do {
            nick = scanner.nextLine();
            correct = checkUniqueNickname(nick);
            if(!correct){
                controller.view.showMessage("Username already exists, please choose another username");
            }
        }while (!correct);

        //Ask the player for his chosen color
        do {
            colorString = scanner.nextLine();
            try{
                color = Util.stringToColor(colorString);
                correct = checkUniqueColor(color);
                if(!correct) controller.view.showMessage("Color is already taken, please choose a free color");
            } catch (NullPointerException e){
                controller.view.showMessage("Color doesn't exist, please choose a valid color");
                correct = false;
            }
        }while (!correct);

        Player player = new Player(nick, color);
        game.addPlayer(player);
    }

    /**
     * Method that checks if a player nickname is already present in the game
     * @param nick the nickname that needs checking
     * @return true if it's NOT already present, false if it is
     */
    private boolean checkUniqueNickname(String nick){
        for(Player p : getGame().getPlayers()){
            if(p.getNickname().equals(nick)) return false;
        }
        return true;
    }

    /**
     * Method that checks if a player chosen color is already present in the game
     * @param color the color that needs checking
     * @return true if it's NOT already present, false if it is
     */
    private boolean checkUniqueColor(Color color){
        for(Player p : getGame().getPlayers()){
            if(p.getColor() == color) return false;
        }
        return true;
    }
}
