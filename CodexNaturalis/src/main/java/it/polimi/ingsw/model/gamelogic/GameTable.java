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
     *
     * @param ResourceDeck Deck with the initial resource cards
     * @param GoldDeck     Deck with the initial gold cards
     * @param PlayerZones  Map that saves player and its player field
     * @param CommonGoals  Array which contains the common goals GoalCard
     * @param scoreboard   The scoreboard that contains all the player scores
     */
    public GameTable(Deck ResourceDeck, Deck GoldDeck, Map<Player, PlayerField> PlayerZones, GoalCard[] CommonGoals, ScoreBoard scoreboard) {
        this.ResourceDeck = ResourceDeck;
        this.GoldDeck = GoldDeck;
        this.PlayerZones = PlayerZones;
        this.CommonGoals = CommonGoals.clone();
        this.Scoreboard = scoreboard;
    }

    /**
     * ResourceDeck getter
     *
     * @return Deck
     */
    public Deck getResourceDeck() {
        return ResourceDeck;
    }

    /**
     * GoldDeck getter
     *
     * @return Deck
     */
    public Deck getGoldDeck() {
        return GoldDeck;
    }

    /**
     * PlayerZones getter
     *
     * @return PlayerZones
     */
    public Map<Player, PlayerField> getPlayerZones() {
        return this.PlayerZones;
    }

    /**
     * Get a player zone for a given username
     * @param username the given username
     * @return the player zone of the user, or null if it wasnt found
     */
    public PlayerField getPlayerZoneForUser(String username){
        for(Player p : PlayerZones.keySet()){
            if(p.getNickname().equals(username)){
                return PlayerZones.get(p);
            }
        }

        //player field hasn't been found
        return null;
    }
    /**
     * CommonGoal by index getter
     *
     * @param index d
     * @return GoalCard
     */
    public GoalCard getCommonGoal(int index) throws IndexOutOfBoundsException {
        if(index!=0 && index!=1)
            throw new IndexOutOfBoundsException();
        else
         return CommonGoals[index];
    }

    /**
     * ScoreBoard getter
     *
     * @return ScoreBoard
     */
    public ScoreBoard getScoreBoard() {
        return Scoreboard;
    }

    /**
     * Given a combos arraylist and a combo it finds all the non overlapping (compared to combo) combos (the overlapping check is made only for the starting combo)
     * @param combos ArrayList of combos to check
     * @param combo starting combo
     * @return ArrayList<ArrayList<GameCard>> empty if all overlaps combo, otherwise the non overlapping ones
     */
    private ArrayList<ArrayList<GameCard>> getNotOverlappingCombo(ArrayList<ArrayList<GameCard>> combos, ArrayList<GameCard> combo) {
        ArrayList<ArrayList<GameCard>> nonOverlapping = new ArrayList<ArrayList<GameCard>>();
        boolean nonOverlaping;
        for (ArrayList<GameCard> currentCombo : combos) {
            nonOverlaping=true;
            for (GameCard card : combo) {
                if (currentCombo.contains(card))
                {
                    nonOverlaping=false;
                    break;
                }


            }
            if(nonOverlaping)
                nonOverlapping.add(currentCombo);
        }
        return nonOverlapping;
    }

    /**
     * Recursive Method to get the count of non overlapping combos starting by each one and trying all the choices
     * @param hisNotOverlappingCombos the not overlapping combos of a given starting combo
     * @return increments by one each time the method is called and it returns when all the branches get an empty nonOverlapping ArrayList
     */
    private int countOfNotOverlappingCombos(ArrayList<ArrayList<GameCard>> hisNotOverlappingCombos,int currentCount) {
        int maxCount=currentCount;
        for (ArrayList<GameCard> combo : hisNotOverlappingCombos) {
            ArrayList<ArrayList<GameCard>> nonOverlapping = new ArrayList<ArrayList<GameCard>>();
            nonOverlapping.addAll(getNotOverlappingCombo(hisNotOverlappingCombos, combo));
            if(nonOverlapping.isEmpty()) {

                maxCount=Math.max(countOfNotOverlappingCombos(nonOverlapping,currentCount+1),maxCount+1);

            }

        }
        return maxCount;
    }

    /**
     * method to find the maximum numbers of Non overlapping card combos in an Overlapping arrayList
     * Calls recursive method with each combo as starting combo
     * @param overlappingCombos overlapping combos
     * @return int number of maximum non overlapping combos
     */
    private int findMaxCombosInOverlappingCombos(ArrayList<ArrayList<GameCard>> overlappingCombos) {

        int maxCombos = overlappingCombos.isEmpty() ? 0 : 1;


        for (ArrayList<GameCard> combo : overlappingCombos) {
            ArrayList<ArrayList<GameCard>> nonOverlapping = new ArrayList<ArrayList<GameCard>>();

            nonOverlapping.addAll(getNotOverlappingCombo(overlappingCombos, combo));
            if(!nonOverlapping.isEmpty())
            maxCombos = Math.max(maxCombos, countOfNotOverlappingCombos(nonOverlapping,1));
        }

        return maxCombos;
    }

    /**
     * Count of common goals points for a given playerfield
     *
     * @param Player PlayerField
     * @return points of common goals int
     */
    private int countCommonGoalPoints(PlayerField Player) {
        int points = 0;
        int min;

        for (int i = 0; i < 2; i++) {
            GoalCard commonGoal = getCommonGoal(i);
            if (commonGoal.isResourceGoal()) {
                ResourceGoalCard ResourceGoal = (ResourceGoalCard) commonGoal;
                min = Integer.MAX_VALUE;
                HashMap<Resource, Float> resourcesNeeded = new HashMap<>();
                for (Resource resource : ResourceGoal.getRequirements()) {
                    resourcesNeeded.put(resource, resourcesNeeded.getOrDefault(resource, 0f) + 1);
                }
                for (Resource resource : resourcesNeeded.keySet()) {
                    float num = Player.getResourceFromMap(resource);
                    min = Math.min((int) Math.floor(num / resourcesNeeded.get(resource)), min);
                }
                points += commonGoal.getPoints() * min; //points times minimum occurrences of that goal
                getScoreBoard().addReachedPointsCount(Util.getKeyByValue(getPlayerZones(), Player), min);
            } else {


                PositionGoalCard positionalGoal = (PositionGoalCard) commonGoal;
                ArrayList<Direction> Directions = positionalGoal.getPositionsFromBase();
                ArrayList<ArrayList<GameCard>> PossibleCombos = new ArrayList<ArrayList<GameCard>>();

                //logic: will try to match the objective until it visited all the cards;
                for (GameCard currentCard : Player.getGameZone().values()) {
                    GameCard currentPointer = currentCard;

                    if (positionalGoal.getResourceFromBase().get(0) != currentCard.getKingdom())
                        continue;
                    ArrayList<GameCard> possibleCards = new ArrayList<>();
                    possibleCards.add(currentCard);
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

                        if (currentPointer == null || currentPointer.getKingdom() != currentResource)
                            break;
                        possibleCards.add(currentPointer);
                        if (j == Directions.size() - 1) {

                            PossibleCombos.add(possibleCards);

                        }
                    }
                }

                ArrayList<ArrayList<GameCard>> ChosenCombos = new ArrayList<ArrayList<GameCard>>();
                ArrayList<ArrayList<GameCard>> OverLappingCombos = new ArrayList<ArrayList<GameCard>>();

                for (ArrayList<GameCard> Combo : PossibleCombos) {
                    for (GameCard card : Combo) {

                        for (ArrayList<GameCard> Combo1 : PossibleCombos) {
                            if (Combo1.containsAll(Combo) && Combo1.size()==Combo.size())
                                continue;
                            if (Combo1.contains(card)) {


                                if (!OverLappingCombos.contains(Combo1)) {
                                    OverLappingCombos.add(Combo1);
                                }
                                if (!OverLappingCombos.contains(Combo)) {
                                    OverLappingCombos.add(Combo);
                                }


                            }

                        }



                    }
                    if (!OverLappingCombos.contains(Combo) && !ChosenCombos.contains(Combo)) {

                        ChosenCombos.add(Combo);


                    }
                }
                    int totalCombos = ChosenCombos.size();
                    if (!OverLappingCombos.isEmpty()) {
                        totalCombos += findMaxCombosInOverlappingCombos(OverLappingCombos);

                    }

                    getScoreBoard().addReachedPointsCount(Util.getKeyByValue(getPlayerZones(), Player), totalCombos);
                    points += totalCombos * commonGoal.getPoints();
                }


            }
            return points;

        }


    /**
     * CountGoalPoints counts the goal points of a player given the PlayerField
     *
     * @param Player
     * @return  the counted Goal Points
     */
    public int countGoalPoints(PlayerField Player) {
        int points = countCommonGoalPoints(Player);

        GoalCard privateGoal = Player.getPrivateGoal();
        if (privateGoal.isResourceGoal()) {
            ResourceGoalCard ResourceGoal = (ResourceGoalCard) privateGoal;
            int min = Integer.MAX_VALUE;
            HashMap<Resource, Float> resourcesNeeded = new HashMap<>();
            for (Resource resource : ResourceGoal.getRequirements()) {
                resourcesNeeded.put(resource, resourcesNeeded.getOrDefault(resource, 0f) + 1);
            }
            for (Resource resource : resourcesNeeded.keySet()) {
                float num = Player.getResourceFromMap(resource);
                min = Math.min((int) Math.floor(num / resourcesNeeded.get(resource)), min);
            }
            points += privateGoal.getPoints() * min; //points times minimum occurrences of that goal
            getScoreBoard().addReachedPointsCount(Util.getKeyByValue(getPlayerZones(),Player),min);

        } else {


            PositionGoalCard positionalGoal = (PositionGoalCard) privateGoal;
            ArrayList<Direction> Directions = positionalGoal.getPositionsFromBase();
            ArrayList<ArrayList<GameCard>> PossibleCombos = new ArrayList<ArrayList<GameCard>>();
            ArrayList<ArrayList<GameCard>> ChosenCombos = new ArrayList<ArrayList<GameCard>>();
            ArrayList<ArrayList<GameCard>> OverLappingCombos = new ArrayList<ArrayList<GameCard>>();
            //logic: will try to match the objective until it visited all the cards;
            for (GameCard currentCard : Player.getGameZone().values()) {
                GameCard currentPointer = currentCard;

                if (positionalGoal.getResourceFromBase().getFirst() != currentCard.getKingdom())
                    continue;
                ArrayList<GameCard> possibleCards = new ArrayList<>();
                possibleCards.add(currentCard);
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

                    if (currentPointer == null || currentPointer.getKingdom() != currentResource)
                        break;
                    possibleCards.add(currentPointer);
                    if (j == Directions.size() - 1) {

                        PossibleCombos.add(possibleCards);
                    }

                }
            }


                boolean OverlappingCard = false;

                for (ArrayList<GameCard> Combo : PossibleCombos) {
                    OverlappingCard = false;
                    for (GameCard card : Combo) {

                        for (ArrayList<GameCard> Combo1 : PossibleCombos) {
                            if (Combo1.containsAll(Combo) && Combo1.size()==Combo.size())
                                continue;
                            if (Combo1.contains(card)) {

                                if (!OverLappingCombos.contains(Combo1)) {
                                    OverLappingCombos.add(Combo1);
                                }
                                if (!OverLappingCombos.contains(Combo)) {
                                    OverLappingCombos.add(Combo);
                                }

                            }

                        }


                    }
                    if (!OverLappingCombos.contains(Combo) && !ChosenCombos.contains(Combo)) {

                        ChosenCombos.add(Combo);

                    }


                }


            int totalCombos=ChosenCombos.size();
            if(!OverLappingCombos.isEmpty()) {
                totalCombos += findMaxCombosInOverlappingCombos(OverLappingCombos) ;

            }
            points += totalCombos * privateGoal.getPoints();
            getScoreBoard().addReachedPointsCount(Util.getKeyByValue(getPlayerZones(),Player),totalCombos);
        }
        return points;
    }
}
