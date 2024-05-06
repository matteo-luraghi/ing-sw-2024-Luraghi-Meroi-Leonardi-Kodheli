package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.gamelogic.Coordinates;
import it.polimi.ingsw.view.mainview.ViewPlayerFieldFactory;
import it.polimi.ingsw.view.mainview.AnsiColors;

import java.util.ArrayList;
import java.util.List;

/**
 * ViewPlayerFieldCLIFactory class
 * @author Lorenzo Meroi
 */
public class ViewPlayerFieldCLIFactory extends ViewPlayerFieldFactory {

    public ViewPlayerFieldCLIFactory() {
        this.gameCardViewer = new ViewGameCardCLIFactory();
        this.goalCardViewer = new ViewGoalCardCLIFactory();
    }

    /**
     * abstract method to show the player field to its owner
     */
    @Override
    public void showComplete() {
        /*
                 [-1,+1:P]
        [-2,+0:A]         [+0,+0:S]
                 [-1,-1:A]         [+1,-1:I]

        Resource Map:
        - Plant: #N°
        ...

        Player's hand:

        Player's private goal:
        */

        ShowGameZone();

        System.out.println();
        System.out.println(this.player.toString()+"'s hand:");
        /*int counter = 1;
        for (ResourceCard c : this.playerField.getHand()) {
            gameCardViewer.SetCard(c);
            System.out.println("Card N°" + counter);
            counter++;
            gameCardViewer.Show();
            System.out.println();
        }*/

        ((ViewGameCardCLIFactory) this.gameCardViewer).printThreeCards(this.playerField.getHand().get(0), this.playerField.getHand().get(1), this.playerField.getHand().get(2), true);

        System.out.println(this.player.toString()+"'s private goal:");
        goalCardViewer.SetCard(this.playerField.getPrivateGoal());
        goalCardViewer.Show();
    }

    /**
     * show the player field to those who are not its owner
     */
    @Override
    public void showToOthers() {
        /*
                 [-1,+1:P]
        [-2,+0:A]         [+0,+0:S]
                 [-1,-1:A]         [+1,-1:I]

        Resource Map:
        - Plant: #N°
        ...
        */

        ShowGameZone();
    }

    /**
     * displays a player's game zone
     */
    private void ShowGameZone() {
        System.out.println(this.player.toString()+"'s field:");

        List<Coordinates> sortedCoordinates = new ArrayList<>(this.playerField.getGameZone().keySet());
        sortedCoordinates = sortedCoordinates.stream()
                .sorted((Coordinates c1, Coordinates c2) -> {
                    if (c1.getY() > c2.getY()) {
                        return -1;
                    } else if (c1.getY() < c2.getY()) {
                        return 1;
                    } else if (c1.getX() < c2.getX()) {
                        return -1;
                    } else if (c1.getX() > c2.getX()) {
                        return 1;
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
                if (k>=sortedCoordinates.size()) {
                    break;
                }
                if (sortedCoordinates.get(k).getX() == j && sortedCoordinates.get(k).getY() == i) {
                    if (this.playerField.getGameZone().get((sortedCoordinates.get(k))) instanceof GoldCard) {
                        System.out.print(CardToString(sortedCoordinates.get(k), (GoldCard) this.playerField.getGameZone().get(sortedCoordinates.get(k))));
                    } else {
                        System.out.print(CardToString(sortedCoordinates.get(k), this.playerField.getGameZone().get(sortedCoordinates.get(k))));
                    }
                    k++;
                } else {
                    System.out.print("         ");
                }
            }
            System.out.println();

        }

        System.out.println(this.player.toString()+"'s Resources:");
        for (Resource r : this.playerField.getResourceMap().keySet()) {
            if (!r.equals(Resource.UNKNOWN) && !r.equals(Resource.COVERED) && !r.equals(Resource.HIDDEN) && !r.equals(Resource.BLANK))
                System.out.println("- " + r.toStringExt() + ": #" + this.playerField.getResourceFromMap(r)+AnsiColors.ANSI_RESET);
        }
    }

    /**
     * displays a card in the player field
     * @param coor refers to the position in which the card sits
     * @param card refers to the card to display
     * @return a string representing the card
     */
    private String CardToString (Coordinates coor, GameCard card) {
        String cardString = "";
        if (card instanceof GoldCard)
            cardString += AnsiColors.ANSI_YELLOW;
        else
            cardString += AnsiColors.ANSI_WHITE;

        cardString = "[";

        if (coor.getX()>=0)
            cardString += "+";

        cardString += coor.getX() + ",";

        if (coor.getY()>=0)
            cardString += "+";

        cardString +=coor.getY() + ":" + card.getKingdom().toString();

        if (card instanceof GoldCard)
            cardString += AnsiColors.ANSI_YELLOW;
        else
            cardString += AnsiColors.ANSI_WHITE;

        cardString += "]";

        cardString += AnsiColors.ANSI_RESET;


        return cardString;
    }
}
