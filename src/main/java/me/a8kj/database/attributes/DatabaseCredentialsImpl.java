package me.a8kj.database.attributes;

import java.util.HashMap;
import java.util.Map;

import me.a8kj.database.attributes.value.CredentialsValue;
import me.a8kj.database.attributes.value.impl.SimpleCredentialsValue;
import me.a8kj.database.exception.impl.InvalidPropertiesException;

/**
 * Implementation of the {@link DatabaseCredentials} interface for handling
 * database credentials.
 * <p>
 * This class provides a concrete implementation of the
 * {@link DatabaseCredentials}
 * interface, allowing you to manage credentials stored as key-value pairs. The
 * credentials are stored in a {@link Map} with keys of type {@link K} and
 * values
 * of type {@link CredentialsValue}.
 * </p>
 * 
 * @param <K> the type of the credentials key
 * @author a8kj7sea
 */
public class DatabaseCredentialsImpl<K extends Enum<K> & CredentialsKey> implements DatabaseCredentials<K> {

    /**
     * The map that stores the credentials, where each key is a
     * {@link CredentialsKey}
     * and each value is a {@link CredentialsValue}.
     */
    private final Map<K, CredentialsValue<?>> credentials = new HashMap<>();

    /**
     * Retrieves all stored credentials as a map.
     * 
     * @return a map of credentials where keys are of type {@link K} and values are
     *         of type {@link CredentialsValue}.
     */
    @Override
    public Map<K, CredentialsValue<?>> getCredentials() {
        return credentials;
    }

    /**
     * Adds a new credential to the credentials map.
     * 
     * @param key   the key for the new credential
     * @param type  the type of the credential value
     * @param value the value of the credential
     * @param <U>   the type of the credential value
     * @throws InvalidPropertiesException if the key or value is null
     */
    @Override
    public <U> void addCredential(K key, Class<U> type, U value) {
        if (key == null || value == null) {
            throw new InvalidPropertiesException("Key and value cannot be null", new IllegalArgumentException());
        }

        credentials.put(key, SimpleCredentialsValue.of(type, value));
    }

    /**
     * Removes a specific credential from the credentials map.
     * 
     * @param key the key of the credential to remove
     * @throws InvalidPropertiesException if the key is null
     */
    @Override
    public void removeCredential(K key) {
        if (key == null) {
            throw new InvalidPropertiesException("Key cannot be null", new IllegalArgumentException());
        }

        credentials.remove(key);
    }

    /**
     * Clears all credentials stored in the map.
     */
    @Override
    public void clearCredentials() {
        credentials.clear();
    }
}
