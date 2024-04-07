package it.polimi.ingsw.model.gamelogic;

import java.util.ArrayList;

/**
 * SetUpState class
 * it manages the creation of the game
 * @author Lorenzo Meroi
 */
public class SetUpState extends State{

    /**
     * constructor of the SetUpState class
     * @param game is the gamestate to which the state refers
     */
    public SetUpState(GameState game) {
        super(game);
    }

    /**
     * method to handle the setupping state of the game
     */
    @Override
    public void HandleState() {

    }

    /**
     * method to create the PlayerFields for each player
     */
    private void CreatePlayerFields() {}

    /**
     * method to create the game's table
     */
    private void CreateGametable() {}

    /**
     * method to create the game's two decks
     */
    private void CreateDecks() {}
}
