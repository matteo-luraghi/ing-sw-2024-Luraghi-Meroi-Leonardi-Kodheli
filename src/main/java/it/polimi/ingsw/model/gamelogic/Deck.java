package it.polimi.ingsw.model.gamelogic;

import com.google.gson.*;
import it.polimi.ingsw.model.card.ResourceCard;

import java.io.*;
import java.util.*;

/**
 * Deck class
 * @author Lorenzo Meroi
 */
public class Deck implements Serializable {
    @Serial
    private static final long serialVersionUID = -4453030266153945511L;
    private Queue<ResourceCard> cards;

    private ResourceCard[] uncoveredCards;

    private final boolean isGold;

    /**
     * Deck constructor
     * @param isGold if true returns a Deck of GoldCards, otherwise one of ResourceCards
     */
    public Deck (boolean isGold) {
        // list needed to shuffle the cards after they're added
        List<ResourceCard> cardsList = new ArrayList<>();

        // initialize the json parser
        JsonParser parser = new JsonParser();
        if(!isGold){
            for(int i=1; i<=40; i++) {
                String cardName = "CardsJSON/resourceCards/resourceCard" + i + ".json";
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(cardName);
                // initialize the json file reader and save the card
                try(Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    JsonObject parsedResourceCard = parser.parse(reader).getAsJsonObject();
                    cardsList.add(Util.fromJSONtoResourceCard(parsedResourceCard));
                } catch (IOException e) {
                    System.err.println("Error parsing resource cards");
                }
            }
        } else {
            for(int i=1; i<=40; i++) {
                String cardName = "CardsJSON/goldCards/goldCard" + i + ".json";
                InputStream inputStream = getClass().getClassLoader().getResourceAsStream(cardName);
                // initialize the json file reader and save the card
                try(Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    JsonObject parsedGoldCard = parser.parse(reader).getAsJsonObject();
                    cardsList.add(Util.fromJSONtoGoldCard(parsedGoldCard));
                } catch (IOException e) {
                    System.err.println("Error parsing gold cards");
                }
            }
        }

        // shuffle the cards
        Collections.shuffle(cardsList);

        // save shuffled cards in the queue
        this.cards = new LinkedList<>();
        cards.addAll(cardsList);

        // set the first uncovered cards
        this.uncoveredCards = new ResourceCard[2];
        this.uncoveredCards[0] = cards.remove();
        this.uncoveredCards[0].flip();
        this.uncoveredCards[1] = cards.remove();
        this.uncoveredCards[1].flip();
        this.isGold = isGold;
    }

    /**
     * Deck constructor overloading for testing
     * @param isGold if true returns a Deck of GoldCards, otherwise one of ResourceCards
     * @param testing true if testing the deck
     */
    public Deck (boolean isGold, boolean testing) {
        // list needed to shuffle the cards after they're added
        List<ResourceCard> cardsList = new ArrayList<>();

        // initialize the json parser
        JsonParser parser = new JsonParser();
        if(!isGold){
            for(int i=1; i<=40; i++) {
                // dev path for intelliJ
                String cardName = "src/main/resources/CardsJSON/resourceCards/resourceCard" + i + ".json";
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(cardName);
                } catch (FileNotFoundException e) {
                    System.err.println("File not found: " + cardName);
                }
                // initialize the json file reader and save the card
                try(Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    JsonObject parsedResourceCard = parser.parse(reader).getAsJsonObject();
                    cardsList.add(Util.fromJSONtoResourceCard(parsedResourceCard));
                } catch (IOException e) {
                    System.err.println("Error parsing resource cards");
                }
            }
        } else {
            for(int i=1; i<=40; i++) {
                // dev path for intelliJ
                String cardName = "src/main/resources/CardsJSON/goldCards/goldCard" + i + ".json";
                InputStream inputStream = null;
                try {
                    inputStream = new FileInputStream(cardName);
                } catch (FileNotFoundException e) {
                    System.err.println("File not found: " + cardName);
                }
                // initialize the json file reader and save the card
                try(Reader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    JsonObject parsedGoldCard = parser.parse(reader).getAsJsonObject();
                    cardsList.add(Util.fromJSONtoGoldCard(parsedGoldCard));
                } catch (IOException e) {
                    System.err.println("Error parsing gold cards");
                }
            }
        }

        // shuffle the cards
        Collections.shuffle(cardsList);

        // save shuffled cards in the queue
        this.cards = new LinkedList<>();
        cards.addAll(cardsList);

        // set the first uncovered cards
        this.uncoveredCards = new ResourceCard[2];
        this.uncoveredCards[0] = cards.remove();
        this.uncoveredCards[0].flip();
        this.uncoveredCards[1] = cards.remove();
        this.uncoveredCards[1].flip();
        this.isGold = isGold;
    }

    /**
     * IsGold getter
     * @return a boolean telling whether the deck contains gold cards or not
     */
    public boolean isGold() {
        return isGold;
    }

    /**
     * Cards getter
     * @return the queue of cards
     */
    public Queue<ResourceCard> getCards() {
        return cards;
    }

    /**
     * Get the first card of the deck without drawing it
     * @return the card on the top of the deck
     */
    public ResourceCard getTopCard(){ return cards.peek(); }
    /**
     * Draw function (only called internally from the Deck class)
     * @param which can be 0 (from deck), 1(first uncovered card), 2(second uncovered card)
     * @throws NullPointerException when trying to draw from an uncovered card that does not exist
     * @return ResourceCard chosen
     */
    private ResourceCard Draw (int which) throws NullPointerException{
        ResourceCard cardToDraw;
        if (which == 0) {//drawing from deck
            cardToDraw = cards.poll(); //returns null if the deck is empty
            if(cardToDraw!=null) {
                cardToDraw.flip();
            }
        } else { //drawing from uncovered cards
            cardToDraw = uncoveredCards[which-1];
            if(cardToDraw!=null) {
                setUncoveredCard(which); //no need to call this if the uncovered card is already absent!
            }


        }

        return cardToDraw;
    }

    /**
     * Draw from the deck function accessible from the outside
     * @return ResourceCard chosen
     * @throws NullPointerException when the cards queue is empty
     */
    public ResourceCard DrawFromDeck() throws NullPointerException{
        if(cards.isEmpty()){
            throw new NullPointerException();
        }
        return Draw(0);
    }

    /**
     * Draw from the deck function accessible from the outside
     * @param which (should only be 0 or 1) indicating the index of the UncoveredResourceCard to draw
     * @return ResourceCard chosen
     * @throws NullPointerException when which is not 0 nor 1
     */
    public ResourceCard DrawFromUncovered(int which) throws NullPointerException{
        if(which != 0 && which != 1){
            throw new NullPointerException();
        }
        return Draw(which + 1);
    }

    /**
     * UncoveredCards getter
     * @return ResourceCard[]
     */
    public ResourceCard[] getUncoveredCards (){
        return this.uncoveredCards.clone();
    }

    /**
     * UncoveredCards setter, it is taken from the deck
     * @param which can be 1(first uncovered card) or 2(second uncovered card)
     * @throws IndexOutOfBoundsException if "which" is not 1 nor 2
     */
    public void setUncoveredCard (int which) throws NullPointerException{
        if(which!= 1 && which!=2)
            throw new IndexOutOfBoundsException();
        this.uncoveredCards[which-1] = this.Draw(0);
    }

    /**
     * Check if the deck is empty
     * @return true if the deck is empty and there is no uncovered card
     */
    public boolean isDeckEmpty () {
        return (this.cards.isEmpty() && this.uncoveredCards[0] == null && this.uncoveredCards[1] == null);
    }
}
