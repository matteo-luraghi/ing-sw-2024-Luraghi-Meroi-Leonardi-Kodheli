package it.polimi.ingsw.view.mainview;

import it.polimi.ingsw.model.card.GameCard;

import java.io.Serial;
import java.io.Serializable;

/**
 * viewGameCardFactory abstract class
 * @author Lorenzo Meroi
 */
public abstract class ViewGameCardFactory implements Serializable {

    @Serial
    private static final long rerialVersionUID = -8649120532345782946L;
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
