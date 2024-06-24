package it.polimi.ingsw.model.gamelogic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ScoreBoard test
 * @author Françesko
 */
public class ScoreBoardTest {

    ScoreBoard scoreBoard;
    ArrayList<Player> players = new ArrayList<>();
    @BeforeEach
    public void setup()
    {
        Random random = new Random();
        int numberOfPlayers = random.nextInt(3) + 2;
        for (int i = 1; i <= numberOfPlayers; i++) {
            players.add(new Player("Player" + i, Color.values()[i - 1]));
        }
        scoreBoard=new ScoreBoard(players);
    }

    /**
     * Test if board map is initialized correctly
     * @result= all the keys are 0
     */
    @Test
    public void getBoardTest() {
        Map<Player,Integer> board=scoreBoard.getBoard();
        for(Player player: players)
        {
            assertEquals(0,board.get(player));
        }
    }

    /**
     * test for adding points to each player
     * @result= if less than zero exception has to be thrown and points will not be added, otherwise the getter will return the setted points
     */
    @Test
    public void testAddPoints() {
        Random random=new Random();
        for(Player player: players) {
            int randPoints = random.nextInt(-10, 10);
            try {
                scoreBoard.addPoints(player, randPoints);
                if(randPoints<0)
                    fail();
            }
            catch (IllegalArgumentException e)
            {
                if(randPoints>=0)
                    fail();
            }
            if(randPoints>=0)
            assertEquals(randPoints,scoreBoard.getPoints(player));
            else assertEquals(0,scoreBoard.getPoints(player));
        }
    }
}