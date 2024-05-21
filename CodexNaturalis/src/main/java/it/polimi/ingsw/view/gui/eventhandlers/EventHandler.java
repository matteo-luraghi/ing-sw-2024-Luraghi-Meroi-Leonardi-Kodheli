package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.view.gui.GUI;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;

/**
 * Abstact class that gets extended by all the event handlers for the GUI
 * Useful to store common methods for all event handlers
 * @author Gabriel Leonardi
 */
public abstract class EventHandler {
    protected GUI view;
    private Alert alert = null;

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
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.setContentText(text);
        alert.showAndWait();
        alert = null;
    }

    /**
     * If there is an active alert, close it
     */
    public void closePopup(){
        if(alert != null){
            alert.close();
        }
    }

    /**
     * Gets a child from the given object that has the specified id
     * @param parent the parent that contains the children
     * @param id the id of the specified client (Ignoring case)
     * @return the client object if it's contained in the parent, null if it's not present
     */
    protected Node getChildrenFromID(Pane parent, String id) {
        for(Node o: parent.getChildren()){
            if(o.getId() != null && o.getId().equalsIgnoreCase(id)){
                return o;
            }
        }
        return null;
    }

}
