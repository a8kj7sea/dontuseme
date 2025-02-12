package me.a8kj.database.enums;

/**
 * Enum representing the different connection statuses for a database.
 * <p>
 * This enum is used to track the current state of the database connection
 * during database operations.
 * </p>
 * 
 * @author a8kj7sea
 */
public enum ConnectionStatus {

    /**
     * Represents the state where the database connection is idle (not active).
     */
    IDLE,

    /**
     * Represents the state where the database connection is successfully
     * established.
     */
    CONNECTED,

    /**
     * Represents the state where the database connection has been disconnected.
     */
    DISCONNECTED,

    /**
     * Represents the state where an error occurred during the database connection.
     */
    ERROR;
}
