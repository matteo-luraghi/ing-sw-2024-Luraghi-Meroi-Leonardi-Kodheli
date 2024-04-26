package it.polimi.ingsw.connection.message.serverMessage;

import it.polimi.ingsw.connection.message.Message;
import it.polimi.ingsw.view.mainview.View;

public class TextMessage extends ServerMessage {
    private static final long serialVersionUID = -8177785563760959331L;
    private final String message;

    public TextMessage(String message) {
        super(Message.TEXT);
        this.message = message;
    }

    @Override
    public void show(View view) {
        view.showMessage(this.message);
    }
}
