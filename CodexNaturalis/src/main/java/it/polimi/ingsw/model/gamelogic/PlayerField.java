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
    private ArrayList<GameCard> InPlayOrder;
    private Map<Resource, Integer> resourceMap;

    /**
     * PlayerField constructor without the startingCard
     */
    public PlayerField(){
        hand = new ArrayList<ResourceCard>();
        gameZone = new HashMap<Coordinates, GameCard>();
        resourceMap = getInitializeResourceMap(); //resource map has to be initialized otherwise in resourceMap.get you get a null pointer
        InPlayOrder=new ArrayList<GameCard>();
    }

    /**
     * PlayerField constructor
     * @param startingCard the first card to be ever placed in the game zone
     */
    public PlayerField (StartingCard startingCard) {
        hand = new ArrayList<ResourceCard>();
        gameZone = new HashMap<Coordinates, GameCard>();
        gameZone.put(new Coordinates(0, 0), startingCard);
        resourceMap = getInitializeResourceMap(); //resource map has to be initialized otherwise in resourceMap.get you get a null pointer
        InPlayOrder = new ArrayList<GameCard>();
        if (startingCard.getIsFront()) { //add to the resource map the starting card's permanent resources
            for (Resource resource : startingCard.getPermanentResources()) {
                if (resource != null) {
                    resourceMap.put(resource, resourceMap.get(resource) + 1);

                }
            }
        }
        InPlayOrder.add(startingCard); //add the starting card as the first card
        for (int i = 0; i <= 3; i++) { //add to the resource map the starting card's corner resources
            Resource resource = startingCard.getCorner(i);
            if (resource != null && !resource.equals(Resource.BLANK) && !resource.equals(Resource.HIDDEN)) {

                resourceMap.put(resource, resourceMap.get(resource) + 1);
            }
        }

    }

    /**
     * Checks that with the current hand the player can make a move
     * Checks indistinctly all the possible coordinates within the playable range (also occupied ones) by calling IsPlayable
     * @return true if the player can make a move otherwise false (should skip the turn)
     */
    public boolean canPlayHand() {
        ResourceCard card = getHand().get(0);
        card.flip();
        for(GameCard placedCard: getGameZone().values()) {
            Coordinates coordinates=getCoordinates(placedCard);
            if(IsPlayable(new Coordinates(coordinates.getX()-1, coordinates.getY()+1),card)
                    || IsPlayable(new Coordinates(coordinates.getX()+1, coordinates.getY()+1),card)
                    || IsPlayable(new Coordinates(coordinates.getX()-1, coordinates.getY()-1),card)
                    || IsPlayable(new Coordinates(coordinates.getX()+1, coordinates.getY()-1),card)) {
                card.flip();
                return true;
            }
        }
        card.flip();
        return false;
    }

    /**
     * inOrderPlayList getter, this arraylist is useful for keeping track of the placing
     * order in the gui interface (overlapping corners)
     * @return GameCard arraylist
     */
    public ArrayList<GameCard> getInPlayOrderList()
    {
        return InPlayOrder;
    }

    /**
     * get in order of play the corresponding card id
     * @return Id Arraylist of the cards
     */
    public ArrayList<Integer> getInPlayOrderCardId()
    {
        ArrayList<Integer> IdList=new ArrayList<>();
        for(int i=0;i<InPlayOrder.size();i++)
        {
            IdList.add(InPlayOrder.get(i).getId());
        }
        return IdList;
    }
    /**
     * Adds the startingCard to the player field
     * @param startingCard the startingCard the player chose
     */
    public void addStartingCard(StartingCard startingCard){
        gameZone.put(new Coordinates(0,0), startingCard);

        if (startingCard.getIsFront()) { //add to the resource map the starting card's permanent resources
            for (Resource resource : startingCard.getPermanentResources()) {
                if (resource != null) {
                    resourceMap.put(resource, resourceMap.get(resource) + 1);
                }
            }
        }
        for (int i = 0; i <= 3; i++) { //add to the resource map the starting card's corner resources
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
        ResourceCard card = from.DrawFromDeck();
        hand.add(card);
    }

    /**
     * method to draw an uncovered card
     * @param from which deck (Resource or Gold cards) to draw from
     * @param which whether to draw from the covered cards in the deck or from the uncovered ones (0 for the first uncovered, 1 for the second)
     */
    private void drawFromUncovered (Deck from, int which) {
        ResourceCard card = from.DrawFromUncovered(which);
        hand.add(card);
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

        gameZone.put(where, card);

        hand.remove(Util.checkIfResourceCardIsPresent(hand, card));
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
                { if(gameZone.get(coordinate).getIsFront() || (coordinate.getX()==0 && coordinate.getY()==0)) {
                    if (!gameZone.get(coordinate).getCorner(2).equals(Resource.BLANK))
                        resourceMap.put(gameZone.get(coordinate).getCorner(2), resourceMap.get(gameZone.get(coordinate).getCorner(2)) - 1);

                    gameZone.get(coordinate).coverCorner(2);
                }
                }
            } else if (x == cardX + 1 && y == cardY - 1) { //BottomRight
                if(gameZone.get(coordinate).getIsFront()|| (coordinate.getX()==0 && coordinate.getY()==0)) {
                    if (!gameZone.get(coordinate).getCorner(0).equals(Resource.BLANK))
                        resourceMap.put(gameZone.get(coordinate).getCorner(0), resourceMap.get(gameZone.get(coordinate).getCorner(0)) - 1);

                    gameZone.get(coordinate).coverCorner(0);
                }
            } else if (x == cardX - 1 && y == cardY - 1) { //BottomLeft
                if(gameZone.get(coordinate).getIsFront() || (coordinate.getX()==0 && coordinate.getY()==0)) {
                    if (!gameZone.get(coordinate).getCorner(1).equals(Resource.BLANK))
                        resourceMap.put(gameZone.get(coordinate).getCorner(1), resourceMap.get(gameZone.get(coordinate).getCorner(1)) - 1);

                    gameZone.get(coordinate).coverCorner(1);
                }
            } else if (x == cardX - 1 && y == cardY + 1) { //TopLeft
                if(gameZone.get(coordinate).getIsFront() || (coordinate.getX()==0 && coordinate.getY()==0))
                {
                if (!gameZone.get(coordinate).getCorner(3).equals(Resource.BLANK))
                    resourceMap.put(gameZone.get(coordinate).getCorner(3), resourceMap.get(gameZone.get(coordinate).getCorner(3)) - 1);

                gameZone.get(coordinate).coverCorner(3);
}
            }
        }
        int points=0;
        if (!card.getIsGold()) {
            points=card.getPoints();
        } else {
            points=calculateCardPoints(where, (GoldCard) card);
        }
        InPlayOrder.add(card); //keep track of the play order
        //Return the amount of points the card has scored
        if(!card.getIsFront())
            return 0;

        return points;
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
     * Method to get a gamecard by searching its coordinates (no the same object in tha map)
     * @param coordinates Coordinates of the searched card
     * @return the searched card or null
     */
    public GameCard getGameCardByEqualCoordinate(Coordinates coordinates)
    {
        for(Coordinates coordinates1:gameZone.keySet())
        {
            if(coordinates1.getX()== coordinates.getX() && coordinates1.getY()== coordinates.getY())
                return gameZone.get(coordinates1);
        }
        return null;
    }
    /**
     * method to know if a card is playable in the given coordinates
     * @param where the coordinates in which you want to play the card
     * @param card the card you want to play
     * @return true if it's playable, false if it is not
     */
    public boolean IsPlayable (Coordinates where, ResourceCard card) {
        if(Util.checkIfResourceCardIsPresent(hand, card)==null)
        {
        //    System.err.println("CARD not in Hand");
            return  false;
        }
        for(Coordinates coordinate: gameZone.keySet())
        {
            if(coordinate.getX()== where.getX() && coordinate.getY()== where.getY()) {
         //       System.err.println("Place already occupied");
                return false;
            }}
        if (card.getIsGold() && card.getIsFront()) {
            if (!checkConditions((GoldCard) card))
            {
              //  System.err.println("condition check failed");
                return false;
            }
        }
        if (Math.abs(where.getY() + where.getX()) % 2 == 1) {

               // System.err.println("math check failed");
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

            if (x == cardX+1 && y == cardY+1) { //cardx=-1 cardy=-1
                existsTR = true;
                canTR = !gameZone.get(coordinate).getCorner(2).equals(Resource.HIDDEN) || !gameZone.get(coordinate).getIsFront() ;
               // System.out.println("1 "+canTR+" "+existsTR);
            } else if (x == cardX+1 && y == cardY-1) { //cardx=-1 cardy=+1
                existsBR = true;
                canBR  =!gameZone.get(coordinate).getCorner(0).equals(Resource.HIDDEN)|| !gameZone.get(coordinate).getIsFront();
             //   System.out.println("2 "+canBR+" "+existsBR);
            } else if (x == cardX-1 && y == cardY+1) { //cardx=1 cardy=-1
                existsTL = true;
                canTL  =!gameZone.get(coordinate).getCorner(3).equals(Resource.HIDDEN)|| !gameZone.get(coordinate).getIsFront();
               // System.out.println("3 "+canTL+" "+existsTL);
            } else if (x == cardX-1 && y == cardY-1) { //cardx=1 cardy=1
                existsBL = true;
                canBL  =!gameZone.get(coordinate).getCorner(1).equals(Resource.HIDDEN)|| !gameZone.get(coordinate).getIsFront();
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
    public int calculateCardPoints (Coordinates where, GoldCard card) {
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
        if(!card.getIsFront())
            return true;
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
        return getGameCardByEqualCoordinate(rCoordinates);
    }

    /**
     * Given a gamecard returns the closest card down in the left
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getDownLeft(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates lCoordinates=new Coordinates(currentCoordinates.getX()-1,currentCoordinates.getY()-1);
        return getGameCardByEqualCoordinate(lCoordinates);
    }

    /**
     * Given a gamecard returns the closest card up in the right
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getUpRight(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates rCoordinates=new Coordinates(currentCoordinates.getX()+1,currentCoordinates.getY()+1);
        return getGameCardByEqualCoordinate(rCoordinates);
    }

    /**
     * Given a gamecard returns the closest card up in the left
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getUpLeft(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates lCoordinates=new Coordinates(currentCoordinates.getX()-1,currentCoordinates.getY()+1);
        return getGameCardByEqualCoordinate(lCoordinates);
    }
    /**
     * Given a gamecard returns the closest card up
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getUp(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates Coordinates=new Coordinates(currentCoordinates.getX(),currentCoordinates.getY()+1);
        return getGameCardByEqualCoordinate(Coordinates);
    }

    /**
     * Given a gamecard returns the closest card down
     * @param currentGameCard
     * @return null if not present or the gamecard if present
     */
    public GameCard getDown(GameCard currentGameCard) {
        Coordinates currentCoordinates=getCoordinates(currentGameCard);
        Coordinates Coordinates=new Coordinates(currentCoordinates.getX(),currentCoordinates.getY()-1);
        return getGameCardByEqualCoordinate(Coordinates);
    }

}
