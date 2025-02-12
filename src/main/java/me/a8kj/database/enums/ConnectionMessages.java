package me.a8kj.database.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ConnectionMessages {

    NOT_CONNECTED("Not connected to the %s database."),
    SUCCESSFULLY_CONNECTED("Connected to %s database successfully!"),
    FAILED_CONNECTION("Failed to connect to %s database!"),
    CLOSED_CONNECTION("Database connection to %s has been closed!");

    private final String message;

    public String format(String databaseName) {
        return String.format(message, databaseName);
    }
}
