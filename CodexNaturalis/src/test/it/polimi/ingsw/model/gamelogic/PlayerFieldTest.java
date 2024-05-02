package it.polimi.ingsw.model.gamelogic;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.card.*;
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
 */
//TODO find edge cases, and gold cards, refactor
public class PlayerFieldTest {

    PlayerField playerField;
    StartingCard startingCard;
    Deck resDeck, goldDeck;

    /**
     * setup method for creating and parsing starting card
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
     * Given an GameCard returns the first index that contains BLANK or COVERED
     * @param card GameCard
     * @return first index that contains blank, if not found -1
     */
    private int getFirstHiddenIndex(GameCard card)
    {
        for(int i=0; i<4;  i++)
        {
            if(card.getCorner(i)==Resource.HIDDEN && card.getCorner(i)!=Resource.COVERED )
                return i;
        }
        return -1;
    }
    /**
     * Given an GameCard returns the first index that is not BLANK or COVERED
     * @param card GameCard
     * @return first index that is not blank, if not found -1
     */
    private int getFirstNotHiddenIndex(GameCard card)
    {
        for(int i=0; i<4;  i++)
        {
            if(card.getCorner(i)!=Resource.HIDDEN && card.getCorner(i)!=Resource.COVERED)
                return i;
        }
        return -1;
    }

    /**
     * testing that Playability is correct for any card
     */
    @Test
    public void IsPlayableTest() {
        //cases: already occupied coordinate,

        playerField.draw(goldDeck, 0); //get a card in the hand
        GoldCard card = (GoldCard) playerField.getHand().getFirst();

        boolean playableIndexes[] = new boolean[4];


        Coordinates coordinates;
int n=0;
        //4 cases
        while(true) {
       //     System.out.println(card.getCorner(0)+" "+card.getCorner(1)+" "+card.getCorner(2)+" "+card.getCorner(3));
            coordinates = new Coordinates(1, -1); //i=0 j=3
            if (startingCard.getCorner(0)==Resource.HIDDEN ) {
                assertFalse(playerField.IsPlayable(coordinates, (ResourceCard) card));
                System.out.println("Not Playable!");
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates, (ResourceCard) card));
                System.out.println("Playable!");
            }
            coordinates = new Coordinates(1, 1);// i=1 j=2
            if (startingCard.getCorner(1)==Resource.HIDDEN ) {
                assertFalse(playerField.IsPlayable(coordinates, (ResourceCard) card));
                System.out.println("Not Playable!");
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates, (ResourceCard) card));
                System.out.println("Playable!");
            }
            coordinates = new Coordinates(-1, -1);// i=2 j=1
            if (startingCard.getCorner(2)==Resource.HIDDEN) {

                assertFalse(playerField.IsPlayable(coordinates, (ResourceCard) card));
                System.out.println("Not Playable!");
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates, (ResourceCard) card));
                System.out.println("Playable!");
            }
            coordinates = new Coordinates(-1, 1);// i=3 j=0
            if (startingCard.getCorner(3)==Resource.HIDDEN) {
                assertFalse(playerField.IsPlayable(coordinates, (ResourceCard) card));
                System.out.println("Not Playable!");
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates, (ResourceCard) card));
                System.out.println("Playable!");
            }
            playerField.draw(goldDeck,1);
            n++;
            card=(GoldCard) playerField.getHand().get(n);
            if(card== null)
                break;
        }




    }

    /**
     * Casually trying to place all cards in the deck, asserting that checks are correct and congruent with Play output.
     */
    @Test
    public void Playtest()
    {


      //  System.out.println(playerField.getHand().getFirst().getPoints()+" "+playerField.getHand().getFirst().getCorner(3));




    //indexing ->   0: y=y+1 x=x-1    --     1:y=y+1 x=x+1   --    2:y=y-1 x=x-1  --    3:y=y-1 x=x+1  -1: can't place anything on that card
        Coordinates coordinates=new Coordinates(0,0);

        GameCard card=startingCard;

        boolean lock=false;
        while(true) {
            boolean found=false;

            for (Coordinates coordinate : playerField.getGameZone().keySet()) {

                card=playerField.getGameZone().get(coordinate);

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

                if( (playerField.getGameZone().containsKey(coordinates))) {
                    System.err.println("coordinate already occupied");
                  continue;
                }
                found=true;
                break;
            }
            if(!found)
            {
                System.out.println("Couldn't find suitable card where to place");
                return;
            }
            try {
                if(!lock)
                    playerField.draw(resDeck, 0);
            }
            catch (NullPointerException e)
            {
                System.out.println("Finished Cards");
                return;
            }

            System.out.println(card.getCorner(0)+" "+card.getCorner(1)+" "+card.getCorner(2)+" "+card.getCorner(3)+" "+Util.getKeyByValue(playerField.getGameZone(),card).getX()+" "+Util.getKeyByValue(playerField.getGameZone(),card).getY());

            card=playerField.getHand().getFirst();

            System.out.println(card.getCorner(0)+" "+card.getCorner(1)+" "+card.getCorner(2)+" "+card.getCorner(3)+" "+ coordinates.getX()+" "+coordinates.getY());
            if (playerField.IsPlayable(coordinates, (ResourceCard) card)) {
                assertEquals( ((ResourceCard) card).getPoints(), playerField.Play(coordinates, (ResourceCard)card));
                System.out.println("placed!");
                lock=false;

            } else {
                assertEquals(-1, playerField.Play(coordinates, (ResourceCard)card));
                System.out.println("Can't be placed!");
                lock=true;

            }

        }
    }

}