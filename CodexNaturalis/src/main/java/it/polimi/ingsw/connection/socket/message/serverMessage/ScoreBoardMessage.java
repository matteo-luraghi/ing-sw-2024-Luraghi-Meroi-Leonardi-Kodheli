package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.ScoreBoard;
import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;
import java.rmi.RemoteException;

/**
 * ScoreBoardMessage class
 * used to show the scoreboard during the final phase of the game
 * @author Matteo Leonardo Luraghi
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
     * Show the scoreboard
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        try {
            view.ShowScoreBoard(this.scoreBoard);
        } catch (RemoteException e) {
            System.err.println("Error showing scoreboard");
        }
    }
}
