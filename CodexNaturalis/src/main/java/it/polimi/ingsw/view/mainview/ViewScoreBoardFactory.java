package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.gamelogic.ScoreBoard;

import java.io.Serial;
import java.io.Serializable;

/**
 * viewScoreBoardFactory abstract class
 * @author Lorenzo Meroi
 */
public abstract class ViewScoreBoardFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = -6180496716791423076L;
    protected ScoreBoard scoreBoard;

    public void set(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }
    /**
     * abstract method to show the scoreboard
     */
    public abstract void show();
}
