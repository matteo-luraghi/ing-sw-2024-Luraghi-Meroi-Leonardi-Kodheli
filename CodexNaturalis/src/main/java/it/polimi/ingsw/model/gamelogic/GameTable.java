package it.polimi.ingsw.model.gamelogic;

import it.polimi.ingsw.model.card.GoldCard;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * class GameTable
 * @author Francesk Kodheli
 */
public class GameTable {
    private Deck ResourceDeck;
    private Deck GoldDeck;
    private Map<Player, PlayerField> PlayerZones;
    private GoldCard[] CommonGoals; //TODO: THIS NEEDS TO BE GOALCARD BUT GOALCARD IS ABSTACT!!
    private ScoreBoard Scoreboard;

    /**
     * GameTable Constructor
     * @param ResourceDeck Deck with the initial resource cards
     * @param GoldDeck Deck with the initial gold cards
     * @param Players Arraylist that contains all the initialized players
     * @param PlayerFields Array list that contain the PlayerFields, wich will be mapped with the Players array list into the PlayerZones map
     * @param CommonGoals Array wich contains the common goals GoldCard
     * @param scoreboard The scoreboard that contains all the player scores
     */
    public GameTable(Deck ResourceDeck, Deck GoldDeck, ArrayList<Player> Players, ArrayList<PlayerField> PlayerFields, GoldCard[] CommonGoals, ScoreBoard scoreboard)
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
     * @param index d
     * @return GoldCard
     */
    public GoldCard getCommonGoal(int index) {
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
     * CountGoalPoints counts the goal points of a player given the PlayerField
     * @param Player
     */
    public int countGoalPoints (PlayerField Player)
    {
        return 0;
        //TODO: Complete this function once we have established how we handle coordinates
        /**
         * ...
         */
    }
}
