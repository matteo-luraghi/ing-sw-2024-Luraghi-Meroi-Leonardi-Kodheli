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
        //temporarily I assume there's going to be 4 players
        ArrayList<Player> Players = new ArrayList<Player>();
        Players.add(new Player("Lorenzo", Color.RED));
        Players.add(new Player("Gabriel", Color.BLUE));
        Players.add(new Player("Matteo", Color.GREEN));
        Players.add(new Player("Francesk", Color.YELLOW));
        //end of Players initialization

        ScoreBoard scoreBoard = new ScoreBoard(Players);
        Deck goldDeck = new Deck(true);
        Deck resurceDeck = new Deck(false);

        ArrayList<PlayerField> playerFields = new ArrayList<>();
        //@TODO parse starting cards and give one to a random playerfield

        //@TODO parse goal cards, first give one to each playerfield through the PlayerField.setPrivateGoal method; then choose two of them as the common goals
        // put them into an array for the GameTable constructor
        //GameTable gameTable = new GameTable();
        GameState gameState = new GameState(Players, Players.get(0), gameTable);
        this.game = gameState;
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
