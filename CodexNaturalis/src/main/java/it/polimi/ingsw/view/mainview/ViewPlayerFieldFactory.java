package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.model.gamelogic.PlayerField;
import it.polimi.ingsw.view.cli.ViewGameCardCLIFactory;
import it.polimi.ingsw.view.cli.ViewGoalCardCLIFactory;

import java.io.Serial;
import java.io.Serializable;

/**
 * viewPlayerFieldFactory abstract class
 * @author Lorenzo Meroi
 */
public abstract class ViewPlayerFieldFactory implements Serializable {

    @Serial
    private static final long serialVersionUID = 1780416040676399725L;
    protected PlayerField playerField;

    protected Player player;

    protected ViewGameCardFactory gameCardViewer;

    protected ViewGoalCardFactory goalCardViewer;


    /**
     * abstract method to display the player field to it's owner
     */
    public abstract void showComplete();

    /**
     * abstract method to display the player field to those who do not own it
     */
    public abstract void showToOthers();

    /**
     * method to set which player field to show
     * @param playerField is the player field you want to be shown
     */
    public void setPlayerField(PlayerField playerField, Player player) {
        this.playerField = playerField;
        this.player = player;
    }
}
