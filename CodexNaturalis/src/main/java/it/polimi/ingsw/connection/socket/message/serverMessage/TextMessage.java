package it.polimi.ingsw.connection.socket.message.serverMessage;

import it.polimi.ingsw.view.mainview.View;

import java.io.Serial;

/**
 * TextMessage class
 * used to send text messages to clients
 * @author Matteo Leonardo Luraghi
 */
public class TextMessage extends ServerMessage {
    @Serial
    private static final long serialVersionUID = -8177785563760959331L;
    private final String message;

    /**
     * Constructor
     * @param message the message to display
     */
    public TextMessage(String message) {
        this.message = message;
    }

    /**
     * Show the message in the CLI or GUI
     * @param view the view interface
     */
    @Override
    public void show(View view) {
        view.showMessage(this.message);
    }
}
