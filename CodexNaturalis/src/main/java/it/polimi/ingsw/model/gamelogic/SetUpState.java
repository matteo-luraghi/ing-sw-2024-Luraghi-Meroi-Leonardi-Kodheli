package it.polimi.ingsw.model.gamelogic;

import com.google.gson.Gson;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.StartingCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

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
        Gson gson = new Gson();
        //temporarily I assume there's going to be 4 players
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Lorenzo", Color.RED));
        players.add(new Player("Gabriel", Color.BLUE));
        players.add(new Player("Matteo", Color.GREEN));
        players.add(new Player("Francesk", Color.YELLOW));
        //end of Players initialization

        ScoreBoard scoreBoard = new ScoreBoard(players);
        Deck goldDeck = new Deck(true);
        Deck resurceDeck = new Deck(false);

        Map<Player, PlayerField> playerZones = new HashMap<Player, PlayerField>();

        // generate 4 different starting cards
        int[] startingCards = new Random().ints(1,6).distinct().limit(4).toArray();
        for (int i=0; i<players.size(); i++) {

            int startingCardNum = startingCards[i];

            // parse random starting card
            String cardPath = "../resources/CardsJSON/startingCards/startingCard" + startingCardNum + ".json"; //TODO: fix root path
            try(Reader reader = new FileReader(cardPath)) {
                StartingCard card = gson.fromJson(reader, StartingCard.class);
                PlayerField playerField = new PlayerField(card);

                // add player and playerfield to map
                playerZones.put(players.get(i), playerField);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //@TODO parse goal cards, first give one to each playerfield through the PlayerField.setPrivateGoal method; then choose two of them as the common goals
        int[] goalCards = new Random().ints(1, 16).distinct().limit(10).toArray();
        int index = 0;

        for (Player player: players) {
            /*
            GoalCard[] privateGoals = new GoalCard[2];
            String cardPath = "../resources/CardsJSON/goalCards/goalCard" + goalCards[index] + ".json"; //TODO: fix root path
            try(Reader reader = new FileReader(cardPath)) {
                GoalCard card = gson.fromJson(reader, GoalCard.class);
                privateGoals[0] = card;
            } catch (IOException e) {
                e.printStackTrace();
            }
            index++;
            cardPath = "../resources/CardsJSON/startingCards/goalCards/goalCard" + goalCards[index] + ".json"; //TODO: fix root path
            try(Reader reader = new FileReader(cardPath)) {
                GoalCard card = gson.fromJson(reader, GoalCard.class);
                privateGoals[1] = card;
            } catch (IOException e) {
                e.printStackTrace();
            }
            index++;
             */
        }

        // put them into an array for the GameTable constructor
        GameTable gameTable = new GameTable(resurceDeck, goldDeck, playerZones);
        this.game = new GameState(players, players.get(0), gameTable);
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
