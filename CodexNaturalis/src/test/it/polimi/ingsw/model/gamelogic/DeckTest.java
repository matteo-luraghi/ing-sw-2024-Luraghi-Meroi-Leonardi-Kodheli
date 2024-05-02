package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.ResourceCard;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
   Deck deck, goldDeck;


    /**
     * setup a normal deck and a gold deck for each test
     */
    @Before
    public void setUp(){
        deck=new Deck(false);
        goldDeck= new Deck(true);
    }


    /**
     * Type test for checking correct initialization
     */
    @Test
    public void TypeTest(){

        Assertions.assertEquals(false, deck.isGold());
        Assertions.assertEquals(true, goldDeck.isGold());
        Assertions.assertEquals(true, goldDeck.DrawFromDeck().getIsGold());
        Assertions.assertEquals(false, deck.DrawFromDeck().getIsGold());
    }


    /**
     * Drawtest: Draws first two cards from deck and goldDeck, checks if they are not null and not present
     * in the remaining cards. Draws all the remaining cards from the uncovered, asserting that they
     * are removed from the remaining cards. Asserts that the drawed cards are 40 for normal and 40 for
     * gold deck, assert that the decks are empty and the exception is thrown.
     *
     */
    @Test
    public void DrawTest()
    {
        ResourceCard drawed=deck.DrawFromDeck();
        GoldCard drawed1=(GoldCard) goldDeck.DrawFromDeck();
        assertNotEquals(null,drawed);
        assertNotEquals(null,drawed1);
        assertFalse(deck.getCards().contains(drawed));
        assertFalse(goldDeck.getCards().contains(drawed));
        int i=1,n=1;
        int j=0;
        while(true)
        {
            drawed=deck.DrawFromUncovered(j);

            if(drawed==null)
                break;
            assertFalse(deck.getCards().contains(drawed));
        i++;


        }



        while(true)
        {
            drawed1=(GoldCard) goldDeck.DrawFromUncovered(j);

            if(drawed1==null)
                break;
            assertFalse(goldDeck.getCards().contains(drawed));
        n++;


        }

        assertNotEquals(null,goldDeck.DrawFromUncovered(1));
        assertNotEquals(null,deck.DrawFromUncovered(1));
        n++;
        i++;
        try
        {
            deck.DrawFromDeck();
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e)
        {

        }
        try
        {
            goldDeck.DrawFromDeck();
            fail("Expected NullPointerException");
        }
        catch(NullPointerException e)
        {

        }


        Assertions.assertEquals(40,i);
        Assertions.assertEquals(40, n);
        assertEquals(true, deck.isDeckEmpty());
        assertEquals(true, goldDeck.isDeckEmpty());
    }




}