package it.polimi.ingsw.model.gamelogic;

/**
 * class GameTable
 * @author Francesk Kodheli
 */
public class GameTable {
    private Deck ResourceDeck;
    private Deck GoldDeck;
    private Map<Player, PlayerField> PlayerZones;
    private GoldCard[] CommonGoals;
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
            PlayerZone.put(Players.get(i), Playerfield);
            i++;
        }

        this.CommonGoals=CommonGoals.clone();
        this.Scoreboard=scoreboard;

    }

    /**
     * ResouceDeck getter
     * @return Deck
     */
    public Deck GetResourceDeck() {
        return ResourceDeck;
    }

    /**
     * GoldDeck getter
     * @return Deck
     */
    public Deck GetGoldDeck() {
        return GoldDeck;
    }

    /**
     * CommonGoal by index getter
     * @param index
     * @return GoldCard
     */
    public GoldCard GetCommonGoal(int index) {
        return CommonGoals[index];
    }

    /**
     * ScoreBoard getter
     * @return ScoreBoard
     */
    public ScoreBoard GetScoreBoard() {
        return Scoreboard;
    }

    /**
     * CountGoalPoints counts the goal points of a player given the PlayerField
     * @param Player
     */
    public CountGoalPoints (PlayerField Player)
    {
        /**
         * ...
         */
    }
}
