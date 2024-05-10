package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.model.gamelogic.Color;
import it.polimi.ingsw.view.mainview.View;

import java.util.ArrayList;

/**
 * ColorRequest class
 * used to ask the player for a color
 * @author Matteo Leonardo Luraghi
 */
public class ColorRequest extends ServerMessage {
    private static final long serialVersionUID = 7874353162024038116L;
    private final ArrayList<Color> colors;

    /**
     * Constructor
     */
    public ColorRequest(ArrayList<Color> colors) {
        this.colors = colors;
    }

    /**
     * Show the available colors via TUI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.insertColor(colors);
    }
}
