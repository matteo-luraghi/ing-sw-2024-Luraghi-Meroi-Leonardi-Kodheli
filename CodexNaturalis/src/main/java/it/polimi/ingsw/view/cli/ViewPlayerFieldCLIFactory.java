package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.GameCard;
import it.polimi.ingsw.model.card.Resource;
import it.polimi.ingsw.model.gamelogic.Coordinates;
import it.polimi.ingsw.view.mainview.ViewPlayerFieldFactory;
import it.polimi.ingsw.view.mainview.AnsiColors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ViewPlayerFieldCLIFactory extends ViewPlayerFieldFactory {
    /**
     * abstract method to show the player field
     */
    @Override
    public void show() {
        /*
                 [-1,+1:P]
        [-2,+0:A]         [+0,+0:S]
                 [-1,-1:A]         [+1,-1:I]

        Resource Map:
        - Plant: #N°
        */

        ArrayList<Coordinates> sortedCoordinates = new ArrayList<>(this.playerField.getGameZone().keySet());
        sortedCoordinates = (ArrayList<Coordinates>) sortedCoordinates.stream()
                .sorted((Coordinates c1, Coordinates c2) -> {
                    if (c1.getY() > c2.getY()) {
                        return 1;
                    } else if (c1.getY() < c2.getY()) {
                        return -1;
                    } else if (c1.getX() < c2.getX()) {
                        return 1;
                    } else if (c1.getX() > c2.getX()) {
                        return -1;
                    }
                    return 0;
                })
                .toList();

        int southernMost = Integer.MAX_VALUE;
        int easternMost = Integer.MIN_VALUE;
        int westernMost = Integer.MAX_VALUE;
        int northernMost = Integer.MIN_VALUE;

        for (Coordinates c : sortedCoordinates) {
            if (c.getY() < southernMost) {
                southernMost = c.getY();
            }
            if (c.getX() > easternMost) {
                easternMost = c.getX();
            }
            if (c.getX() < westernMost) {
                westernMost = c.getX();
            }
            if (c.getY() > northernMost) {
                northernMost = c.getY();
            }
        }


        int k = 0;
        for (int i=northernMost; i>=southernMost; i--) {
            for (int j=westernMost; j<=easternMost; j++) {
                if (sortedCoordinates.get(k).getX() == j && sortedCoordinates.get(k).getY() == i) {
                    System.out.print(CardToString(sortedCoordinates.get(k),this.playerField.getGameZone().get(sortedCoordinates.get(k))));
                    k++;
                } else {
                    System.out.print("         ");
                }
            }
            System.out.println();
        }

        System.out.println("Your resources:");
        for (Resource r : this.playerField.getResourceMap().keySet()) {
            System.out.println("- " + r.toString() + ": #" + this.playerField.getResourceFromMap(r));
        }
    }

    private String CardToString (Coordinates coor, GameCard card) {
        String finalString = "[";

        if (coor.getX()>=0)
            finalString += "+";

        finalString += coor.getX() + ",";

        if (coor.getY()>=0)
            finalString += "+";

        finalString +=coor.getY() + ":" + card.getKingdom().toString() + "]";

        return finalString;
    }
}
