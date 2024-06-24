package it.polimi.ingsw.model.gamelogic;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.view.cli.ViewPlayerFieldCLIFactory;
import org.junit.Before;
import com.google.gson.Gson;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * PlayerFieldTest
 * @author Françesko
 */

public class PlayerFieldTest {

    PlayerField playerField;
    StartingCard startingCard;
    Deck resDeck, goldDeck;

    /**
     * setup method for creating and parsing starting card
     *
     */
    @Before
    public void setup()
    {
        resDeck=new Deck(false);
        goldDeck=new Deck(true);
        Random random = new Random();
        int index=random.nextInt(6)+1;
        JsonParser parser = new JsonParser();
        String cardPath = "./src/main/resources/CardsJSON/startingCards/startingCard" + index + ".json";

        // initialize the json file reader and save the card
        try(Reader reader = new FileReader(cardPath)) {
            JsonObject parsedStartingCard = parser.parse(reader).getAsJsonObject();
            startingCard=Util.fromJSONtoStartingCard(parsedStartingCard);
            if(!startingCard.getIsFront()) {
                startingCard.flip();

            }
            playerField=new PlayerField(startingCard);
            System.out.println(startingCard.getCorner(0)+" "+startingCard.getCorner(1)+" "+startingCard.getCorner(2)+" "+startingCard.getCorner(3));


        } catch (IOException e) {
            e.printStackTrace();
        }


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
     * testing that Playability is correct for any card
     * @result if a base corner is hidden the card cannot be played
     */
    @Test
    public void IsPlayableTest() {
        //cases: already occupied coordinate,

        playerField.draw(resDeck, 0); //get a card in the hand
        ResourceCard card = playerField.getHand().getFirst();



        Coordinates coordinates;
        int n=0;
        //4 cases
        while(true) {
            coordinates = new Coordinates(1, -1); //i=0 j=3
            if (startingCard.getCorner(3)==Resource.HIDDEN ) {
                assertFalse(playerField.IsPlayable(coordinates, card));
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates, card));
            }
            coordinates = new Coordinates(1, 1);// i=1 j=2
            if (startingCard.getCorner(1)==Resource.HIDDEN ) {
                assertFalse(playerField.IsPlayable(coordinates,  card));
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates, card));
            }
            coordinates = new Coordinates(-1, -1);// i=2 j=1
            if (startingCard.getCorner(2)==Resource.HIDDEN) {

                assertFalse(playerField.IsPlayable(coordinates,card));
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates, card));
            }
            coordinates = new Coordinates(-1, 1);// i=3 j=0
            if (startingCard.getCorner(0)==Resource.HIDDEN) {
                assertFalse(playerField.IsPlayable(coordinates, card));
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates,card));
            }
            playerField.draw(resDeck,1);
            n++;
            card= playerField.getHand().get(n);
            if(card== null)
                break;
        }




    }

    /**
     * Casually trying to place all cards in the deck, asserting that checks are correct and congruent with Play output.
     * @result If playability condition are met cards are played otherwise not
     * note: Better implementation of this method in the GameTableTest class with the simulate Play
     */
    @Test
    public void playTest()
    {




        Coordinates coordinates;

        GameCard card;

        while(true) {
            /**
             * try to draw from the resDeck, further testing with also the gold test is made in the GameTable test
             */
            boolean found=false;
            try {

                    playerField.draw(resDeck, 0);
            }
            catch (NullPointerException e)
            {
                ViewPlayerFieldCLIFactory VIEW= new ViewPlayerFieldCLIFactory();
                VIEW.setPlayerField(playerField, new Player("Mario", Color.RED));
                VIEW.ShowGameZone();
                return;
            }
            for (Coordinates coordinate : playerField.getGameZone().keySet()) {

                card=playerField.getGameZone().get(coordinate);
//indexing ->   0: y=y+1 x=x-1    --     1:y=y+1 x=x+1   --    2:y=y-1 x=x-1  --    3:y=y-1 x=x+1  -1: can't place anything on that card
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
                ResourceCard playCard=playerField.getHand().getFirst();
                if (playerField.IsPlayable(coordinates, (ResourceCard) playCard)) {

                    assertEquals( ((ResourceCard) playCard).getPoints(), playerField.Play(coordinates, (ResourceCard)playCard));

                    found=true;
                    break;

                } else {

                    assertEquals(-1, playerField.Play(coordinates, (ResourceCard)playCard));


                }



            }
            if(!found)
            {
                ViewPlayerFieldCLIFactory VIEW= new ViewPlayerFieldCLIFactory();
                VIEW.setPlayerField(playerField, new Player("Mario", Color.RED));
                VIEW.ShowGameZone();

                return;
            }







        }
    }

}