package me.a8kj.test.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.database.attributes.CredentialsKey;

/**
 * Enum representing the basic credentials needed for database connection.
 * <p>
 * This enum defines the keys used for the URL, username, and password needed
 * to authenticate and connect to a database.
 * </p>
 * 
 * @author a8kj7sea
 */
@RequiredArgsConstructor
@Getter
public enum BasicCredentials implements CredentialsKey {

    /**
     * The URL of the database to connect to.
     */
    URL("url"),

    /**
     * The username used for authentication.
     */
    USERNAME("username"),

    /**
     * The password used for authentication.
     */
    PASSWORD("password");

    private final String name;
}
