package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.ViewScoreBoardFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ViewScoreBoardCLIFactory extends ViewScoreBoardFactory {
    /**
     * abstract method to show the scoreboard
     */
    @Override
    public void show() {
        Set<Player> playerSet = this.scoreBoard.getBoard().keySet();
        List<Player> playerList = playerSet.stream()
                .parallel()
                .sorted((Player p1, Player p2) -> (this.scoreBoard.getPoints(p1)>=this.scoreBoard.getPoints(p2)) ? 1 : -1)
                .toList();
        int i = 1;
        for (Player p : playerList) {
            System.out.println("#"+i+" "+p.getNickname() + ": " + this.scoreBoard.getPoints(p));
            System.out.println();
            i++;
        }
    }
}
