package it.polimi.ingsw.model.card;

import java.io.Serial;
import java.util.ArrayList;

/**
 * GoldCard Class
 * @author Matteo Leonardo Luraghi
 */
public class GoldCard extends ResourceCard{
    @Serial
    private static final long serialVersionUID = -190142351748065027L;
    private final PointCondition pointCondition;
    private final ArrayList<Resource> playableCondition;

    /**
     * Constructor
     * @param kingdom type of the card
     * @param isFront which side of the card is currently visible
     * @param corners content of the front corners of the card
     * @param points points assigned when the card is played
     * @param pointCondition condition to get the points when the card is played
     * @param playableCondition necessary condition to play the card
     */
    public GoldCard(Kingdom kingdom, boolean isFront, Resource[] corners, int points,
                    PointCondition pointCondition, ArrayList<Resource> playableCondition) {
        super(kingdom, isFront, corners, points, true);
        this.pointCondition = pointCondition;
        this.playableCondition = new ArrayList<>(playableCondition);
    }

    /**
     * PointCondition getter
     * @return condition to get the points when the card is played
     */
    public PointCondition getPointCondition(){
        return this.pointCondition;
    }

    /**
     * PlayableCondition getter
     * @return necessary condition to play the card
     */
    public ArrayList<Resource> getPlayableCondition() {
        return (ArrayList<Resource>) playableCondition.clone();
    }
}
