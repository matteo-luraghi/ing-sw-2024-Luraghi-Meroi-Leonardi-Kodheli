package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * PlayerField class
 * @author Lorenzo Meroi
 */
public class PlayerField implements Serializable {
    @Serial
    private static final long serialVersionUID = 1638400040589664145L;
    private ArrayList<ResourceCard> hand;

    private GoalCard privateGoal;

    private Map<Coordinates, GameCard> gameZone;

    private Map<Resource, Integer> resourceMap;

    /**
     * PlayerField constructor without the startingCard
     */
    public PlayerField(){
        hand = new ArrayList<ResourceCard>();
        gameZone = new HashMap<Coordinates, GameCard>();
        resourceMap = getInitializeResourceMap(); //resource map has to be initialized otherwise in resourceMap.get you get a null pointer
    }

    /**
     * PlayerField constructor
     * @param startingCard the first card to be ever placed in the game zone
     */
    public PlayerField (StartingCard startingCard) {
        hand = new ArrayList<ResourceCard>();
        gameZone = new HashMap<Coordinates, GameCard>();
        gameZone.put(new Coordinates(0,0), startingCard);
        resourceMap = getInitializeResourceMap(); //resource map has to be initialized otherwise in resourceMap.get you get a null pointer

        if (startingCard.getIsFront()) {
            for (Resource resource : startingCard.getPermanentResources()) {
                if (resource != null) {
                    resourceMap.put(resource, resourceMap.get(resource) + 1);
                }
            }
        }
        for (int i = 0; i <= 3; i++) {
            Resource resource = startingCard.getCorner(i);
            if (resource != null && !resource.equals(Resource.BLANK) && !resource.equals(Resource.HIDDEN)) {

                resourceMap.put(resource, resourceMap.get(resource) + 1);
            }
        }

    }

    /**
     * Adds the startingCard to the player field
     * @param startingCard the startingCard the player chose
     */
    public void addStartingCard(StartingCard startingCard){
        gameZone.put(new Coordinates(0,0), startingCard);

        if (startingCard.getIsFront()) {
            for (Resource resource : startingCard.getPermanentResources()) {
                if (resource != null) {
                    resourceMap.put(resource, resourceMap.get(resource) + 1);
                }
            }
        }
        for (int i = 0; i <= 3; i++) {
            Resource resource = startingCard.getCorner(i);
            if (resource != null && !resource.equals(Resource.BLANK) && !resource.equals(Resource.HIDDEN)) {

                resourceMap.put(resource, resourceMap.get(resource) + 1);
            }
        }
    }

    /**
     * Initialize the hashmap to have all the resources counter at 0
     * @return the initialized hashmap
     */
    private HashMap<Resource, Integer> getInitializeResourceMap()
    {
        HashMap<Resource, Integer> map=new HashMap<Resource, Integer>();
        for(int i=0;i<Resource.values().length;i++)
        {
            map.put(Resource.values()[i],0);
        }
        return map;
    }
    /**
     * Hand getter
     * @return ArrayList<ResourceCard>
     */
    public ArrayList<ResourceCard> getHand() {
        return hand;
    }

    /**
     * method to draw a card from a deck
     * @param from which deck (Resource or Gold cards) to draw from
     */
    private void drawFromDeck (Deck from) {
        ResourceCard drawn = from.DrawFromDeck();
        hand.add(drawn);
    }

    /**
     * method to draw an uncovered card
     * @param from which deck (Resource or Gold cards) to draw from
     * @param which whether to draw from the covered cards in the deck or from the uncovered ones (0 for the first uncovered, 1 for the second)
     */
    private void drawFromUncovered (Deck from, int which) {
        ResourceCard drawn = from.DrawFromUncovered(which);
        hand.add(drawn);
    }

