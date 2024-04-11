package it.polimi.ingsw.model.gamelogic;

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
        //TODO: make custom InvalidColorException
        s = s.toLowerCase();
        if(s.equals("RED")) return Color.RED;
        if(s.equals("BLUE")) return Color.BLUE;
        if(s.equals("YELLOW")) return Color.YELLOW;
        if(s.equals("GREEN")) return Color.GREEN;

        throw new NullPointerException();
    }
}
