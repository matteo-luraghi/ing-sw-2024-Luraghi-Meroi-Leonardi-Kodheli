package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

/**
 * ColorRequest class
 * used to ask the player for a color
 * @author Matteo Leonardo Luraghi
 */
public class ColorRequest extends ServerMessage {
    private static final long serialVersionUID = 7874353162024038116L;

    /**
     * Constructor, sets the message type as COLOR_REQUEST
     */
    public ColorRequest() {
        super(Message.COLOR_REQUEST);
    }

    /**
     * Show the available colors via TUI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.ShowChooseColor();
    }
}
