package it.polimi.ingsw.view.gui.eventhandlers;

import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Platform;
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
        alert.setTitle("");
        alert.setHeaderText("");
        alert.setContentText("There was a problem with yours or someone's connection: you will be kicked out now, bye!");
        alert.getDialogPane().setStyle(" -fx-background-color: #ede3ba;" +
                "-fx-font-family: Cambria;" +
                "-fx-font-style: italic;" +
                "-fx-font-size: large;" +
                "-fx-font-weight: bold;");
        alert.showAndWait();
        try{
            view.getClient().disconnect();
        } catch (NullPointerException ignored){}
        Platform.exit();
    }
    /**
     * Shows an information popup
     */
    public void showPopup(String text){
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setContentText(text);
        alert.getDialogPane().setStyle(" -fx-background-color: #ede3ba;" +
                "-fx-font-family: Cambria;" +
                "-fx-font-style: italic;" +
                "-fx-font-size: large;" +
                "-fx-font-weight: bold;");
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
