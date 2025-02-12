package me.a8kj.database.exception;

/**
 * Custom exception class for database-related errors.
 * <p>
 * This class extends {@link RuntimeException} and implements
 * {@link DatabaseException}.
 * It is used to represent errors that occur during database operations.
 * </p>
 * 
 * @author a8kj7sea
 */
public class DatabaseExceptionBase extends RuntimeException implements DatabaseException {

    /**
     * Constructs a new DatabaseExceptionBase with the specified detail message.
     * 
     * @param message the detail message.
     */
    public DatabaseExceptionBase(String message) {
        super(message);
    }

    /**
     * Constructs a new DatabaseExceptionBase with the specified detail message and
     * cause.
     * 
     * @param message the detail message.
     * @param cause   the cause of the exception.
     */
    public DatabaseExceptionBase(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Retrieves the detail message of this exception.
     * 
     * @return the detail message of this exception.
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * Retrieves the cause of this exception.
     * 
     * @return the cause of this exception.
     */
    @Override
    public Throwable getCause() {
        return super.getCause();
    }
}
