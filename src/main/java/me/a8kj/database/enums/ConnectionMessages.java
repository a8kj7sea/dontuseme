package me.a8kj.database.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Enum representing various connection messages used in database operations.
 * <p>
 * This enum contains predefined messages that are used for logging or
 * displaying
 * connection statuses during database operations.
 * </p>
 * 
 * @author a8kj7sea
 */
@RequiredArgsConstructor
@Getter
public enum ConnectionMessages {

    /**
     * Message indicating that the database is not connected.
     */
    NOT_CONNECTED("Not connected to the %s database."),

    /**
     * Message indicating successful connection to the database.
     */
    SUCCESSFULLY_CONNECTED("Connected to %s database successfully!"),

    /**
     * Message indicating failure in connecting to the database.
     */
    FAILED_CONNECTION("Failed to connect to %s database!"),

    /**
     * Message indicating that the database connection has been closed.
     */
    CLOSED_CONNECTION("Database connection to %s has been closed!");

    private final String message;

    /**
     * Formats the message with the provided database name.
     * 
     * @param databaseName the name of the database to include in the message
     * @return the formatted message with the database name
     */
    public String format(String databaseName) {
        return String.format(message, databaseName);
    }
}
