package it.polimi.ingsw.model.gamelogic.gamechat;

import it.polimi.ingsw.model.gamelogic.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * gamechat class
 * @author Francesk Kodheli
 */
public class GameChat implements Serializable {
    @Serial
    private static final long serialVersionUID = -3923695197721707905L;
    private ArrayList<Message> messages;


    /**
     * public GameChat constructor
     */
    public GameChat()
    {
        messages=new ArrayList<>();
    }

    /**
     * Full log messages getter: Prevent undesired accesses to the original message objects
     * @return the copy of the messages arraylist
     */
    public ArrayList<Message> getMessages() {
        ArrayList<Message> copyMessages=new ArrayList<>();
        {
            for(Message message:messages)
            {
                copyMessages.add(new Message(message));
            }
        }
        return copyMessages;
    }

    /**
     * get the last message copy saved
     * @return Message
     */
    public Message getLastMessage() //useful for gui interactions since they are rendered immediately
    {
        return new Message(messages.getLast());
    }

    /**
     * saves a new message in the arraylist
     * @param message message to save
     */
    public void saveMessage(Message message)
    {
        messages.add(message);
    }

    /**
     * method to filter the chat for a given player
     * @param player to filter the chat for
     * @return the filtered gamechat
     */
    public GameChat filterChat (Player player) {
        GameChat filteredChat = new GameChat();

        // only the author and the receiver of the message can see it
        for (Message m : messages) {
            if (m.getRecipient().equalsIgnoreCase("all") ||
                    m.getRecipient().equalsIgnoreCase(player.getNickname()) ||
                    m.getAuthor().getNickname().equalsIgnoreCase(player.getNickname())) {
                filteredChat.saveMessage(m);
            }
        }

        return filteredChat;
    }

    /**
     * useful in CLI, shows 10 messages starting from a selected messagePage
     * @param messagePage if it is 0 it shows the last 10 messages, otherwise it shows the corresponding message page
     * @return the message page requested
     */
    public ArrayList<Message> messagesToShow (int messagePage) {

        if (this.messages.size() <= 10) {
            return (ArrayList<Message>) this.messages.clone();
        }

        int index = this.messages.size() - 1 - (messagePage*10);

        ArrayList<Message> toShow = new ArrayList<>();

        if (messagePage == this.messages.size()/10) {
            index = 0;
            for (int i=0; i<10; i++) {
                toShow.add(this.messages.get(index+i));
            }
        }
        else {
            for (int i = 9; i >= 0; i--) {
                toShow.add(this.messages.get(index - i));
            }
        }

        return toShow;
    }
}
