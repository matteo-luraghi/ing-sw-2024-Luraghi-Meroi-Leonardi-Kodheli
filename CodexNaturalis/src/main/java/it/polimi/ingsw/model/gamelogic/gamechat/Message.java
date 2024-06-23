package it.polimi.ingsw.model.gamelogic.gamechat;

import it.polimi.ingsw.model.gamelogic.Player;

import java.io.Serial;
import java.io.Serializable;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

/**
 * message class
 * @author Francesk Kodheli
 */
public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = -6160152938762690523L;
    private final String message;
    private final Player author;
    private final String time;
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
        Date date=new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("[HH:mm:ss]");
        this.time = dateFormat.format(date);
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
        this.time=message.getTime();
    }


    /**
     * message copy Getter
     * @return string message
     */
    public String getMessage()
    {
        return message; //prevent modifications to the message
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
    public String getTime()
    {
        return this.time;
    }

    /**
     * method to print the message
     * @return
     */
    public String toString () {
        String result = "";

        result += this.time + " ";

        if (!this.recipient.equalsIgnoreCase("all")) {
            result += "(PRIVATE) ";
        }

        result += "(" + this.author + "): ";

        result += this.message;

        return result;
    }

    /**
     * toString method for GUI purpose
     * @return STRING format of the message
     */
    public String toStringGUI () {
        String result = "";

        result += this.time + " ";

        if (!this.recipient.equalsIgnoreCase("all")) {
            result += "(PRIVATE) ";
        }

        result += "(" + this.author.getNickname() + "): ";

        result += this.message;

        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(message, message1.message) && Objects.equals(author, message1.author) && Objects.equals(time, message1.time) && Objects.equals(recipient, message1.recipient);
    }
}
