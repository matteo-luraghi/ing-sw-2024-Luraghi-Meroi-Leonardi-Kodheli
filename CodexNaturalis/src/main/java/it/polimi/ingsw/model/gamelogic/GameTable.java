package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.Resource;
import it.polimi.ingsw.model.card.ResourceGoalCard;
import it.polimi.ingsw.model.card.GameCard;

import java.util.*;


/**
 * class GameTable
 * @author Francesk Kodheli
 */
public class GameTable {
    private Deck ResourceDeck;
    private Deck GoldDeck;
    private Map<Player, PlayerField> PlayerZones;
    private GoalCard[] CommonGoals;
    private ScoreBoard Scoreboard;

    /**
     * GameTable Constructor
     * @param ResourceDeck Deck with the initial resource cards
     * @param GoldDeck Deck with the initial gold cards
     * @param Players Arraylist that contains all the initialized players
     * @param PlayerFields Array list that contain the PlayerFields, wich will be mapped with the Players array list into the PlayerZones map
     * @param CommonGoals Array wich contains the common goals GoalCard
     * @param scoreboard The scoreboard that contains all the player scores
     */
    public GameTable(Deck ResourceDeck, Deck GoldDeck, ArrayList<Player> Players, ArrayList<PlayerField> PlayerFields, GoalCard[] CommonGoals, ScoreBoard scoreboard)
    {
        this.ResourceDeck=ResourceDeck;
        this.GoldDeck=GoldDeck;
        this.PlayerZones=new HashMap<Player, PlayerField>();

        int i=0;
        for(PlayerField Playerfield: PlayerFields)
        {
            PlayerZones.put(Players.get(i), Playerfield);
            i++;
        }

        this.CommonGoals=CommonGoals.clone();
        this.Scoreboard=scoreboard;

    }

    /**
     * ResouceDeck getter
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
                ResourceGoalCard ResourceGoal=(ResourceGoalCard)commonGoal;
                min=Integer.MAX_VALUE;
                int numberOfResources;
                for(Resource resourceGoal: ResourceGoal.getRequirements())
                {
                    numberOfResources = Player.getResourceFromMap(resourceGoal); //number of occurrences of that resource
                    if(numberOfResources<min)
                    {
                        min=numberOfResources;
                    }
                }
                points=commonGoal.getPoints()*min; //points times minimum occurrences of that goal

            }
            else
            {
                //arraylist of counted cards contains already counted cards for a given positional goal
                //the search will always start at the lowest card in the field, trying to follow the goal requirement (going up or at sides). Priority on right.
                int x=0,y=0, xtemp=x,ytemp=y;
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
        int commonGoalPoints=countCommonGoalPoints(Player);

        return 0;
        //TODO: Complete this function once we have established how we handle coordinates
        /**
         * ...
         */
    }

}
