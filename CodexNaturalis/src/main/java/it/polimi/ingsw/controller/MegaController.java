package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.card.GoalCard;
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
    private Player player;

    /**
     * MegaController constructor
     * @param game Game that the megaController manages
     */
    public MegaController(GameState game){
        this.game = game;
        this.scanner = new Scanner(System.in);
    }

    /**
     * gameState getter
     * @return the current gameState;
     */
    public GameState getGame() {
        return game;
    }

    /**
     * Asks the first player for the number of players that will be playing this game
     */
    protected void giveNumberPlayers(){
        int numOfPlayers;
        SetUpState setUpState = (SetUpState) game.getState();
        numOfPlayers = setUpState.getNumberOfPlayers();
        if(numOfPlayers == -1){
            view.showMessage("You are the first player");
            do{
                view.showMessage("Choose the number of players for this game");
                numOfPlayers = scanner.nextInt();
                if(numOfPlayers < 2 || numOfPlayers > 4){
                    view.showMessage("Invalid number of players, choose again");
                }
            }while(numOfPlayers < 2 || numOfPlayers > 4);


            setUpState.setNumberOfPlayers(numOfPlayers);
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
                view.showMessage("Username already exists, please choose another username");
            }
        }while (!correct);

        //Ask the player for his chosen color
        do {
            colorString = scanner.nextLine();
            try{
                color = Util.stringToColor(colorString);
                correct = checkUniqueColor(color);
                if(!correct) view.showMessage("Color is already taken, please choose a free color");
            } catch (NullPointerException e){
                view.showMessage("Color doesn't exist, please choose a valid color");
                correct = false;
            }
        }while (!correct);

        view.showMessage("Waiting for players");
        Player player = new Player(nick, color);
        game.addPlayer(player);
        this.player = player;
    }

    /**
     * Method that makes the player select his private goal card
     * @param option1 The first goal card offered to the player
     * @param option2 The second goal card offered to the player
     */
    public void choosePrivateGoal(GoalCard option1, GoalCard option2){
        boolean correct = false;
        int option;
        do{
            view.showMessage("Which GoalCard will you choose? (1 or 2)");
            option = scanner.nextInt();
            if(option != 1 && option != 2){
                view.showMessage("Invalid number, choose again");
            } else {
                correct = true;
            }
        }while(!correct);

        switch(option){
            case 1:
                game.getGameTable().getPlayerZones().get(player).setPrivateGoal(option1);
                break;
            case 2:
                game.getGameTable().getPlayerZones().get(player).setPrivateGoal(option2);
                break;
        }
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
