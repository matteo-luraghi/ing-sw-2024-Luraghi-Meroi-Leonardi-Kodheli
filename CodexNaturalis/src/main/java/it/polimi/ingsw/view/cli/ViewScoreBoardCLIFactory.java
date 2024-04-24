package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.AnsiColors;
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
            switch (p.getColor()) {
                case RED -> {System.out.print(AnsiColors.ANSI_RED);}
                case BLUE -> {System.out.print(AnsiColors.ANSI_BLUE);}
                case GREEN -> {System.out.print(AnsiColors.ANSI_GREEN);}
                case YELLOW -> {System.out.print(AnsiColors.ANSI_YELLOW);}
                default -> {System.out.print(AnsiColors.ANSI_RESET);}
            }
            System.out.println("#"+i+" "+p.getNickname() + ": " + this.scoreBoard.getPoints(p));
            System.out.println(AnsiColors.ANSI_RESET);
            i++;
        }
    }
}
