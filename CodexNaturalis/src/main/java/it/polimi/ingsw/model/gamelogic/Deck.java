package it.polimi.ingsw.model.gamelogic;

import com.google.gson.*;
import it.polimi.ingsw.model.card.GoldCard;
import it.polimi.ingsw.model.card.Kingdom;
import it.polimi.ingsw.model.card.Resource;
import it.polimi.ingsw.model.card.ResourceCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Deck class
 * @author Lorenzo Meroi
 */
public class Deck {
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
                String cardPath = "./src/main/resources/CardsJSON/resourceCards/resourceCard" + i + ".json";
                // initialize the json file reader and save the card
                try(Reader reader = new FileReader(cardPath)) {
                    JsonObject parsedResourceCard = parser.parse(reader).getAsJsonObject();
                    cardsList.add(Util.fromJSONtoResourceCard(parsedResourceCard));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            for(int i=1; i<=40; i++) {
                String cardPath = "./src/main/resources/CardsJSON/goldCards/goldCard" + i + ".json";
                // initialize the json file reader and save the card
                try(Reader reader = new FileReader(cardPath)) {
                    JsonObject parsedGoldCard = parser.parse(reader).getAsJsonObject();
                    cardsList.add(Util.fromJSONtoGoldCard(parsedGoldCard));
                } catch (IOException e) {
                    e.printStackTrace();
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
        this.uncoveredCards[1] = cards.remove();
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
     * Draw function (only called internally from the Deck class)
     * @param which can be 0 (from deck), 1(first uncovered card), 2(second uncovered card)
     * @throws NullPointerException when trying to draw from an uncovered card that does not exist
     * @return ResourceCard chosen
     */
    private ResourceCard Draw (int which)  {
        if (which == 0) {//drawing from deck
            return cards.poll(); //returns null if the deck is empty
        } else { //drawing from uncovered cards
            try {
                ResourceCard drawn = uncoveredCards[which-1];
                if(drawn!=null) {
                    setUncoveredCard(which); //no need to call this if the uncovered card is already absent!
                }
                return drawn;
            } catch (NullPointerException e) {  //TODO: Custom exception?
                System.err.println("No such card in here");
            }
        }
        return null;
    }

    /**
     * Draw from the deck function accessible from the outside
     * @return ResourceCard chosen
     */
    public ResourceCard DrawFromDeck(){
        if(cards.isEmpty()){
            System.err.println("Deck empty, cannot Draw");
            return null;
        }
        return Draw(0);
    }

    /**
     * Draw from the deck function accessible from the outside
     * @param which (should only be 0 or 1) indicating the index of the UncoveredResourceCard to draw
     * @return ResourceCard chosen
     */
    public ResourceCard DrawFromUncovered(int which){
        if(which != 0 && which != 1){
            System.err.println("param which is outOfBounds");
            return null;
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
     * @param Which can be 1(first uncovered card) or 2(second uncovered card)
     */
    public void setUncoveredCard (int Which) {
        this.uncoveredCards[Which-1] = this.Draw(0);
    }

    public boolean isDeckEmpty () {
        return (this.cards.isEmpty() && this.uncoveredCards[0] == null && this.uncoveredCards[1] == null);
    }
}
