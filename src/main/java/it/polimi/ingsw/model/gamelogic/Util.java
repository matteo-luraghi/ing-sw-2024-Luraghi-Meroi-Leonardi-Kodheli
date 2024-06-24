package it.polimi.ingsw.model.gamelogic;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.view.gui.GUI;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * Class for multipurpose methods used by other classes
 */
public class Util {
    /**
     * template method for retrieving the key given the value, supposing 1to1 mapping
     * @param map map value
     * @param value requested value
     * @return KeyValue if present, otherwise null
     * @param <T> Key Data Type
     * @param <E> Value Data Type
     */
    public static <T, E> T getKeyByValue(Map<T, E> map, E value) { //one to one mapping if present
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * Function that return the color equals to the string given
     * @param s the string we want to turn into a color
     * @return the color indicated by the string
     * @throws NullPointerException when s is not equal to one of the colors
     */
    public static Color stringToColor(String s) throws NullPointerException{
        s = s.toUpperCase();
        if(s.equals("RED")) return Color.RED;
        if(s.equals("BLUE")) return Color.BLUE;
        if(s.equals("YELLOW")) return Color.YELLOW;
        if(s.equals("GREEN")) return Color.GREEN;

        throw new NullPointerException();
    }

    /**
     * from string to Kingdom enum
     * @param string input string
     * @return Kingdom enum
     */
    public static Kingdom fromStringToKingdom(String string)
    {
        switch(string)
        {
            case "INSECT" -> {return Kingdom.INSECT;}
            case "PLANT" -> {return Kingdom.PLANT;}
            case "FUNGI" -> {return Kingdom.FUNGI;}
            case "ANIMAL" -> {return Kingdom.ANIMAL;}
            case "STARTING" -> {return Kingdom.STARTING;}
            default -> {return Kingdom.UNKNOWN;}
        }
    }

    /**
     * from string to Resource enum
     * @param string input string
     * @return Resource enum
     */
    public static Resource fromStringToResource(String string)
    {
        switch(string)
        {
            case "ANIMAL" -> {return Resource.ANIMAL;}
            case "BLANK" -> {return Resource.BLANK;}
            case "FUNGI" -> {return Resource.FUNGI;}
            case "PLANT" -> {return Resource.PLANT;}
            case "HIDDEN" -> {return Resource.HIDDEN;}
            case "INSECT" -> {return Resource.INSECT;}
            case "POTION" -> {return Resource.POTION;}
            case "SCROLL" -> {return Resource.SCROLL;}
            case "COVERED" -> {return Resource.COVERED;}
            case "FEATHER" -> {return Resource.FEATHER;}

            default -> {return Resource.UNKNOWN;}
        }
    }

    /**
     * from string to PointCondition enum
     * @param string input string
     * @return PointCondition enum
     */
    public static PointCondition fromStringToPointCondition(String string)
    {
        switch(string)
        {
            case "NORMAL" -> {return PointCondition.NORMAL;}
            case "CORNER" -> {return PointCondition.CORNER;}
            case "FEATHER" -> {return PointCondition.FEATHER;}
            case "POTION" -> {return PointCondition.POTION;}
            case "SCROLL" -> {return PointCondition.SCROLL;}


            default -> {return PointCondition.UNKNOWN;}
        }
    }

    /**
     * from string to direction enum
     * @param string input string
     * @return direction enum
     */
    public static Direction fromStringToDirection(String string)
    {
        switch(string)
        {
            case "TOP_RIGHT" -> {return Direction.TOP_RIGHT;}
            case "TOP_LEFT" -> {return Direction.TOP_LEFT;}
            case "TOP" -> {return Direction.TOP;}


            default -> {return Direction.UNKNOWN;}
        }
    }
    /**
     * JSON parser for Resource Cards
     * @param Json Json object
     * @return ResourceCard
     */
    public static ResourceCard fromJSONtoResourceCard(JsonObject Json)
    {
        Kingdom kingdom=Util.fromStringToKingdom(Json.get("kingdom").getAsString());
        boolean isFront=Json.get("isFront").getAsBoolean();
        JsonArray cornersArray =Json.getAsJsonArray("corners");
        Resource[] corners=new Resource[4];
        int n=0;
        for(JsonElement o: cornersArray)
        {
            corners[n]=Util.fromStringToResource(o.getAsString()); //to check
            n++;
        }
        int points=Json.get("points").getAsInt();
        boolean isGoldJ=Json.get("isGold").getAsBoolean();
        int id=Json.get("id").getAsInt();
        return new ResourceCard(kingdom,isFront,corners, points,isGoldJ, id);

    }
    /**
     * JSON parser for Gold Cards
     * @param Json Json object
     * @return GoldCard
     */
    public static GoldCard fromJSONtoGoldCard(JsonObject Json)
    {
        Kingdom kingdom=Util.fromStringToKingdom(Json.get("kingdom").getAsString());
        boolean isFront=Json.get("isFront").getAsBoolean();
        JsonArray cornersArray =Json.getAsJsonArray("corners");
        Resource[] corners=new Resource[4];
        int n=0;
        for(JsonElement o: cornersArray)
        {
            corners[n]=Util.fromStringToResource(o.getAsString()); //to check
            n++;
        }
        int points=Json.get("points").getAsInt();



        PointCondition pointCondition=fromStringToPointCondition(Json.get("pointCondition").getAsString());
        ArrayList<Resource> playableCondition= new ArrayList<Resource>();

        JsonArray playableConditionArray=Json.getAsJsonArray("playableCondition");

        for(JsonElement o: playableConditionArray)
        {
            playableCondition.add(fromStringToResource(o.getAsString()));

        }
        int id=Json.get("id").getAsInt();
        return new GoldCard(kingdom,isFront,corners, points,pointCondition,playableCondition, id);

    }

    /**
     * JSON parser for Starting Cards
     * @param Json Json object
     * @return StartingCard
     */
    public static StartingCard fromJSONtoStartingCard(JsonObject Json)
    {
        Kingdom kingdom=Util.fromStringToKingdom(Json.get("kingdom").getAsString());
        boolean isFront=Json.get("isFront").getAsBoolean();
        JsonArray cornersArray =Json.getAsJsonArray("corners");
        Resource[] corners=new Resource[cornersArray.size()];
        int n=0;
        for(JsonElement o: cornersArray)
        {

            corners[n]=Util.fromStringToResource(o.getAsString()); //to check
            n++;
        }

        JsonArray backCornersArray =Json.getAsJsonArray("backCorners");
        Resource[] backCorners=new Resource[backCornersArray.size()];
        n=0;
        for(JsonElement o: backCornersArray)
        {

            backCorners[n]=Util.fromStringToResource(o.getAsString()); //to check
            n++;
        }
        JsonArray permanentResourcesArray =Json.getAsJsonArray("permanentResources");
        Resource[] permanentResources=new Resource[permanentResourcesArray.size()];

        n=0;
        for(JsonElement o: permanentResourcesArray)
        {
            permanentResources[n]=Util.fromStringToResource(o.getAsString()); //to check
            n++;
        }

        int id=Json.get("id").getAsInt();

        return new StartingCard(kingdom,isFront,corners,backCorners,permanentResources, id);

    }

    /**
     * JSON parser for Resource Goal Cards
     * @param Json Json object
     * @return ResourceGoalCard
     */
    public static ResourceGoalCard fromJSONtoResourceGoalCard(JsonObject Json)
    {

        int points=Json.get("points").getAsInt();

        JsonArray requirementsArray =Json.getAsJsonArray("requirements");
        ArrayList<Resource> requirements=new  ArrayList<Resource>();

        for(JsonElement o: requirementsArray)
        {
            requirements.add(Util.fromStringToResource(o.getAsString()));

        }
        int id=Json.get("id").getAsInt();
        return new ResourceGoalCard(points,requirements, id);

    }

    /**
     * JSON parser for Positional Goal Cards
     * @param Json Json object
     * @return PositionGoalCard
     */
    public static PositionGoalCard fromJSONtoPositionGoalCard(JsonObject Json)
    {

        int points=Json.get("points").getAsInt();

        JsonArray resourceFromBase =Json.getAsJsonArray("resourceFromBase");
        ArrayList<Kingdom> requirements=new  ArrayList<Kingdom>();

        for(JsonElement o: resourceFromBase)
        {
            requirements.add(Util.fromStringToKingdom(o.getAsString()));
        }
        JsonArray directionArray =Json.getAsJsonArray("positionsFromBase");
        ArrayList<Direction> direction=new  ArrayList<Direction>();

        for(JsonElement o: directionArray)
        {
            direction.add(Util.fromStringToDirection(o.getAsString()));
        }
        int id=Json.get("id").getAsInt();
        return new PositionGoalCard(points,direction,requirements, id);

    }

    /**
     * Utility method to check if a card is contained in a list of cards
     * @param cards The list of cards
     * @param cardToCheck The card that needs to be checked
     * @return the card obj the card is found, null otherwise
     */
    public static ResourceCard checkIfResourceCardIsPresent(ArrayList<ResourceCard> cards, ResourceCard cardToCheck){
        for(ResourceCard current : cards){
            ResourceCard correct = current;
            if(current.getKingdom() != cardToCheck.getKingdom() || current.getIsGold() != cardToCheck.getIsGold() || current.getPoints() != cardToCheck.getPoints())
                correct = null;
            for(int i = 0; i < 4  && correct!=null; i++){
                if(current.getCorner(i) != cardToCheck.getCorner(i))
                    correct = null;
            }
            if(correct!=null && cardToCheck.getIsGold()){
                GoldCard c1, c2;
                c1 = (GoldCard) cardToCheck;
                c2 = (GoldCard) current;
                if(c1.getPointCondition() != c2.getPointCondition() || c1.getPlayableCondition().size() != c2.getPlayableCondition().size())
                    correct = null;

                //Playable conditions have the same size
                for(int i = 0; i < c1.getPlayableCondition().size() && correct!=null; i++){
                    if(c1.getPlayableCondition().get(i) != c2.getPlayableCondition().get(i))
                        correct = null;
                }
            }
            if (correct!=null) return correct;
        }
        return null;
    }

    /**
     * Given the id of a game card, return the path to open the image
     * @param id the id of the game card
     * @param isFront if it's facing the front
     * @return The path of the image
     */
    static public String getImageFromID(int id, boolean isFront){
        String cardName = id + ".png";
        //Add leading 0s to cardName
        if(id < 100){
            cardName = "0" + cardName;
        }
        if(id < 10){
            cardName = "0" + cardName;
        }

        if(isFront){
            return GUI.class.getResource("assets/CODEX_cards_gold_front/" + cardName).toString();
        }else{
            return GUI.class.getResource("assets/CODEX_cards_gold_back/" + cardName).toString();
        }
    }

    /**
     * Given the id of a tutorial image, return the path to open the image
     * @param id the id of the tutorial image
     * @return The path of the image
     */
    public static String getTutorialImageByID(int id) {
        String imageName = id + ".png";
        return GUI.class.getResource("assets/tutorial/"+imageName).toString();
    }
}
