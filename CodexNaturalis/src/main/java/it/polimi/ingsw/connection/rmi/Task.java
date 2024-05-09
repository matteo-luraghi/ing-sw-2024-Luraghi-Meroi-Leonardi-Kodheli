package it.polimi.ingsw.connection.rmi;

public interface Task<T> {
    T execute();
}