    /**
     * method to draw a card
     * @param from which deck (Resource or Gold cards) to draw from
     * @param which whether to draw from the covered cards in the deck or from the uncovered ones (0 for the covered, 1 for the first uncovered, 2 for the second)
     */
    public void draw(Deck from, int which) throws NullPointerException{
        if(which == 0){
            drawFromDeck(from);
        } else if( which==1 || which==2){
            drawFromUncovered(from, which-1);
        } else{
            throw new NullPointerException();
        }

    }

    /**
     * method to play a card in a position
     * @param where the coordinates in which you want to play
     * @param card the card you want to play
     * @return the number of points that the card has gotten (-1 for unplaced card)
     */
//Mostly, have still to check properly limit cases
    public int Play (Coordinates where, ResourceCard card) {

        //better if we check
        if(!IsPlayable(where,card))
        {
            return -1;
        }
        for(ResourceCard cardInHand: hand)
        {
            if(cardInHand.equals(card))
            {
                card=cardInHand;
                break;
            }
        }

        gameZone.put(where, card);
        hand.remove(card);
        //Add the visible resources to the resourceMap
        if (!card.getIsFront()) {
            Resource permanentResource = getResource(card);
            resourceMap.put(permanentResource, resourceMap.get(permanentResource) + 1);
        } else {
            for (int i = 0; i < 4; i++) {
                Resource resource = card.getCorner(i);
                if (resource != null && !resource.equals(Resource.HIDDEN) && !resource.equals(Resource.BLANK) && !resource.equals(Resource.COVERED)) {
                    resourceMap.put(resource, resourceMap.get(resource) + 1);
                }
            }
        }

        int cardX = where.getX();
        int cardY = where.getY();
        //Loop over every card in the GameZone, if we find one adjacent to the card we are about to play
        //and the corner is not BLANK (we know it's not HIDDEN or COVERED thanks to the IsPlayable method

        for (Coordinates coordinate : gameZone.keySet()) {
            int x = coordinate.getX();
            int y = coordinate.getY();
            if (x == cardX + 1 && y == cardY + 1) { //TopRight

                if (!gameZone.get(coordinate).getCorner(2).equals(Resource.BLANK))
                    resourceMap.put(gameZone.get(coordinate).getCorner(2), resourceMap.get(gameZone.get(coordinate).getCorner(2)) - 1);

                gameZone.get(coordinate).coverCorner(2);

            } else if (x == cardX + 1 && y == cardY - 1) { //BottomRight

                if (!gameZone.get(coordinate).getCorner(3).equals(Resource.BLANK))
                    resourceMap.put(gameZone.get(coordinate).getCorner(3), resourceMap.get(gameZone.get(coordinate).getCorner(3)) - 1);

                gameZone.get(coordinate).coverCorner(3);

            } else if (x == cardX - 1 && y == cardY - 1) { //BottomLeft

                if (!gameZone.get(coordinate).getCorner(1).equals(Resource.BLANK))
                    resourceMap.put(gameZone.get(coordinate).getCorner(1), resourceMap.get(gameZone.get(coordinate).getCorner(1)) - 1);

                gameZone.get(coordinate).coverCorner(1);

            } else if (x == cardX - 1 && y == cardY + 1) { //TopLeft

                if (!gameZone.get(coordinate).getCorner(0).equals(Resource.BLANK))
                    resourceMap.put(gameZone.get(coordinate).getCorner(0), resourceMap.get(gameZone.get(coordinate).getCorner(0)) - 1);

                gameZone.get(coordinate).coverCorner(0);

            }
        }
        //TODO: Shouldn't we just update the scoreBoard?
        //Return the amount of points the card has scored
        if (!card.getIsGold()) {
            return card.getPoints();
        } else {
            return calculateCardPoints(where, (GoldCard) card);
        }
    }

