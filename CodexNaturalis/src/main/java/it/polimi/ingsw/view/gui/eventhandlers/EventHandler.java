package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.view.gui.GUI;
import javafx.scene.control.Alert;

/**
 * Abstact class that gets extended by all the event handlers for the GUI
 * Useful to store common methods for all event handlers
 * @author Gabriel Leonardi
 */
public abstract class EventHandler {
    protected GUI view;

    /**
     * View setter
     * @param view the view we want to set
     */
    public void setView(GUI view){
        this.view = view;
    }

    /**
     * Shows a disconnection message and return when the user clicks okay
     */
    public void disconnection() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Disconnection");
        alert.setHeaderText("There was a problem with yours or someone's connection");
        alert.setContentText("You will be kicked out now, bye!");
        alert.showAndWait();
    }
    /**
     * Shows an information popup
     */
    public void showPopup(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
