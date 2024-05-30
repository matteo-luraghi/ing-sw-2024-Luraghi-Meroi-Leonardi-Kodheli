package it.polimi.ingsw.model.gamelogic.gamechat;

import java.util.ArrayList;
import java.util.HashMap;

public class GameChat {

    private ArrayList<Message> messages;



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


}
