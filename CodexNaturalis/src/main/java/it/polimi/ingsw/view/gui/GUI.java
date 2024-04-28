package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.connection.Client;
import it.polimi.ingsw.model.card.GoalCard;
import it.polimi.ingsw.model.gamelogic.Player;
import it.polimi.ingsw.view.mainview.View;

public class GUI implements View {
    @Override
    public void start() {

    }

    @Override
    public Client connectToServer() {
        return null;
    }

    @Override
    public void showMessage(String s) {

    }

    @Override
    public void ShowLogin() {

    }

    @Override
    public void askForPlayersNumber() {

    }

    @Override
    public void ShowWaitingForPlayers() {

    }

    @Override
    public void ShowChoosePrivateGoal(GoalCard[] goalCards) {

    }

    @Override
    public void ShowPrivateGoal(Player player) {

    }

    @Override
    public void ShowPlayerField(Player player) {

    }

    @Override
    public void ShowDecks() {

    }

    @Override
    public void ShowScoreBoard() {

    }

    @Override
    public void ShowWinner() {

    }

    @Override
    public void ShowEndOfGame() {

    }
}
