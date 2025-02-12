package me.a8kj.zobrelib.database.attributes;

/**
 * An interface that defines a credentials key.
 * <p>
 * This interface is intended to be implemented by enum types or classes
 * that represent various keys used for database credentials (e.g., URL,
 * username, password).
 * </p>
 * 
 * @author a8kj7sea
 */
public interface CredentialsKey {

    /**
     * Retrieves the name of the credentials key.
     * 
     * @return the name of the key
     */
    String getName();
}
