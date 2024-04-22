package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.gamelogic.ScoreBoard;

/**
 * ScoreBoardObserver class
 * @author Lorenzo Meroi
 */
public class ScoreBoardObserver {

    private ViewScoreBoardFactory scoreBoardViewer;

    /**
     * ScoreBoardObserver constructor
     * @param scoreBoardViewer is the viewer you want to update on any change
     */
    public ScoreBoardObserver(ViewScoreBoardFactory scoreBoardViewer) {
        this.scoreBoardViewer = scoreBoardViewer;
    };

    /**
     * method to update the viewer on a change
     */
    public void Update () {}
}
