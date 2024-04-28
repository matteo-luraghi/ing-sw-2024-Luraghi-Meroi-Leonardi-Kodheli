package it.polimi.ingsw.connection.message;

/**
 * server, client and connection messages
 */
public enum Message {
    // init phase
    LOGIN_REQUEST, PLAYERS_NUMBER_REQUEST, COLOR_REQUEST, GOAL_CARD_REQUEST, WAITING_FOR_PLAYERS,
    // gameflow
    YOUR_TURN,
    PLAY_CARD_REQUEST, DRAW_CARD_REQUEST, TURN_ENDED,
    // endgame
    WINNER,
    // client messages
    LOGIN_RESPONSE, PLAYERS_NUMBER_RESPONSE, COLOR_RESPONSE, GOAL_CARD_RESPONSE, PLAY_CARD_RESPONSE, DRAW_CARD_RESPONSE,
    // connection messages
    PING, DISCONNECTION, TEXT
}