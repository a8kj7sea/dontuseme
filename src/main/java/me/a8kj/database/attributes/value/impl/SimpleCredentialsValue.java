package me.a8kj.database.attributes.value.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.database.attributes.value.CredentialsValue;

/**
 * A simple implementation of the {@link CredentialsValue} interface.
 * <p>
 * This class holds a type and value for a specific credential and provides
 * methods to retrieve them.
 * </p>
 * 
 * @param <U> the type of the credential's value
 * @author a8kj7sea
 */
@RequiredArgsConstructor(staticName = "of")
@Getter
public class SimpleCredentialsValue<U> implements CredentialsValue<U> {
    private final Class<U> type;
    private final U value;
}
