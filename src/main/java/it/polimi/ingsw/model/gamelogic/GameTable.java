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
     * @throws IndexOutOfBoundsException if index is not 0 nor 1
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
     * @return ArrayList<ArrayList<GameCard>> empty if all overlaps combo, otherwise the non overlapping ones (the initial combo will not be in the returned arraylist)
     */
    private ArrayList<ArrayList<GameCard>> getNotOverlappingCombo(ArrayList<ArrayList<GameCard>> combos, ArrayList<GameCard> combo) {
        ArrayList<ArrayList<GameCard>> nonOverlapping = new ArrayList<ArrayList<GameCard>>();
        boolean nonOverlaping;
        for (ArrayList<GameCard> currentCombo : combos) {

            nonOverlaping=true; /*start with the presumption that's not overlapping*/
            for (GameCard card : combo) {
                if (currentCombo.contains(card))
                {
                    nonOverlaping=false; /*if a "currentCombo" contains the same card as the given "combo" then it won't be added to the nonOverlapping ArrayList*/
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
     * note: complexity of maximum n!, but since there are very few cases due to the limited card numbers, the implementation will be
     * fast enough for any case and won't waste too much memory.
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
                //every time a combo has at least one non overlapping combo then the current count will be increased by one

            }

        }
        /*
         * reached the final point where nonOverlapping is empty will have in max count the
         * count of all the considered combos, this will be compared with Math.max together
         * with all the other possible non overlapping combinations
         */
        return maxCount;


    }

    /**
     * method to find the maximum numbers of Non overlapping card combos in an Overlapping arrayList
     * Calls recursive method with each combo as starting combo
     * note: could have called directly a recursive method, but prefered to separate the incipit for clarity purpose.
     * @param overlappingCombos overlapping combos
     * @return int number of maximum non overlapping combos
     */
    private int findMaxCombosInOverlappingCombos(ArrayList<ArrayList<GameCard>> overlappingCombos) {

        int maxCombos = overlappingCombos.isEmpty() ? 0 : 1;

        /*
         * this starts the recursive function for each of the combos in
         * the ArrayList
         */
        for (ArrayList<GameCard> combo : overlappingCombos) {
            ArrayList<ArrayList<GameCard>> nonOverlapping = new ArrayList<ArrayList<GameCard>>();
            /*
             * adds in nonOverlapping all the not overlapping (respectively to the given "combo") combos
             * and then start the recursion with the given nonOverlapping ArrayList
             */
            nonOverlapping.addAll(getNotOverlappingCombo(overlappingCombos, combo));
            if(!nonOverlapping.isEmpty())
            maxCombos = Math.max(maxCombos, countOfNotOverlappingCombos(nonOverlapping,1));
        }

        return maxCombos;
    }

    /**
     * Count of common goals points for a given playerfield
     *
     * @param playerField PlayerField
     * @return points of common goals int
     */
    private int countCommonGoalPoints(PlayerField playerField) {
        int points = 0;
        int min;

        for (int i = 0; i < 2; i++) {
            GoalCard commonGoal = getCommonGoal(i);
            if (commonGoal.isResourceGoal()) { /*case the goal is Resource type */
                /*
                 * briefly checks with the support of the ResourceMap how many times the given goal can be satisfied
                 * with the current PlayerField
                 */
                ResourceGoalCard ResourceGoal = (ResourceGoalCard) commonGoal;
                min = Integer.MAX_VALUE;
                HashMap<Resource, Float> resourcesNeeded = new HashMap<>();
                /*
                 * puts in a Map the needed Resources with their corresponding count
                 */
                for (Resource resource : ResourceGoal.getRequirements()) {
                    resourcesNeeded.put(resource, resourcesNeeded.getOrDefault(resource, 0f) + 1);
                }
                /*
                 * calculates the minimum occurrences of all the needed resources
                 */
                for (Resource resource : resourcesNeeded.keySet()) {
                    float num = playerField.getResourceFromMap(resource);
                    min = Math.min((int) Math.floor(num / resourcesNeeded.get(resource)), min);
                }
                points += commonGoal.getPoints() * min; //points times minimum occurrences of that goal
                getScoreBoard().addReachedPointsCount(Util.getKeyByValue(getPlayerZones(), playerField), min);
            } else { /* case the goal is Positional type */


                PositionGoalCard positionalGoal = (PositionGoalCard) commonGoal;
                ArrayList<Direction> Directions = positionalGoal.getPositionsFromBase();
                /*Will contain all the possibles card
                 combination that respects the goal*/
                ArrayList<ArrayList<GameCard>> PossibleCombos = new ArrayList<ArrayList<GameCard>>();

               /*For each card in the Game Zone will check if (with the near cards) it satisfies the goal
                * note: will also consider all the overlapping ones, since the rules indicates that for a single goal
                *       can consider a card only one time will use a method to get the maximum number of combinations
                *       that considers all the found cards maximum once
                * */
                for (GameCard currentCard : playerField.getGameZone().values()) {
                    GameCard currentPointer = currentCard;

                    /*
                     * if it's not the same kingdom then we can pass to the next card and discard the current one as base
                     */
                    if (positionalGoal.getResourceFromBase().get(0) != currentCard.getKingdom())
                        continue;
                    ArrayList<GameCard> possibleCards = new ArrayList<>();
                    possibleCards.add(currentCard);
                    /*
                     * for all the directions in the goal will check if the current combination (with currentCard as base)
                     * can be matched in the Game Zone
                     */
                    for (int j = 0; j < Directions.size(); j++)  //also have to do this for each card that has not been used for the goal
                    {

                        Direction currentDir = Directions.get(j);

                        Kingdom currentResource = positionalGoal.getResourceFromBase().get(j + 1);

                        /*
                         * get respectively to the current pointer (the current card that is satisfying the goal)
                         * the next card in the game zone that matches the goal request
                         */
                        if (currentDir == Direction.TOP)
                        {
                            currentPointer = playerField.getUp(currentPointer);
                        }
                        else if (currentDir == Direction.TOP_LEFT)
                        {
                            currentPointer = playerField.getUpLeft(currentPointer);

                        }
                        else if (currentDir == Direction.TOP_RIGHT)
                        {
                            currentPointer = playerField.getUpRight(currentPointer);
                        }

                        /*
                         * if the card is found, and it has the requested kingdom, then it can be added to the
                         * possible Cards for the combination
                         */
                        if (currentPointer == null || currentPointer.getKingdom() != currentResource)
                            break;
                        possibleCards.add(currentPointer);
                        /*
                         * on the last goal request if it didn't break on any check the possibleCards will be considered
                         * then as a possible Combo, then it can be checked if it overlaps with any other combination
                         */
                        if (j == Directions.size() - 1)
                        {
                            PossibleCombos.add(possibleCards);
                        }
                    }
                }

                int ChosenCombos = 0;
                ArrayList<ArrayList<GameCard>> OverLappingCombos = new ArrayList<ArrayList<GameCard>>();

                /*
                 * for every found combo checks if any other combo is overlapping with it
                 */
                for (ArrayList<GameCard> combo : PossibleCombos) {
                    /*
                     * for each card checks all the combos, could also do the other way, but preferred as this
                     */
                    for (GameCard card : combo) {

                        for (ArrayList<GameCard> combo1 : PossibleCombos) {
                            /*
                             * cannot be checking the same combo, as clearly it would be tagged as overlapping with itself
                             */
                            if (combo1.equals(combo))
                                continue;
                            /*if a "combo1" contains any card of the given combo then will add, if not present, combo1 and
                             * combo to the overlapping ArrayList
                             */
                            if (combo1.contains(card)) {


                                if (!OverLappingCombos.contains(combo1)) {
                                    OverLappingCombos.add(combo1);
                                }
                                if (!OverLappingCombos.contains(combo)) {
                                    OverLappingCombos.add(combo);
                                }


                            }

                        }

                    }
                    /*
                     * finally make the check that if the combo is not in the overlapping ones it can be counted as a
                     * valid combo
                     */
                    if (!OverLappingCombos.contains(combo))
                    {
                        ChosenCombos++;
                    }
                }
                /*
                 * adds to the totalCombos the ChosenCombos size
                 */
                    int totalCombos = ChosenCombos;

                    if (!OverLappingCombos.isEmpty())
                    {
                        /*
                         * calls the recursive method eventually to find the maximum count of non overlapping combinations
                         * and then adds to the total combos
                         */
                        totalCombos += findMaxCombosInOverlappingCombos(OverLappingCombos);
                    }

                /*
                 * for winner tracking purpose saves also how many combos have been achieved for the current goal
                 */
                getScoreBoard().addReachedPointsCount(Util.getKeyByValue(getPlayerZones(), playerField), totalCombos);
                    points += totalCombos * commonGoal.getPoints();
                }


            }
            return points;

        }


    /**
     * CountGoalPoints counts the goal points of a player given the PlayerField
     * note: this method is actually analog to the countCommonGoalPoints one, but for has been separated for clarity purpose
     * @param Player
     * @return  the counted Goal Points
     */
    public int countGoalPoints(PlayerField Player) {
        int points = countCommonGoalPoints(Player);

        GoalCard privateGoal = Player.getPrivateGoal();
        if (privateGoal.isResourceGoal()) {/*case the goal is Resource type */
            /*
             * briefly checks with the support of the ResourceMap how many times the given goal can be satisfied
             * with the current PlayerField
             */
            ResourceGoalCard ResourceGoal = (ResourceGoalCard) privateGoal;
            int min = Integer.MAX_VALUE;
            HashMap<Resource, Float> resourcesNeeded = new HashMap<>();
            /*
             * puts in a Map the needed Resources with their corresponding count
             */
            for (Resource resource : ResourceGoal.getRequirements()) {
                resourcesNeeded.put(resource, resourcesNeeded.getOrDefault(resource, 0f) + 1);
            }
            /*
             * calculates the minimum occurrences of all the needed resources
             */
            for (Resource resource : resourcesNeeded.keySet()) {
                float num = Player.getResourceFromMap(resource);
                min = Math.min((int) Math.floor(num / resourcesNeeded.get(resource)), min);
            }
            points += privateGoal.getPoints() * min; //points times minimum occurrences of that goal
            getScoreBoard().addReachedPointsCount(Util.getKeyByValue(getPlayerZones(),Player),min);

        } else { /* case the goal is Positional type */


            PositionGoalCard positionalGoal = (PositionGoalCard) privateGoal;
            ArrayList<Direction> Directions = positionalGoal.getPositionsFromBase();
            /*Will contain all the possibles card
             combination that respects the goal*/
            ArrayList<ArrayList<GameCard>> PossibleCombos = new ArrayList<ArrayList<GameCard>>();
            int ChosenCombos = 0;
            ArrayList<ArrayList<GameCard>> OverLappingCombos = new ArrayList<ArrayList<GameCard>>();

            /*For each card in the Game Zone will check if (with the near cards) it satisfies the goal
             * note: will also consider all the overlapping ones, since the rules indicates that for a single goal
             *       can consider a card only one time will use a method to get the maximum number of combinations
             *       that considers all the found cards maximum once
             * */
            for (GameCard currentCard : Player.getGameZone().values()) {
                GameCard currentPointer = currentCard;
                /*
                 * if it's not the same kingdom then we can pass to the next card and discard the current one as base
                 */
                if (positionalGoal.getResourceFromBase().getFirst() != currentCard.getKingdom())
                    continue;
                ArrayList<GameCard> possibleCards = new ArrayList<>();
                possibleCards.add(currentCard);
                /*
                 * for all the directions in the goal will check if the current combination (with currentCard as base)
                 * can be matched in the Game Zone
                 */
                for (int j = 0; j < Directions.size(); j++)  //also have to do this for each card that has not been used for the goal
                {


                    Direction currentDir = Directions.get(j);
                    Kingdom currentResource = positionalGoal.getResourceFromBase().get(j + 1);
                    /*
                     * get respectively to the current pointer (the current card that is satisfying the goal)
                     * the next card in the game zone that matches the goal request
                     */
                    if (currentDir == Direction.TOP) {
                        currentPointer = Player.getUp(currentPointer);


                    } else if (currentDir == Direction.TOP_LEFT) {
                        currentPointer = Player.getUpLeft(currentPointer);


                    } else if (currentDir == Direction.TOP_RIGHT) {
                        currentPointer = Player.getUpRight(currentPointer);


                    }
                    /*
                     * if the card is found, and it has the requested kingdom, then it can be added to the
                     * possible Cards for the combination
                     */
                    if (currentPointer == null || currentPointer.getKingdom() != currentResource)
                        break;
                    possibleCards.add(currentPointer);
                    /*
                     * on the last goal request, if it didn't break on any check, the possibleCards will be considered
                     * then as a possible Combo, then it can be checked if it overlaps with any other combination
                     */
                    if (j == Directions.size() - 1) {

                        PossibleCombos.add(possibleCards);
                    }

                }
            }
                /*
                 * for every found combo checks if any other combo is overlapping with it
                 */
                for (ArrayList<GameCard> Combo : PossibleCombos) {
                    /*
                     * for each card checks all the combos, could also do the other way, but preferred as this
                     */
                    for (GameCard card : Combo) {

                        for (ArrayList<GameCard> Combo1 : PossibleCombos) {
                            /*
                             * cannot be checking the same combo, as clearly it would be tagged as overlapping with itself
                             */
                            if (Combo1.containsAll(Combo) && Combo1.size()==Combo.size())
                                continue;
                            /*if a "combo1" contains any card of the given combo then will add, if not present, combo1 and
                             * combo to the overlapping ArrayList
                             */
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
                    /*
                     * finally make the check that if the combo is not in the overlapping ones it can be counted as a
                     * valid combo
                     */
                    if (!OverLappingCombos.contains(Combo)) {

                        ChosenCombos++;

                    }


                }

            /*
             * adds to the totalCombos the ChosenCombos size
             */
            int totalCombos=ChosenCombos;
            if(!OverLappingCombos.isEmpty()) {
                /*
                 * calls the recursive method eventually to find the maximum count of non overlapping combinations
                 * and then adds to the total combos
                 */
                totalCombos += findMaxCombosInOverlappingCombos(OverLappingCombos) ;

            }
            points += totalCombos * privateGoal.getPoints();
            /*
             * for winner tracking purpose saves also how many combos have been achieved for the current goal
             */
            getScoreBoard().addReachedPointsCount(Util.getKeyByValue(getPlayerZones(),Player),totalCombos);
        }
        return points;
    }
}
