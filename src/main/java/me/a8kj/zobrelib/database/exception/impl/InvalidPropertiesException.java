package me.a8kj.zobrelib.database.exception.impl;

import me.a8kj.zobrelib.database.exception.DatabaseExceptionBase;

/**
 * Exception thrown when invalid properties are encountered in database
 * operations.
 * <p>
 * This class extends {@link DatabaseExceptionBase} and is used to represent
 * errors
 * caused by invalid properties, such as missing or malformed database
 * credentials.
 * </p>
 * 
 * @author a8kj7sea
 */
public class InvalidPropertiesException extends DatabaseExceptionBase {

    /**
     * Constructs a new InvalidPropertiesException with the specified detail
     * message.
     * 
     * @param message the detail message.
     */
    public InvalidPropertiesException(String message) {
        super(message);
    }

    /**
     * Constructs a new InvalidPropertiesException with the specified detail message
     * and cause.
     * 
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public InvalidPropertiesException(String message, Throwable cause) {
        super(message, cause);
    }
}
