package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.GameCard;
import it.polimi.ingsw.model.gamelogic.Coordinates;
import it.polimi.ingsw.view.mainview.ViewPlayerFieldFactory;

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

        for (Coordinates c : sortedCoordinates) {
            if (c.getY() < southernMost) {
                southernMost = c.getY();
            }
            if (c.getX() > easternMost) {
                easternMost = c.getX();
            }
        }


        int k = 0;
        for (int i=0; i<easternMost; i++) {
            for (int j=0; j<southernMost; j++) {
                if (sortedCoordinates.get(k).getX() == i && sortedCoordinates.get(k).getY() == j) {
                    System.out.print(CardToString(sortedCoordinates.get(j),this.playerField.getGameZone().get(sortedCoordinates.get(j))));
                    k++;
                } else {
                    System.out.print("         ");
                }
            }
            System.out.println();
        }
        //TODO print resourcemap
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
