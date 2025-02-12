package me.a8kj.database.attributes;

import java.util.Map;

import me.a8kj.database.attributes.value.CredentialsValue;
import me.a8kj.database.exception.impl.InvalidPropertiesException;

public interface DatabaseCredentials<K extends Enum<? extends CredentialsKey>> {



    Map<K, CredentialsValue<?>> getCredentials();

    default <U> U getCredential(K key, Class<U> type) {
        if (getCredentials().isEmpty() || getCredentials() == null)
            throw new InvalidPropertiesException("Database properties cannot be empty", new IllegalArgumentException());
        CredentialsValue<?> valueHolder = getCredentials().get(key);
        if (valueHolder != null && valueHolder.getType().equals(type)) {
            return type.cast(valueHolder.getValue());
        }
        return null;
    }

    <U> void addCredential(K key, Class<U> type, U value);

    void removeCredential(K key);

    void clearCredentials();
}
