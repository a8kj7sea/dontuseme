package me.a8kj.database.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.database.attributes.CredentialsKey;

@RequiredArgsConstructor
@Getter
public enum BasicCredentials implements CredentialsKey {
    URL("url"),
    USERNAME("username"),
    PASSWORD("password");

    private final String name;

}