    /**
     * method to get the permanent Resource of a Resource/gold card (the one on the back, corresponding to its kingdom)
     * @param card which card you want to get the resource from
     * @return the permanent resource of that card
     */
    private static Resource getResource(ResourceCard card) {
        Kingdom cardKingdom = card.getKingdom();
        Resource permanentResource = null;
        if (cardKingdom.equals(Kingdom.PLANT)) {
            permanentResource = Resource.PLANT;
        } else if (cardKingdom.equals(Kingdom.ANIMAL)) {
            permanentResource = Resource.ANIMAL;
        } else if (cardKingdom.equals(Kingdom.FUNGI)) {
            permanentResource = Resource.FUNGI;
        } else if (cardKingdom.equals(Kingdom.INSECT)) {
            permanentResource = Resource.INSECT;
        }
        return permanentResource;
    }

    /**
     * method to know if a card is playable in the given coordinates
     * @param where the coordinates in which you want to play the card
     * @param card the card you want to play
     * @return true if it's playable, false if it is not
     */
    public boolean IsPlayable (Coordinates where, ResourceCard card) {
        boolean foundCard=false;
        for(ResourceCard cardInHand: getHand())
        {
            if(cardInHand.equals(card)) {
                foundCard = true;
                break;
            }
        }
        if(!foundCard)
        {
            System.out.println("CARD not in Hand");
            return  false;
        }
        if (gameZone.containsKey(where)) {
            System.err.println("coordinate already occupied");
            return false;
        }
        if (card.getIsGold() && card.getIsFront()) {
            if (!checkConditions((GoldCard) card))
            {
                System.err.println("condition check failed");
                return false;
            }
        }
        if (Math.abs(where.getY() + where.getX()) % 2 == 1) {

                System.err.println("math check failed");
                return false;

        }
        //booleans that describe whether a nearby card exists and whether the corresponding corner is hidden or not
        boolean canTR = true;
        boolean existsTR = false;
        boolean canTL = true;
        boolean existsTL = false;
        boolean canBR = true;
        boolean existsBR = false;
        boolean canBL = true;
        boolean existsBL = false;


        int cardX = where.getX();
        int cardY = where.getY();

        for (Coordinates coordinate : gameZone.keySet()) {
            int x = coordinate.getX();
            int y = coordinate.getY();

            if (x == cardX+1 && y == cardY+1) {
                existsTR = true;
                canTR = !gameZone.get(coordinate).getCorner(2).equals(Resource.HIDDEN);
               // System.out.println("1 "+canTR+" "+existsTR);
            } else if (x == cardX+1 && y == cardY-1) {
                existsBR = true;
                canBR  =!gameZone.get(coordinate).getCorner(3).equals(Resource.HIDDEN);
             //   System.out.println("2 "+canBR+" "+existsBR);
            } else if (x == cardX-1 && y == cardY+1) {
                existsTL = true;
                canTL  =!gameZone.get(coordinate).getCorner(0).equals(Resource.HIDDEN);
               // System.out.println("3 "+canTL+" "+existsTL);
            } else if (x == cardX-1 && y == cardY-1) {
                existsBL = true;
                canBL  =!gameZone.get(coordinate).getCorner(1).equals(Resource.HIDDEN);
                //System.out.println("4 "+canBL+" "+existsBL);
            }
        }

        return (canTR && canBR && canTL && canBL && (existsTR || existsBR || existsTL || existsBL));
    }

    /**
     * method to calculate the points given when playing a gold card
     * @param where the coordinates in which you want to play the card
     * @param card the card you want to play
     * @return the points you get from playing it
     */
    private int calculateCardPoints (Coordinates where, GoldCard card) {
        switch (card.getPointCondition()) {
            case NORMAL -> {return card.getPoints();}

            case CORNER -> {
                int points = 0;
                int cardX = where.getX();
                int cardY = where.getY();

                for (Coordinates coordinates : gameZone.keySet()) {
                    int x = coordinates.getX();
                    int y = coordinates.getY();

                    if (x == cardX+1 && y == cardY+1)
                        points = points+2;
                    else if (x == cardX+1 && y == cardY-1)
                        points = points+2;
                    else if (x == cardX-1 && y == cardY+1)
                        points = points+2;
                    else if (x == cardX-1 && y == cardY-1)
                        points = points+2;
                }

                return points;
            }

            case POTION -> {return resourceMap.get(Resource.POTION);}

            case SCROLL -> {return resourceMap.get(Resource.SCROLL);}

            case FEATHER -> {return resourceMap.get(Resource.FEATHER);}

            default -> {return 0;}
        }

    }

