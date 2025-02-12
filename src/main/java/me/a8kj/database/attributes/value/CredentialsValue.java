package me.a8kj.database.attributes.value;

public interface CredentialsValue<U> {
    Class<U> getType();
    U getValue();
}

