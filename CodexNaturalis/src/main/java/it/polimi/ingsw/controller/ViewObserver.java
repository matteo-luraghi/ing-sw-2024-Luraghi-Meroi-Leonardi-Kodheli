package it.polimi.ingsw.controller;

/**
 * View Observer, observes the view for game flow changes and updates the MegaController
 * @author Gabriel Leonardi
 */
public class ViewObserver {

    MegaController controller;
    public ViewObserver(MegaController controller){
        this.controller = controller;
    }
    public void publish(String message){
        System.err.println("ViewObserver not yet implemented, message sent: " + message);
    }

    public void decideNumberOfPlayers(){
        controller.giveNumberPlayers();
    }

    public void makePlayerLogin(){
        controller.setPlayer();
    }
}
