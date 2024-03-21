package it.polimi.ingsw.card;

import java.util.ArrayList;

/**
 * GoldCard Class
 * @author Matteo Leonardo Luraghi
 */
public class GoldCard extends ResourceCard{
    private final PointCondition pointCondition;
    private final ArrayList<Resource> playableCondition;

    /**
     * Constructor
     * @param kingdom type of the card
     * @param isFront which side of the card is currently visible
     * @param corners content of the front corners of the card
     * @param points points assigned when the card is played
     * @param isGold true if the card is gold
     * @param pointCondition condition to get the points when the card is played
     * @param playableCondition necessary condition to play the card
     */
    public GoldCard(Kingdom kingdom, boolean isFront, Resource[] corners, int points, boolean isGold,
                    PointCondition pointCondition, ArrayList<Resource> playableCondition) {
        super(kingdom, isFront, corners, points, isGold);
        this.pointCondition = pointCondition;
        this.playableCondition = (ArrayList<Resource>) playableCondition.clone();

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
