package me.a8kj.zobrelib.database.attributes;

import java.util.Map;

import me.a8kj.zobrelib.database.attributes.value.CredentialsValue;
import me.a8kj.zobrelib.database.exception.impl.InvalidPropertiesException;

/**
 * Interface for handling database credentials.
 * <p>
 * This interface is responsible for managing the credentials required to
 * connect
 * to a database, such as the URL, username, and password. The credentials are
 * stored in a map, where the keys are of type {@link CredentialsKey}.
 * </p>
 * 
 * @param <K> the type of the credentials key
 * @author a8kj7sea
 */
public interface DatabaseCredentials<K extends Enum<? extends CredentialsKey>> {

    /**
     * Retrieves all stored credentials as a map.
     * 
     * @return a map of credentials where keys are of type {@link K} and values are
     *         of type {@link CredentialsValue}
     */
    Map<K, CredentialsValue<?>> getCredentials();

    /**
     * Retrieves a specific credential value based on the key and type.
     * 
     * @param key  the key of the credential to retrieve
     * @param type the expected type of the credential value
     * @param <U>  the type of the credential value
     * @return the value of the credential if it exists and matches the type, or
     *         null if not found
     * @throws InvalidPropertiesException if the credentials map is empty or null
     */
    default <U> U getCredential(K key, Class<U> type) {
        if (getCredentials().isEmpty() || getCredentials() == null)
            throw new InvalidPropertiesException("Database properties cannot be empty", new IllegalArgumentException());
        CredentialsValue<?> valueHolder = getCredentials().get(key);
        if (valueHolder != null && valueHolder.getType().equals(type)) {
            return type.cast(valueHolder.getValue());
        }
        return null;
    }

    /**
     * Adds a new credential to the credentials map.
     * 
     * @param key   the key for the new credential
     * @param type  the type of the credential value
     * @param value the value of the credential
     * @param <U>   the type of the credential value
     */
    <U> void addCredential(K key, Class<U> type, U value);

    /**
     * Removes a specific credential from the credentials map.
     * 
     * @param key the key of the credential to remove
     */
    void removeCredential(K key);

    /**
     * Clears all credentials stored in the map.
     */
    void clearCredentials();
}
