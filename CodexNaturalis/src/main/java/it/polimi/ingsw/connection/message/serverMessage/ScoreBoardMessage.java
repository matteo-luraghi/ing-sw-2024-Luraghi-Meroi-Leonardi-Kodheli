package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.ScoreBoard;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

/**
 * ScoreBoardMessage class
 * used to show the scoreboard during the final phase of the game
 */
public class ScoreBoardMessage extends ServerMessage{
    @Serial
    private static final long serialVersionUID = 5036041983760800663L;
    private final ScoreBoard scoreBoard;

    /**
     * Constructor
     * @param scoreBoard scoreboard to show
     */
    public ScoreBoardMessage(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
    }

    /**
     * Show the scoreboard via CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowScoreBoard(this.scoreBoard);
    }
}
