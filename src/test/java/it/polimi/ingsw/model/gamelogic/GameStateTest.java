package it.polimi.ingsw.model.gamelogic;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.StartingCard;
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * GameState Test
 * @author Françesko
 */
public class GameStateTest {
    GameState gameState;
    Deck resDeck;
    Deck goldDeck;
    GameTable gameTable;
    ArrayList<PlayerField> playerFields;
    ArrayList<Player> players;

    Map<Player, PlayerField> playerZones;
    ArrayList<GoalCard> commonGoals;

    ScoreBoard scoreBoard;

    /**
     * Setup: before tests it generates a random number of players from 2 to 4 and assigns all the values
     */
    @Before
    public void setup() {
        Random random = new Random();
        int numberOfPlayers = random.nextInt(3) + 2;
        players=new ArrayList<>();
        playerFields=new ArrayList<>();
        playerZones=new HashMap<>();
        commonGoals=new ArrayList<>();
        ArrayList<Integer> usedIndexes = new ArrayList<Integer>();
        ArrayList<Integer> usedIndexesGoal = new ArrayList<Integer>();
        int index, index2;
        JsonParser parser = new JsonParser();
        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player("Player" + i, Color.values()[i-1]));
            boolean repeat;
            do {
                index = random.nextInt(6) + 1;


                repeat = false;

                for (Integer index1 : usedIndexes) {
                    if (index1.intValue() == index)
                        repeat = true;
                }


            } while (repeat);
            do {
                index2 = random.nextInt(16) + 1;


                repeat = false;

                for (Integer index1 : usedIndexesGoal) {
                    if (index1.intValue() == index2)
                        repeat = true;
                }


            } while (repeat);
            usedIndexes.add(index);
            usedIndexesGoal.add(index2);
            String cardPathStarting = "./src/main/resources/CardsJSON/startingCards/startingCard" + index + ".json";
            String cardPathGoal = "./src/main/resources/CardsJSON/goalCards/goalCard" + index2 + ".json";
            // initialize the json file reader and save the card
            try (Reader reader = new FileReader(cardPathStarting)) {
                JsonObject parsedStartingCard = parser.parse(reader).getAsJsonObject();
                StartingCard startingCard = Util.fromJSONtoStartingCard(parsedStartingCard);
                if (random.nextBoolean()) {
                    startingCard.flip();

                }

                playerFields.add(new PlayerField(startingCard));
                playerZones.put(players.get(i - 1), playerFields.get(i - 1));
            } catch (IOException e) {
                e.printStackTrace();
            }
            try (Reader reader = new FileReader(cardPathGoal)) {


                JsonObject parsedGoalCard = parser.parse(reader).getAsJsonObject();
                GoalCard goalCard;
                if (parsedGoalCard.get("isResourceGoal").getAsBoolean()) {
                    goalCard = Util.fromJSONtoResourceGoalCard(parsedGoalCard);
                } else {
                    goalCard = Util.fromJSONtoPositionGoalCard(parsedGoalCard);
                }


                playerFields.get(i - 1).setPrivateGoal(goalCard);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<2;i++) {
            boolean repeat;
            do {
                index2 = random.nextInt(16) + 1;


                repeat = false;

                for (Integer index1 : usedIndexesGoal) {
                    if (index1.intValue() == index2)
                        repeat = true;
                }


            } while (repeat);

            String cardPathGoal = "./src/main/resources/CardsJSON/goalCards/goalCard" + index2 + ".json";
            try (Reader reader = new FileReader(cardPathGoal)) {


                JsonObject parsedGoalCard = parser.parse(reader).getAsJsonObject();
                GoalCard goalCard;
                if (parsedGoalCard.get("isResourceGoal").getAsBoolean()) {
                    goalCard = Util.fromJSONtoResourceGoalCard(parsedGoalCard);
                } else {
                    goalCard = Util.fromJSONtoPositionGoalCard(parsedGoalCard);
                }


                commonGoals.add(goalCard);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        scoreBoard=new ScoreBoard(players);
        resDeck=new Deck(false, true);
        goldDeck=new Deck(true, true);
        gameTable=new GameTable(resDeck,goldDeck,playerZones, commonGoals.toArray(new GoalCard[2]),scoreBoard );
        gameState=new GameState(players,players.get(0),gameTable);
    }

    /**
     * Tests if gameState has been initialized correctly
     * @result gameState getters return the initial correct values
     */
    @Test
    public void testInitiazation()
    {
        assertEquals(State.SETUP,gameState.getState());
        assertEquals(null,gameState.getTurnState());
        assertEquals(gameTable,gameState.getGameTable());
        assertTrue(players.containsAll(gameState.getPlayers()));
        assertEquals(players.getFirst(),gameState.getTurn());

    }

    /**
     * test of setState and setTurnState
     * @result= getters returns the setted value
     */
    @Test
    public void testSetters()
    {
        for(int i=0;i<State.values().length;i++)
        {
            State newState=State.values()[i];
            gameState.setState(newState);
            assertEquals(newState,gameState.getState());
        }
        for(int i=0;i<TurnState.values().length;i++)
        {
            TurnState newState=TurnState.values()[i];
            gameState.setTurnState(newState);
            assertEquals(newState,gameState.getTurnState());
        }



    }

    /**
     * Test to add players up to 4
     * @result= fails when exception is thrown when players are less than 4 or if exception is not thrown when players are 4
     */
    @Test
    public void testAddPlayer()
    {
        while(players.size()<4)
        {
            players.add(new Player("Player"+players.size(),Color.values()[players.size()-1]));
            try {
                gameState.addPlayer(players.getLast());
                assertEquals(players.getLast(),gameState.getPlayers().getLast());
            }
            catch (IndexOutOfBoundsException e)
            {
                fail();
            }
        }
        try {
            gameState.addPlayer(new Player("Player5", Color.RED));
            fail();
        }
        catch (IndexOutOfBoundsException e)
        {
            assertEquals(players.getLast(),gameState.getPlayers().getLast());
        }
    }

    /**
     * tests Next turn
     * @result= the next turn is the next player in the arraylist, if the last the next is the first.
     */
    @Test
    public void testNextTurn()
    {
        for(Player player: gameState.getPlayers())
        {
            assertEquals(gameState.getTurn(),player);
            gameState.nextTurn();
            if(player==gameState.getPlayers().getLast())
                assertEquals(gameState.getTurn(),gameState.getPlayers().getFirst());
            else
                assertEquals(gameState.getTurn(),gameState.getPlayers().get(gameState.getPlayers().indexOf(player)+1));
        }
    }

    /**
     * test that generates random points for each player and saves the correct winner
     * @result= the returned value from getWinner is the same as the correct winner
     */
    @Test
    public void testComputeWinner()
    {
        gameState.setState(State.FINAL);
        Random random=new Random();
        int maxPoints=0;
        Player winnerPlayer=null;
        for(Player player: gameState.getPlayers())
        {

            int points;
            do {
                points=random.nextInt(0,30);
            }while(points==maxPoints);

            gameState.getGameTable().getScoreBoard().addPoints(player,points);
            if(points>maxPoints) {
                winnerPlayer = player;
                maxPoints=points;
            }
        }
        assertEquals(winnerPlayer,gameState.getWinner());


    }

}