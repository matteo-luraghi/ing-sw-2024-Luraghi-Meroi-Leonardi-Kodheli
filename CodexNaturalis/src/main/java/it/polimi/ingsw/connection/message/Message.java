package it.polimi.ingsw.connection.message;

public enum Message {
    //server messages
    // init phase
    LOGIN_REQUEST, PLAYERS_NUMBER_REQUEST, COLOR_REQUEST, GOAL_CARD_REQUEST, USER_ALREADY_PRESENT, WAITING_FOR_PLAYERS,
    // gameflow
    YOUR_TURN, CARD_NOT_PLAYABLE, CARD_PLAYED,
    PICK_A_CARD, CANNOT_DRAW, CARD_DRAWN, TURN_ENDED,
    // endgame
    WINNER,

    // client messages
    LOGIN_RESPONSE, PLAYERS_NUMBER_RESPONSE, COLOR_RESPONSE, GOAL_CARD_RESPONSE, DRAW_CARD_RESPONSE,

    // connection messages
    PING, DISCONNECTION, TEXT
}