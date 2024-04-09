package it.polimi.ingsw.model.gamelogic;

import com.google.gson.Gson;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.card.PositionGoalCard;
import it.polimi.ingsw.model.card.ResourceGoalCard;
import it.polimi.ingsw.model.card.StartingCard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * SetUpState class
 * it manages the creation of the game
 * @author Lorenzo Meroi
 */
public class SetUpState extends State{

    /**
     * constructor of the SetUpState class
     */
    public SetUpState() {
        HandleState();
    }

    /**
     * method to handle the setupping state of the game
     */
    @Override
    public void HandleState() {
        Gson gson = new Gson();
        //temporarily I assume there's going to be 4 players
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Lorenzo", Color.RED));
        players.add(new Player("Gabriel", Color.BLUE));
        players.add(new Player("Matteo", Color.GREEN));
        players.add(new Player("Françesk", Color.YELLOW));
        //end of Players initialization

        ScoreBoard scoreBoard = new ScoreBoard(players);
        Deck goldDeck = new Deck(true);
        Deck resourceDeck = new Deck(false);

        Map<Player, PlayerField> playerZones = new HashMap<Player, PlayerField>();

        // generate 4 different starting cards
        int[] startingCards = new Random().ints(1,6).distinct().limit(4).toArray();
        for (int i=0; i<players.size(); i++) {

            int startingCardNum = startingCards[i];

            // parse random starting card
            String cardPath = "../resources/CardsJSON/startingCards/startingCard" + startingCardNum + ".json"; //TODO: fix root path
            try(Reader reader = new FileReader(cardPath)) {
                StartingCard card = gson.fromJson(reader, StartingCard.class);
                PlayerField playerField = new PlayerField(card);

                // add player and playerfield to map
                playerZones.put(players.get(i), playerField);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Goal cards parsing
        List<GoalCard> goalCards = new ArrayList<>();
        for(int i=1; i<=8; i++) {

            //parse resource card
            String resourceCardPath = "../resources/CardsJSON/goalCards/resourceGoalCard" + i + ".json"; //TODO: fix root path
            try(Reader reader = new FileReader(resourceCardPath)) {
                ResourceGoalCard resourceCard = gson.fromJson(reader, ResourceGoalCard.class);
                goalCards.add(resourceCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
            //parse position card
            String positionCardPath = "../resources/CardsJSON/goalCards/positionGoalCard" + i + ".json"; //TODO: fix root path
            try(Reader reader = new FileReader(positionCardPath)) {
                PositionGoalCard positionCard = gson.fromJson(reader, PositionGoalCard.class);
                goalCards.add(positionCard);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Collections.shuffle(goalCards);

        // for each player make them select the private goal and save it in its player field
        for(Map.Entry<Player, PlayerField> entry: playerZones.entrySet()) {
            PlayerField playerField = entry.getValue();
            playerField.choosePrivateGoal(goalCards.removeFirst(), goalCards.removeFirst());
        }

        GoalCard[] commonGoals = new GoalCard[2];
        commonGoals[0] = goalCards.removeFirst();
        commonGoals[1] = goalCards.removeFirst();

        // put them into an array for the GameTable constructor
        GameTable gameTable = new GameTable(resourceDeck, goldDeck, playerZones, commonGoals ,scoreBoard);
        this.game = new GameState(players, players.get(0), gameTable);
        this.transition(new GameFlowState(this.game));
    }
}
