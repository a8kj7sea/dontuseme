package me.a8kj.database.exception;

/**
 * Interface for database-related exceptions.
 * <p>
 * This interface defines the structure for exceptions that may be thrown during
 * database operations. It extends standard exception behavior with custom
 * handling
 * specific to database operations.
 * </p>
 * 
 * @author a8kj7sea
 */
public interface DatabaseException {

    /**
     * Gets the message associated with the exception.
     * 
     * @return the detailed message of the exception.
     */
    String getMessage();

    /**
     * Gets the cause of the exception.
     * 
     * @return the cause of the exception.
     */
    Throwable getCause();
}
