package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * PlayerField class
 * @author Lorenzo Meroi
 */
public class PlayerField {
    private ArrayList<ResourceCard> hand;

    private GoalCard privateGoal;

    private Map<Coordinates, GameCard> gameZone;

    private Map<Resource, Integer> resourceMap;

    /**
     * PlayerField constructor
     * @param startingCard the first card to be ever placed in the game zone
     */
    public PlayerField (StartingCard startingCard) {
        hand = new ArrayList<ResourceCard>();
        gameZone = new HashMap<Coordinates, GameCard>();
        gameZone.put(new Coordinates(0,0), startingCard);
        resourceMap = new HashMap<Resource, Integer>();

        for (Resource resource : startingCard.getPermanentResources()) {
            if (resource != null) {
                resourceMap.put(resource, resourceMap.get(resource) + 1);
            }
        }
        for (int i = 0; i<=3; i++) {
            Resource resource = startingCard.getCorner(i);
            if (resource!=null && !resource.equals(Resource.BLANK) && !resource.equals(Resource.HIDDEN)) {
                resourceMap.put(resource, resourceMap.get(resource)+1);
            }
        }

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
     * @param which whether to draw from the covered cards in the deck or from the uncovered ones (0 for deck, 1 for the first uncovered, 2 for the second)
     */
    public void Draw (Deck from, int which) {
        ResourceCard drawn = from.Draw(which);
        hand.add(drawn);
    }

    /**
     * method to play a card in a position
     * @param where the coordinates in which you want to play
     * @param card the card you want to play
     */
    public int Play (Coordinates where, ResourceCard card) {
        if (IsPlayable(where, card)) {
            gameZone.put(where, card);
            if (!card.getIsFront()) {
                Resource permanentResource = getResource(card);

                resourceMap.put(permanentResource, resourceMap.get(permanentResource)+1);
            }
            else {
                for (int i=0; i<4; i++) {
                    Resource resource = card.getCorner(i);
                    if (resource!=null && !resource.equals(Resource.HIDDEN) && !resource.equals(Resource.BLANK) && !resource.equals(Resource.COVERED)) {
                        resourceMap.put(resource, resourceMap.get(resource)+1);
                    }
                }
            }

            int cardX = where.getX();
            int cardY = where.getY();

            for (Coordinates coordinate : gameZone.keySet()) {
                int x = coordinate.getX();
                int y = coordinate.getY();
                if (x == cardX+1 && y == cardY+1) {

                    if (!gameZone.get(coordinate).getCorner(2).equals(Resource.BLANK))
                        resourceMap.put(gameZone.get(coordinate).getCorner(2), resourceMap.get(gameZone.get(coordinate).getCorner(2))-1);

                    gameZone.get(coordinate).coverCorner(2);

                } else if (x == cardX+1 && y == cardY-1) {

                    if (!gameZone.get(coordinate).getCorner(0).equals(Resource.BLANK))
                        resourceMap.put(gameZone.get(coordinate).getCorner(0), resourceMap.get(gameZone.get(coordinate).getCorner(0))-1);

                    gameZone.get(coordinate).coverCorner(0);

                } else if (x == cardX-1 && y == cardY-1) {

                    if (!gameZone.get(coordinate).getCorner(1).equals(Resource.BLANK))
                        resourceMap.put(gameZone.get(coordinate).getCorner(1), resourceMap.get(gameZone.get(coordinate).getCorner(1))-1);

                    gameZone.get(coordinate).coverCorner(1);

                } else if (x == cardX-1 && y == cardY+1) {

                    if (!gameZone.get(coordinate).getCorner(3).equals(Resource.BLANK))
                        resourceMap.put(gameZone.get(coordinate).getCorner(3), resourceMap.get(gameZone.get(coordinate).getCorner(3))-1);

                    gameZone.get(coordinate).coverCorner(3);

                }
            }

            if (!card.getIsGold()) {
                return card.getPoints();
            }
            else {
                return calculateCardPoints(where, (GoldCard) card);
            }
        }
        else{
            System.out.println("This card cannot be played here");
        }
        return 0;
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
        if (gameZone.containsKey(where))
            return false;
        if (card.getIsGold()) {
            if (!checkConditions((GoldCard) card))
                return false;
        }
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
                canTR = !card.getCorner(2).equals(Resource.HIDDEN);
            } else if (x == cardX+1 && y == cardY-1) {
                existsBR = true;
                canBR  =!card.getCorner(1).equals(Resource.HIDDEN);
            } else if (x == cardX-1 && y == cardY+1) {
                existsTL = true;
                canTL  =!card.getCorner(1).equals(Resource.HIDDEN);
            } else if (x == cardX-1 && y == cardY-1) {
                existsBL = true;
                canBL  =!card.getCorner(1).equals(Resource.HIDDEN);
            }
        }
        return (canTR && canBR && canTL && canBL && (existsTR || existsBR || existsTL || existsBL));
    }

    /**
     * method to calculate the points given when playing a card
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
     * @param privateGoal
     */
    public void setPrivateGoal(GoalCard privateGoal) {
        this.privateGoal = privateGoal;
    }

    /**
     * game zone getter
     * @return Map<Coordinates, GameCard>
     */
    public Map<Coordinates, GameCard> getGameZone() {
        return gameZone;
    }

    /**
     * resource from resource map getter
     * @param which resource to get
     * @return int
     */
    public int getResource (Resource which) {
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
}
