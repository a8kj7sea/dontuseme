package me.a8kj.database.attributes.value.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.database.attributes.value.CredentialsValue;

@RequiredArgsConstructor(staticName = "of")
@Getter
public class SimpleCredentialsValue<U> implements CredentialsValue<U> {
    private final Class<U> type;
    private final U value;

}
