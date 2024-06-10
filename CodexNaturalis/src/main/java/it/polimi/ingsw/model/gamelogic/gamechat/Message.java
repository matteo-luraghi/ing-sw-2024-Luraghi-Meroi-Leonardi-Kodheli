package it.polimi.ingsw.model.gamelogic.gamechat;

import it.polimi.ingsw.model.gamelogic.Player;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * message class
 * @author Francesk Kodheli
 */
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = -6160152938762690523L;
    private final String message;
    private final Player author;
    private final Date date;

    private final String recipient; //can be a player's nickname or "all" to send the message to everyone

    /**
     * Default constructor for message, date is initialized with the class
     * @param message String message
     * @param author Author of the message
     */
    public Message(String message, Player author, String recipient)
    {
        this.message=message;
        this.author=author;
        this.recipient = recipient;
        // initialize the date to now
        date=new Date();
    }

    /**
     * Constructor for already initialized message
     * @param message message class to copy
     */
    public Message(Message message)
    {
        this.message= message.getMessage();
        this.recipient = message.getRecipient();
        this.author= message.getAuthor();
        this.date=message.getDate();
    }


    /**
     * message copy Getter
     * @return string message
     */
    public String getMessage()
    {
        return new String(message); //prevent modifications to the message
    }

    /**
     * author getter
     * @return Player that sent the message
     */
    public Player getAuthor()
    {
        return author;
    }

    /**
     * recipient getter
     * @return a player's name or "all" to send the message to everyone
     */
    public String getRecipient () {
        return this.recipient;
    }

    /**
     * date getter
     * @return Date copy
     */
    public Date getDate()
    {
        return (Date) date.clone(); //prevent modifications to the date
    }

    /**
     * method to print the message
     * @return
     */
    public String toString () {
        String result = "";

        if (!this.recipient.equalsIgnoreCase("all")) {
            result += "(PRIVATE) ";
        }

        result += "(" + this.author + "): ";

        result += this.message;

        return result;
    }
}
