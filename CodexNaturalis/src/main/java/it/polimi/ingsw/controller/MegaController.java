package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.gamelogic.GameState;

import java.util.Scanner;

/**
 * MegaController class, manages player input and sends messages to the model
 * @author Gabriel Leonardi
 */
public class MegaController {
    private GameState game;
    private ControllerState controllerState;
    protected Scanner scanner;
    private int numOfPlayers;
    /**
     * MegaController constructor
     * @param game Game that the megaController manages
     */
    public MegaController(GameState game){
        this.game = game;
        this.controllerState = new LoginPlayer();
        this.scanner = new Scanner(System.in);
        numOfPlayers = -1;
    }

    /**
     * ControllerState setter, making it possible to transition between states
     * @param newState the controllerState i want to switch to
     */
    public void setState(ControllerState newState){ this.controllerState = newState; }

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
}
