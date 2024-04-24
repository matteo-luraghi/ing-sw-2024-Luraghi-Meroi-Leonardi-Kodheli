package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.gamelogic.ScoreBoard;

/**
 * viewScoreBoardFactory abstract class
 * @author Lorenzo Meroi
 */
public abstract class ViewScoreBoardFactory {

    protected ScoreBoard scoreBoard;

    /**
     * abstract method to show the scoreboard
     */
    public abstract void show();
}