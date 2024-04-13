package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.PlayerField;

/**
 * viewPlayerFieldFactory abstract class
 * @author Lorenzo Meroi
 */
public abstract class ViewPlayerFieldFactory {

    protected PlayerField playerField;

    /**
     * abstract method to show the player field
     */
    public abstract void show();

    /**
     * method to set which player field to show
     * @param playerField is the player field you want to be shown
     */
    public void setPlayerField(PlayerField playerField) {
        this.playerField = playerField;
    }
}
