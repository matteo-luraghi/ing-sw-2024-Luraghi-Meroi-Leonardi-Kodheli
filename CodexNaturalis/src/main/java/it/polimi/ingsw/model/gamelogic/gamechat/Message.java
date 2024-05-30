package it.polimi.ingsw.model.gamelogic.gamechat;

import it.polimi.ingsw.model.gamelogic.Player;

import java.util.Date;

public class Message {

    private String message;
    private Player author;
    private Date date;

    /**
     * Default constructor for message, date is initialized with the class
     * @param message String message
     * @param author Author of the message
     */
    public Message(String message, Player author)
    {
        this.message=message;
        this.author=author;
        date=new Date();
    }

    /**
     * Constructor for already initialized message
     * @param message message class to copy
     */
    public Message(Message message)
    {

        this.message= message.getMessage();
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
     * date getter
     * @return Date copy
     */
    public Date getDate()
    {
        return (Date) date.clone(); //prevent modifications to the date
    }
}
