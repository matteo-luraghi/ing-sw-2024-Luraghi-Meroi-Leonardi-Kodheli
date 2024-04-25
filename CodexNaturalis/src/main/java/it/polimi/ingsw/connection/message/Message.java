package it.polimi.ingsw.connection.message;

public enum Message {
    // init phase
    LOGIN_REQUEST, PLAYERS_NUMBER_REQUEST, USER_ALREADY_PRESENT, WAITING_FOR_PLAYERS,
    // gameflow
    YOUR_TURN, CARD_NOT_PLAYABLE, CARD_PLAYED,
    PICK_A_CARD, CANNOT_DRAW, CARD_DRAWN, TURN_ENDED,
    // endgame
    WINNER,
    // utils
    PING, DISCONNECTION
}