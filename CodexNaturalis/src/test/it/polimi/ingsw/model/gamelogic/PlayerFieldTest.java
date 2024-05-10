package it.polimi.ingsw.model.gamelogic;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.view.cli.ViewPlayerFieldCLIFactory;
import it.polimi.ingsw.view.mainview.ViewPlayerFieldFactory;
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
            System.out.println(card.getCorner(0)+" "+card.getCorner(1)+" "+card.getCorner(2)+" "+card.getCorner(3));
            coordinates = new Coordinates(1, -1); //i=0 j=3
            if (startingCard.getCorner(3)==Resource.HIDDEN ) {
                assertFalse(playerField.IsPlayable(coordinates, card));
                System.out.println("Not Playable!");
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates, card));
                System.out.println("Playable!");
            }
            coordinates = new Coordinates(1, 1);// i=1 j=2
            if (startingCard.getCorner(1)==Resource.HIDDEN ) {
                assertFalse(playerField.IsPlayable(coordinates,  card));
                System.out.println("Not Playable!");
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates, card));
                System.out.println("Playable!");
            }
            coordinates = new Coordinates(-1, -1);// i=2 j=1
            if (startingCard.getCorner(2)==Resource.HIDDEN) {

                assertFalse(playerField.IsPlayable(coordinates,card));
                System.out.println("Not Playable!");
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates, card));
                System.out.println("Playable!");
            }
            coordinates = new Coordinates(-1, 1);// i=3 j=0
            if (startingCard.getCorner(0)==Resource.HIDDEN) {
                assertFalse(playerField.IsPlayable(coordinates, card));
                System.out.println("Not Playable!");
            }
            else {
                assertTrue(playerField.IsPlayable(coordinates,card));
                System.out.println("Playable!");
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
     */
    @Test
    public void Playtest()
    {



    //indexing ->   0: y=y+1 x=x-1    --     1:y=y+1 x=x+1   --    2:y=y-1 x=x-1  --    3:y=y-1 x=x+1  -1: can't place anything on that card
        Coordinates coordinates=new Coordinates(0,0);

        GameCard card=startingCard;
int tries=0;

        while(true) {
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
                ResourceCard playcard=playerField.getHand().getFirst();
                if (playerField.IsPlayable(coordinates, (ResourceCard) playcard)) {

                    assertEquals( ((ResourceCard) playcard).getPoints(), playerField.Play(coordinates, (ResourceCard)playcard));
                   // System.out.println("placed!");

                    found=true;
                    break;

                } else {

                    assertEquals(-1, playerField.Play(coordinates, (ResourceCard)playcard));
                   // System.out.println("Can't be placed!"); //this should not be executed


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