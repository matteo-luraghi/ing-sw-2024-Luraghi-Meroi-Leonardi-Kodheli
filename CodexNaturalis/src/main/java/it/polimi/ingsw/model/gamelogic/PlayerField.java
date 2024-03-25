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
    private ArrayList<ResourceCard> Hand;

    private GoalCard PrivateGoal;

    private Map<Coordinates, GameCard> GameZone;

    private Map<Resource, Integer> ResourceMap;

    /**
     * PlayerField constructor
     * @param startingCard the first card to be ever placed in the game zone
     */
    public PlayerField (StartingCard startingCard) {
        Hand = new ArrayList<ResourceCard>();
        GameZone = new HashMap<Coordinates, GameCard>();
        GameZone.put(new Coordinates(0,0), startingCard);
        ResourceMap = new HashMap<Resource, Integer>();

        for (Resource resource : startingCard.getPermanentResources()) {
            if (resource != null) {
                ResourceMap.put(resource, ResourceMap.get(resource) + 1);
            }
        }
        for (int i = 0; i<=3; i++) {
            Resource resource = startingCard.getCorner(i);
            if (resource!=null && !resource.equals(Resource.BLANK) && !resource.equals(Resource.HIDDEN)) {
                ResourceMap.put(resource, ResourceMap.get(resource)+1);
            }
        }

    }

    /**
     * Hand getter
     * @return ArrayList<ResourceCard>
     */
    public ArrayList<ResourceCard> getHand() {
        return Hand;
    }

    /**
     * method to draw a card from a deck
     * @param from which deck (Resource or Gold cards) to draw from
     * @param which whether to draw from the covered cards in the deck or from the uncovered ones (0 for deck, 1 for the first uncovered, 2 for the second)
     */
    public void Draw (Deck from, int which) {
        ResourceCard drawn = from.Draw(which);
        Hand.add(drawn);
    }

    /**
     * method to play a card in a position
     * @param where the coordinates in which you want to play
     * @param card the card you want to play
     */
    public int Play (Coordinates where, ResourceCard card) {
        if (IsPlayable(where, card)) {
            GameZone.put(where, card);
            if (!card.getIsFront()) {
                Resource permanentResource = getResource(card);

                ResourceMap.put(permanentResource, ResourceMap.get(permanentResource)+1);
            }
            else {
                for (int i=0; i<4; i++) {
                    Resource resource = card.getCorner(i);
                    if (resource!=null && !resource.equals(Resource.HIDDEN) && !resource.equals(Resource.BLANK) && !resource.equals(Resource.COVERED)) {
                        ResourceMap.put(resource, ResourceMap.get(resource)+1);
                    }
                }
            }

            int cardX = where.getX();
            int cardY = where.getY();

            for (Coordinates coordinate : GameZone.keySet()) {
                int x = coordinate.getX();
                int y = coordinate.getY();
                if (x == cardX+1 && y == cardY+1) {

                    if (!GameZone.get(coordinate).getCorner(2).equals(Resource.BLANK))
                        ResourceMap.put(GameZone.get(coordinate).getCorner(2), ResourceMap.get(GameZone.get(coordinate).getCorner(2))-1);

                    GameZone.get(coordinate).coverCorner(2);

                } else if (x == cardX+1 && y == cardY-1) {

                    if (!GameZone.get(coordinate).getCorner(0).equals(Resource.BLANK))
                        ResourceMap.put(GameZone.get(coordinate).getCorner(0), ResourceMap.get(GameZone.get(coordinate).getCorner(0))-1);

                    GameZone.get(coordinate).coverCorner(0);

                } else if (x == cardX-1 && y == cardY-1) {

                    if (!GameZone.get(coordinate).getCorner(1).equals(Resource.BLANK))
                        ResourceMap.put(GameZone.get(coordinate).getCorner(1), ResourceMap.get(GameZone.get(coordinate).getCorner(1))-1);

                    GameZone.get(coordinate).coverCorner(1);

                } else if (x == cardX-1 && y == cardY+1) {

                    if (!GameZone.get(coordinate).getCorner(3).equals(Resource.BLANK))
                        ResourceMap.put(GameZone.get(coordinate).getCorner(3), ResourceMap.get(GameZone.get(coordinate).getCorner(3))-1);

                    GameZone.get(coordinate).coverCorner(3);

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
        if (GameZone.containsKey(where))
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

        for (Coordinates coordinate : GameZone.keySet()) {
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

                for (Coordinates coordinates : GameZone.keySet()) {
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

            case POTION -> {return ResourceMap.get(Resource.POTION);}

            case SCROLL -> {return ResourceMap.get(Resource.SCROLL);}

            case FEATHER -> {return ResourceMap.get(Resource.FEATHER);}

            default -> {return 0;}
        }

    }

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

        return (fungi<=ResourceMap.get(Resource.FUNGI) && animal<=ResourceMap.get(Resource.ANIMAL) && plant<=ResourceMap.get(Resource.PLANT) && insect<=ResourceMap.get(Resource.INSECT));
    }

    /**
     * private goal getter
     * @return GoalCard
     */
    public GoalCard getPrivateGoal() {
        return PrivateGoal;
    }

    /**
     * private goal setter
     * @param privateGoal
     */
    public void setPrivateGoal(GoalCard privateGoal) {
        PrivateGoal = privateGoal;
    }

    /**
     * game zone getter
     * @return Map<Coordinates, GameCard>
     */
    public Map<Coordinates, GameCard> getGameZone() {
        return GameZone;
    }

    /**
     * resource from resource map getter
     * @param which resource to get
     * @return int
     */
    public int getResource (Resource which) {
        return ResourceMap.get(which);
    }

    /**
     * resource from resource map setter
     * @param which resource to set
     * @param how to set it
     */
    public void setResource(Resource which, int how) {
        ResourceMap.put(which, how);
    }
}