    /**
     * method to check if the playable condition of a gold card is respected
     * @param card to check the conditions of
     * @return boolean
     */
    private boolean checkConditions(GoldCard card) {
        int fungi = 0;
        int animal = 0;
        int plant = 0;
        int insect = 0;

        for (Resource resource : card.getPlayableCondition()) {
            if (resource.equals(Resource.FUNGI))
                fungi++;
            else if (resource.equals(Resource.ANIMAL))
                animal++;
            else if (resource.equals(Resource.PLANT))
                plant++;
            else if (resource.equals(Resource.INSECT))
                insect++;
        }

        return (fungi<= resourceMap.get(Resource.FUNGI) && animal<= resourceMap.get(Resource.ANIMAL) && plant<= resourceMap.get(Resource.PLANT) && insect<= resourceMap.get(Resource.INSECT));
    }

    /**
     * private goal getter
     * @return GoalCard
     */
    public GoalCard getPrivateGoal() {
        return privateGoal;
    }

    /**
     * private goal setter
     * @param privateGoal chosen private goal
     */
    public void setPrivateGoal(GoalCard privateGoal) {
        this.privateGoal = privateGoal;
    }

    /**
     * game zone getter
     * @return Map of Coordinates and Game cards
     */
    public Map<Coordinates, GameCard> getGameZone() {
        return gameZone;
    }

    /**
     * resourceMap getter
     * @return the resource map of the playerfield
     */
    public Map<Resource, Integer> getResourceMap() {
        return resourceMap;
    }

    /**
     * resource from resource map getter
     * @param which resource to get
     * @return int
     */
    public int getResourceFromMap (Resource which) {
        return resourceMap.get(which);
    }

    /**
     * resource from resource map setter
     * @param which resource to set
     * @param how to set it
     */
    public void setResource(Resource which, int how) {
        resourceMap.put(which, how);
    }

    /**
     * given a card returns its coordinates if present in the map (1to1 mapping)
     * @param currentGameCard
     * @return coordinates of the card or null
     */
    public Coordinates getCoordinates(GameCard currentGameCard) {
        return Util.getKeyByValue(getGameZone(),currentGameCard);
    }

    /**
     * Given a gamecard returns the closest card down in the right
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getDownRight(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates rCoordinates=new Coordinates(currentCoordinates.getX()+1,currentCoordinates.getY()-1);
        return getGameZone().get(rCoordinates);
    }

    /**
     * Given a gamecard returns the closest card down in the left
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getDownLeft(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates lCoordinates=new Coordinates(currentCoordinates.getX()-1,currentCoordinates.getY()-1);
        return getGameZone().get(lCoordinates);
    }

    /**
     * Given a gamecard returns the closest card up in the right
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getUpRight(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates rCoordinates=new Coordinates(currentCoordinates.getX()+1,currentCoordinates.getY()+1);
        return getGameZone().get(rCoordinates);
    }

    /**
     * Given a gamecard returns the closest card up in the left
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getUpLeft(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates lCoordinates=new Coordinates(currentCoordinates.getX()-1,currentCoordinates.getY()+1);
        return getGameZone().get(lCoordinates);
    }
    /**
     * Given a gamecard returns the closest card up
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getUp(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates Coordinates=new Coordinates(currentCoordinates.getX(),currentCoordinates.getY()+1);
        return getGameZone().get(Coordinates);
    }

    /**
     * Given a gamecard returns the closest card down
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getDown(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates Coordinates=new Coordinates(currentCoordinates.getX(),currentCoordinates.getY()-1);
        return getGameZone().get(Coordinates);
    }

}
