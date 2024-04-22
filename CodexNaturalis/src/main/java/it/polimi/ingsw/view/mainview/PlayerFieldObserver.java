package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.gamelogic.PlayerField;

/**
 * PlayerFieldObserver class
 * @author LorenzoMeroi
 */
public class PlayerFieldObserver {

    private ViewPlayerFieldFactory playerFieldViewer;

    /**
     * PlayerFieldObserver constructor
     * @param playerFieldViewer is the viewer you want to update on any change
     */
    public PlayerFieldObserver (ViewPlayerFieldFactory playerFieldViewer) {
        this.playerFieldViewer = playerFieldViewer;
    }

    /**
     * method to update the viewer on a change
     * @param playerField is the playerField that has changed
     */
    public void Update(PlayerField playerField) {
        this.playerFieldViewer.setPlayerField(playerField);
    }
}
