package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

public class ScoreBoardResponse extends ServerMessage {
    private static final long serialVersionUID = 3189795668287532809L;

    public ScoreBoardResponse() {
    }

    @Override
    public void show(View view) {
        view.ShowScoreBoard();
    }
}
