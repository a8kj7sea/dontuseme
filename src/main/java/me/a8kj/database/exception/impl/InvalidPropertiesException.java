package me.a8kj.database.exception.impl;

import me.a8kj.database.exception.DatabaseExceptionBase;

public class InvalidPropertiesException extends DatabaseExceptionBase {

    public InvalidPropertiesException(String message) {
        super(message);
    }

    public InvalidPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }

}
