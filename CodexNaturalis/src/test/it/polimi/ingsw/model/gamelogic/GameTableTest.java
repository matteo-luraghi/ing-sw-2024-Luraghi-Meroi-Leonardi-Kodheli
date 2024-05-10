package it.polimi.ingsw.model.gamelogic;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.view.cli.ViewGoalCardCLIFactory;
import it.polimi.ingsw.view.cli.ViewPlayerFieldCLIFactory;
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
 * class GameTableTest
 * @author Francesk Kodheli
 */
public class GameTableTest {
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
        resDeck=new Deck(false);
        goldDeck=new Deck(true);
        gameTable=new GameTable(resDeck,goldDeck,playerZones, commonGoals.toArray(new GoalCard[2]),scoreBoard );
    }


    /**
     * Simulates a Match by casually placing the cards (note that first a player plays all the cards until condition met for exit, then the others)
     * @param playerField chosen playerfield
     * @param howManyCards How many cards do you want to play for a given playerField
     * @param printPlayerField boolean value: if true prints at the end the playerField
     */
    public void simulatePlay(PlayerField playerField, int howManyCards, boolean printPlayerField)
    {

        Coordinates coordinates=new Coordinates(0,0);

        GameCard card;
        boolean gold;
        int playedCards=0;
        Random random = new Random();
    int i=0;
        while(true) {
            boolean found = false;
            if (random.nextBoolean()) {
                try {

                    playerField.draw(resDeck, 0);
                    gold=false;
                } catch (NullPointerException e) {
                    try {
                        playerField.draw(goldDeck, 0);
                        gold=true;
                    } catch (NullPointerException e1) {
                        if (printPlayerField) {
                            ViewGoalCardCLIFactory viewGoalCard=new ViewGoalCardCLIFactory();
                            viewGoalCard.printTwoCards(commonGoals.get(0),commonGoals.get(1) );
                            viewGoalCard.printGoalCard(playerField.getPrivateGoal());
                            ViewPlayerFieldCLIFactory VIEW = new ViewPlayerFieldCLIFactory();
                            VIEW.setPlayerField(playerField, Util.getKeyByValue(playerZones, playerField));
                            VIEW.ShowGameZone();
                        }
                        return;
                    }
                }
            } else {
                try {

                    playerField.draw(goldDeck, 0);
                    gold=true;
                } catch (NullPointerException e) {
                    try {
                        playerField.draw(resDeck, 0);
                        gold=false;
                    } catch (NullPointerException e1) {

                        if (printPlayerField) {
                            ViewGoalCardCLIFactory viewGoalCard=new ViewGoalCardCLIFactory();
                            viewGoalCard.printTwoCards(commonGoals.get(0),commonGoals.get(1) );
                            viewGoalCard.printGoalCard(playerField.getPrivateGoal());
                            ViewPlayerFieldCLIFactory VIEW = new ViewPlayerFieldCLIFactory();
                            VIEW.setPlayerField(playerField, Util.getKeyByValue(playerZones, playerField));
                            VIEW.ShowGameZone();
                        }
                        return;
                    }
                }
            }
            int prevPoints=gameTable.getScoreBoard().getPoints(Util.getKeyByValue(gameTable.getPlayerZones(),playerField));
            int nextPoints=gameTable.getScoreBoard().getPoints(Util.getKeyByValue(gameTable.getPlayerZones(),playerField));
            for (Coordinates coordinate : playerField.getGameZone().keySet()) {

                card = playerField.getGameZone().get(coordinate);

                switch (Util.getFirstNotHiddenIndex(card)) {
                    case 0: {

                        coordinates = new Coordinates(Util.getKeyByValue(playerField.getGameZone(), card).getX() - 1, Util.getKeyByValue(playerField.getGameZone(), card).getY() + 1);

                        break;

                    }
                    case 1: {

                        coordinates = new Coordinates(Util.getKeyByValue(playerField.getGameZone(), card).getX() + 1, Util.getKeyByValue(playerField.getGameZone(), card).getY() + 1);

                        break;
                    }
                    case 2: {

                        coordinates = new Coordinates(Util.getKeyByValue(playerField.getGameZone(), card).getX() - 1, Util.getKeyByValue(playerField.getGameZone(), card).getY() - 1);

                        break;
                    }
                    case 3: {

                        coordinates = new Coordinates(Util.getKeyByValue(playerField.getGameZone(), card).getX() + 1, Util.getKeyByValue(playerField.getGameZone(), card).getY() - 1);

                        break;
                    }
                    default: {
                        continue;

                    }
                }
                ResourceCard playcard = playerField.getHand().get(i);

                if (playerField.IsPlayable(coordinates, (ResourceCard) playcard)) {

                    if(((ResourceCard) playcard).getIsFront()) {
                        if(!playcard.getIsGold())
                        assertEquals(((ResourceCard) playcard).getPoints(), playerField.Play(coordinates, (ResourceCard) playcard));
                        else
                            assertEquals(playerField.calculateCardPoints(coordinates, (GoldCard) playcard), playerField.Play(coordinates, (ResourceCard) playcard));
                        gameTable.getScoreBoard().addPoints(Util.getKeyByValue(gameTable.getPlayerZones(),playerField),((ResourceCard) playcard).getPoints());
                        nextPoints=gameTable.getScoreBoard().getPoints(Util.getKeyByValue(gameTable.getPlayerZones(),playerField));
                        assertEquals(prevPoints+((ResourceCard) playcard).getPoints(),nextPoints);
                    }
                    else {
                        assertEquals(0, playerField.Play(coordinates, (ResourceCard) playcard));
                        gameTable.getScoreBoard().addPoints(Util.getKeyByValue(gameTable.getPlayerZones(),playerField),0);
                    }
                    playedCards++;
                    found = true;
                    break;

                } else {

                    assertEquals(-1, playerField.Play(coordinates, (ResourceCard) playcard));
                    // System.out.println("Can't be placed!"); //this should not be executed


                }


            }

            if (!found && gold) {
                i++;


            }
            else if(!found)
            {

                System.out.println("Couldn't find suitable card where to place");
                if (printPlayerField) {
                    ViewGoalCardCLIFactory viewGoalCard=new ViewGoalCardCLIFactory();
                    viewGoalCard.printTwoCards(commonGoals.get(0),commonGoals.get(1) );
                    viewGoalCard.printGoalCard(playerField.getPrivateGoal());
                    ViewPlayerFieldCLIFactory VIEW = new ViewPlayerFieldCLIFactory();
                    VIEW.setPlayerField(playerField, Util.getKeyByValue(playerZones, playerField));
                    VIEW.ShowGameZone();
                }
                return;
            }
            if (playedCards == howManyCards || nextPoints>=20) {
                if (printPlayerField) {
                    ViewGoalCardCLIFactory viewGoalCard=new ViewGoalCardCLIFactory();
                    viewGoalCard.printTwoCards(commonGoals.get(0),commonGoals.get(1) );
                    viewGoalCard.printGoalCard(playerField.getPrivateGoal());
                    ViewPlayerFieldCLIFactory VIEW = new ViewPlayerFieldCLIFactory();
                    VIEW.setPlayerField(playerField, Util.getKeyByValue(playerZones, playerField));
                    VIEW.ShowGameZone();
                }
                return;
            }


        }
    }


    /**
     * Resource Deck getter test
     * @result Asserts that the returned Deck is the same as initialized
     */
    @Test
    public void getResourceDeckTest()
    {
        assertEquals(resDeck,gameTable.getResourceDeck());
    }
    /**
     * Gold Deck getter test
     * @result Asserts that the returned Deck is the same as initialized
     */
    @Test
    public void getGoldDeckTest()
    {
        assertEquals(goldDeck,gameTable.getGoldDeck());
    }

    /**
     * PlayerZones getter test
     *@result Asserts that the returned PlayerZones is the same as initialized
     */
    @Test
    public void getPlayerZonesTest()
    {
        assertEquals(playerZones,gameTable.getPlayerZones());
    }

    /**
     * Tries 100 random values out of bound, and asserts that the returned commonGoals are the same as initialized
     * @result all the wrong indexes generates IndexOutOfBound exception, the correct one returns the initialized
     */
    @Test
    public void getCommonGoalTest()
    {
        Random random= new Random();
        for(int i=0;i<100;i++) {
            int index = random.nextBoolean() ? random.nextInt(Integer.MIN_VALUE, -1) : random.nextInt(2, Integer.MAX_VALUE);
            try {
                gameTable.getCommonGoal(index);
                fail("Expected IndexOutOfBoundExeption");
            }
            catch (Exception e)
            {

            }
        }
        assertEquals(commonGoals.get(0),gameTable.getCommonGoal(0));
        assertEquals(commonGoals.get(1),gameTable.getCommonGoal(1));
    }

    /**
     * checks if the random generated playfield generates the correct ammount of points
     * @result the points assigned to the scoreboard are the same as the calculated
     */
    @Test
    public void countPointsTest()
    {

            for (Player player : players) {
                System.out.println();
                System.out.println(player.getNickname());
                simulatePlay(gameTable.getPlayerZones().get(player), 20, true);
                int PrevPoints = gameTable.getScoreBoard().getPoints(player);
                int goalPoints = gameTable.countGoalPoints(gameTable.getPlayerZones().get(player));

                assertEquals(gameTable.getScoreBoard().getPoints(player) - PrevPoints, goalPoints);

            }


    }



}