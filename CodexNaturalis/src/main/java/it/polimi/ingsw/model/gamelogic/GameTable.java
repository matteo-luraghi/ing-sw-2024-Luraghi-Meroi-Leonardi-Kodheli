package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

/**
 * class GameTable
 * @author Francesk Kodheli
 */
public class GameTable implements Serializable {
    @Serial
    private static final long serialVersionUID = -7226967485514133707L;
    private Deck ResourceDeck;
    private Deck GoldDeck;
    private Map<Player, PlayerField> PlayerZones;
    private GoalCard[] CommonGoals;
    private ScoreBoard Scoreboard;

    /**
     * GameTable Constructor
     * @param ResourceDeck Deck with the initial resource cards
     * @param GoldDeck Deck with the initial gold cards
     * @param PlayerZones Map that saves player and its player field
     * @param CommonGoals Array which contains the common goals GoalCard
     * @param scoreboard The scoreboard that contains all the player scores
     */
    public GameTable(Deck ResourceDeck, Deck GoldDeck, Map<Player, PlayerField> PlayerZones, GoalCard[] CommonGoals, ScoreBoard scoreboard)
    {
        this.ResourceDeck=ResourceDeck;
        this.GoldDeck=GoldDeck;
        this.PlayerZones= PlayerZones;
        this.CommonGoals=CommonGoals.clone();
        this.Scoreboard=scoreboard;
    }

    /**
     * ResourceDeck getter
     * @return Deck
     */
    public Deck getResourceDeck() {
        return ResourceDeck;
    }

    /**
     * GoldDeck getter
     * @return Deck
     */
    public Deck getGoldDeck() {
        return GoldDeck;
    }

    /**
     * PlayerZones getter
     * @return PlayerZones
     */
    public Map<Player, PlayerField> getPlayerZones()
    {
        return this.PlayerZones;
    }

    /**
     * CommonGoal by index getter
     *
     * @param index d
     * @return GoalCard
     */
    public GoalCard getCommonGoal(int index) {
        return CommonGoals[index];
    }

    /**
     * ScoreBoard getter
     * @return ScoreBoard
     */
    public ScoreBoard getScoreBoard() {
        return Scoreboard;
    }

    /**
     * Count of common goals points for a given playerfield
     * @param Player PlayerField
     * @return points of common goals int
     */
    private int countCommonGoalPoints(PlayerField Player)
    {
        int points=0;
        int min=0;

        for(int i=0; i<2;i++)
        {
            GoalCard commonGoal=getCommonGoal(i);
            if(commonGoal.isResourceGoal())
            {
                ResourceGoalCard ResourceGoal=(ResourceGoalCard) commonGoal;
                min=Integer.MAX_VALUE;
                HashMap<Resource, Float> resourcesNeeded = new HashMap<>();
                for(Resource resource: ResourceGoal.getRequirements()) {
                    resourcesNeeded.put(resource, resourcesNeeded.getOrDefault(resource, 0f)+1);
                }
                for(Resource resource: resourcesNeeded.keySet()){
                    float num = Player.getResourceFromMap(resource);
                    min = Math.min((int)Math.floor(num/resourcesNeeded.get(resource)), min);
                }
                points+=commonGoal.getPoints()*min; //points times minimum occurrences of that goal

            }
            else
            {
                //arraylist of counted cards contains already counted cards for a given positional goal
                //the search will always start at the lowest card in the field, trying to follow the goal requirement (going up or at sides). Priority on right.
            /*    int x=0,y=0, xtemp=x,ytemp=y;
                Coordinates currentCoordinates=new Coordinates(x,y);

                GameCard currentCard=Player.getGameZone().get(currentCoordinates);
                GameCard tempCard=currentCard;
                while(true)
                {


                    tempCard=Player.getDownRight(currentCard);
                    if(tempCard!=null)
                        currentCard = tempCard;

                    else //try to search on the left side
                    {
                        tempCard=Player.getDownLeft(currentCard);
                        if(tempCard!=null)
                            currentCard = tempCard;

                        else  //no cards neither left nor right, so reached the lowest point by only trying to go down on right side.
                            break;

                    }
                }*/
         //       GameCard currentPointer=currentCard;
                ArrayList<GameCard> usedCardsForGoal=new ArrayList<>(); //contains all the cards already used for an objective;


                PositionGoalCard positionalGoal=(PositionGoalCard) commonGoal;
                ArrayList<Direction> Directions=positionalGoal.getPositionsFromBase();


                //logic: will try to match the objective until it visited all the cards;
                for(GameCard currentCard : Player.getGameZone().values())
                {
                    GameCard currentPointer=currentCard;

                    if(positionalGoal.getResourceFromBase().get(0)!=currentCard.getKingdom())
                        continue;
                    ArrayList<GameCard> possibleCards = new ArrayList<>();
                    if (!usedCardsForGoal.contains(currentPointer))
                        for (int j = 0; j < Directions.size(); j++)  //also have to do this for each card that has not been used for the goal
                        {


                            Direction currentDir = Directions.get(j);
                            Kingdom currentResource = positionalGoal.getResourceFromBase().get(j + 1);
                            if (currentDir == Direction.TOP) {
                                currentPointer = Player.getUp(currentPointer);


                            } else if (currentDir == Direction.TOP_LEFT) {
                                currentPointer = Player.getUpLeft(currentPointer);


                            } else if (currentDir == Direction.TOP_RIGHT) {
                                currentPointer = Player.getUpRight(currentPointer);


                            }

                            if (currentPointer == null || currentPointer.getKingdom() != currentResource || usedCardsForGoal.contains(currentPointer))
                                break;
                            possibleCards.add(currentPointer);
                            if (j == Directions.size() - 1) {
                                points += commonGoal.getPoints();
                                usedCardsForGoal.addAll(possibleCards);
                            }
                        }
                }
                //to finish

                //TODO for positional ruling
            }
           // commonGoal.
        }
        return points;
    }

