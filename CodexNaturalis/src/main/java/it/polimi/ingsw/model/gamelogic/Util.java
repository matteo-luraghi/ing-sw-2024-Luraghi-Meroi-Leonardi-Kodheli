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
}
