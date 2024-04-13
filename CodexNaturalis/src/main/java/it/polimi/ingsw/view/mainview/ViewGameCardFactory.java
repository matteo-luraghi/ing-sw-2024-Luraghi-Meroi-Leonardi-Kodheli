package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.card.GameCard;

/**
 * viewGameCardFactory abstract class
 * @author Lorenzo Meroi
 */
public abstract class ViewGameCardFactory {

    protected GameCard card;

    /**
     * abstract method to show on screen a game card
     */
    public abstract void Show();

    /**
     * method to set which card to show
     * @param gameCard is the card you want to be shown
     */
    public void SetCard(GameCard gameCard) {
        this.card = gameCard;
    }
}
