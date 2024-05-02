package it.polimi.ingsw.model.gamelogic;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {
    Player player;

    /**
     * player initializaion with name and color
     */
    @Before
    public void setup()
    {
        player=new Player("player1",Color.RED);
    }


    /**
     * Asserts that was initialized correctly,
     * Set all colors to the player and asserts that change was made successfully
     * Changes NickName and Asserts that's equal to the new name
     */
    @Test
    public void GetSetInfoTest()
    {
        assertEquals(Color.RED,player.getColor());
        Assertions.assertEquals("player1",player.getNickname());
        for(int i=0; i<Color.values().length;i++)
        {
            player.setColor(Color.values()[i]);
            assertEquals(Color.values()[i],player.getColor());
        }



        player.setNickname("Player2");

        Assertions.assertEquals("Player2",player.getNickname());
    }


}