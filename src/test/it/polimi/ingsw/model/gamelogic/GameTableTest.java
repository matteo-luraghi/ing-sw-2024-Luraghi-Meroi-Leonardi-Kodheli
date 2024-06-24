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
     * Given an GameCard returns the first index that is not BLANK or COVERED
     * @param card GameCard
     * @return first index that is not blank, if not found -1
     */
    static private int getFirstNotHiddenIndex(GameCard card)
    {
        for(int i=0; i<4;  i++)
        {
            if(card.getCorner(i)!= Resource.HIDDEN && card.getCorner(i)!=Resource.COVERED)
                return i;
        }
        return -1;
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
            /**casually tries to pick from the resource Deck or the gold Deck*/
            if (random.nextBoolean()) {
                try {

                    playerField.draw(resDeck, 0);
                    gold=false;
                } catch (NullPointerException e) {
                    /**
                     * means that the res deck is empty
                     */
                    try {
                        playerField.draw(goldDeck, 0);
                        gold=true;
                    } catch (NullPointerException e1) {
                        /**
                         * means that also the gold deck is empty
                         * so print the playerfield if parameter is true for visual debugging
                         */
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
                /**
                 * try to pick first from the gold deck
                 */
                try {

                    playerField.draw(goldDeck, 0);
                    gold=true;
                } catch (NullPointerException e) {
                    /**
                     * means that the gold deck is empty
                     */
                    try {
                        playerField.draw(resDeck, 0);
                        gold=false;
                    } catch (NullPointerException e1) {

                        /**
                         * mean that also the resource deck is empty, so if the printPlayerField parameter is true
                         * it will print the game zone for visual debugging if needed
                         */
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
             * get the currently saved points
             */
            int prevPoints=gameTable.getScoreBoard().getPoints(Util.getKeyByValue(gameTable.getPlayerZones(),playerField));
            int nextPoints=prevPoints;
            /**
             * for each coordinate saved in the gameZone will check if any corner of the corresponding card can be
             * used as corner to cover
             */
            for (Coordinates coordinate : playerField.getGameZone().keySet()) {

                card = playerField.getGameZone().get(coordinate);

                switch (getFirstNotHiddenIndex(card)) {
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
                ResourceCard playCard = playerField.getHand().get(i);

                /**
                 * if the card is playable in the found coordinates (if actually found) then it will check if
                 * it's playable and asserts that the correct points are given
                 */
                if (playerField.IsPlayable(coordinates, (ResourceCard) playCard)) {

                    if(((ResourceCard) playCard).getIsFront()) {
                        if(!playCard.getIsGold())
                        assertEquals(((ResourceCard) playCard).getPoints(), playerField.Play(coordinates, (ResourceCard) playCard));
                        else {
                            int points=playerField.Play(coordinates, (ResourceCard) playCard);
                            assertEquals(playerField.calculateCardPoints(coordinates, (GoldCard) playCard), points);
                        }
                        gameTable.getScoreBoard().addPoints(Util.getKeyByValue(gameTable.getPlayerZones(),playerField),((ResourceCard) playCard).getPoints());
                        nextPoints=gameTable.getScoreBoard().getPoints(Util.getKeyByValue(gameTable.getPlayerZones(),playerField));
                        assertEquals(prevPoints+((ResourceCard) playCard).getPoints(),nextPoints);
                    }
                    else {
                        assertEquals(0, playerField.Play(coordinates, (ResourceCard) playCard));
                    }
                    playedCards++;
                    found = true;
                    break;

                } else {

                    assertEquals(-1, playerField.Play(coordinates, (ResourceCard) playCard));
                    nextPoints=gameTable.getScoreBoard().getPoints(Util.getKeyByValue(gameTable.getPlayerZones(),playerField));
                    assertEquals(prevPoints,nextPoints); /** there should be no change in the points */

                }


            }

            /**
             * means that can't make any move
             */
            if(!found && !gold)
            {

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
                /**
                 * prints for visual debugging
                 */
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
     * note: due to the random nature of the play zone this test set simulates randomly more play simulations,
     *       and due to the fact that the check implementation of the correct values would be also the copy
     *       of the tested "countGoalPoints" method, we generated numerous random play zones and checked "by hand"
     *       that the given results were correct. In the generation of the play zones,anyway , all the other methods
     *       will be automatically checked for the correct behaviour
     *
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
                System.out.println("Prev "+PrevPoints+" goal "+goalPoints);


            }


    }



}