    /**
     * CountGoalPoints counts the goal points of a player given the PlayerField
     * @param Player
     */
    public int countGoalPoints (PlayerField Player)
    {
        int points=countCommonGoalPoints(Player);

        GoalCard privateGoal=Player.getPrivateGoal();
        if(privateGoal.isResourceGoal())
        {
            ResourceGoalCard ResourceGoal=(ResourceGoalCard)privateGoal;
            int min=Integer.MAX_VALUE;
            HashMap<Resource, Float> resourcesNeeded = new HashMap<>();
            for(Resource resource: ResourceGoal.getRequirements()) {
                resourcesNeeded.put(resource, resourcesNeeded.getOrDefault(resource, 0f)+1);
            }
            for(Resource resource: resourcesNeeded.keySet()){
                float num = Player.getResourceFromMap(resource);
                min = Math.min((int)Math.floor(num/resourcesNeeded.get(resource)), min);
            }
            points+=privateGoal.getPoints()*min; //points times minimum occurrences of that goal


        }
        else
        {

            ArrayList<GameCard> usedCardsForGoal=new ArrayList<>(); //contains all the cards already used for an objective;


            PositionGoalCard positionalGoal=(PositionGoalCard) privateGoal;
            ArrayList<Direction> Directions=positionalGoal.getPositionsFromBase();


            //logic: will try to match the objective until it visited all the cards;
            for(GameCard currentCard : Player.getGameZone().values())
            {
                GameCard currentPointer=currentCard;

                if(positionalGoal.getResourceFromBase().getFirst()!=currentCard.getKingdom())
                    continue;
                ArrayList<GameCard> possibleCards = new ArrayList<>();
                if (!usedCardsForGoal.contains(currentPointer))
                    for (int j = 0; j < Directions.size(); j++)  //also have to do this for each card that has not been used for the goal
                    {


                        Direction currentDir = Directions.get(j);
                        Kingdom currentResource = positionalGoal.getResourceFromBase().get(j + 1);
                        if (currentDir == Direction.TOP) {
                            currentPointer = Player.getUp(currentPointer);


                        } else if (currentDir == Direction.TOP_LEFT) {
                            currentPointer = Player.getUpLeft(currentPointer);


                        } else if (currentDir == Direction.TOP_RIGHT) {
                            currentPointer = Player.getUpRight(currentPointer);


                        }

                        if (currentPointer == null || currentPointer.getKingdom() != currentResource || usedCardsForGoal.contains(currentPointer))
                            break;
                        possibleCards.add(currentPointer);
                        if (j == Directions.size() - 1) {
                            points += privateGoal.getPoints();
                            usedCardsForGoal.addAll(possibleCards);
                        }
                    }
            }

        }
        Player player=Util.getKeyByValue(PlayerZones,Player);
        Scoreboard.addPoints(player,points);
        return points;

    }

}
