package me.a8kj.database.attributes;

import java.util.HashMap;
import java.util.Map;

import me.a8kj.database.attributes.value.CredentialsValue;
import me.a8kj.database.attributes.value.impl.SimpleCredentialsValue;
import me.a8kj.database.exception.impl.InvalidPropertiesException;

public class DatabaseCredentialsImpl<K extends Enum<K> & CredentialsKey> implements DatabaseCredentials<K> {

    private final Map<K, CredentialsValue<?>> credentials = new HashMap<>();

    @Override
    public Map<K, CredentialsValue<?>> getCredentials() {
        return credentials;
    }

    @Override
    public <U> void addCredential(K key, Class<U> type, U value) {
        if (key == null || value == null) {
            throw new InvalidPropertiesException("Key and value cannot be null", new IllegalArgumentException());
        }

        credentials.put(key, SimpleCredentialsValue.of(type, value));
    }

    @Override
    public void removeCredential(K key) {
        if (key == null) {
            throw new InvalidPropertiesException("Key cannot be null", new IllegalArgumentException());
        }

        credentials.remove(key);
    }

    @Override
    public void clearCredentials() {
        credentials.clear();
    }
}
