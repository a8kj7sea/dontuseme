package me.a8kj.zobrelib.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.zobrelib.database.attributes.CredentialsKey;

/**
 * Enum representing the necessary credentials for HikariCP connection pooling.
 * <p>
 * This enum defines the different types of credentials that are required to
 * establish
 * a connection using the HikariCP connection pool. These credentials are
 * typically
 * provided by the user during the setup of the connection pool.
 * </p>
 * <ul>
 * <li>{@link #JDBC_URL} - The JDBC URL for connecting to the database.</li>
 * <li>{@link #USERNAME} - The username for the database connection.</li>
 * <li>{@link #PASSWORD} - The password for the database connection.</li>
 * <li>{@link #DRIVER_CLASS_NAME} - The class name of the JDBC driver.</li>
 * <li>{@link #MAX_POOL_SIZE} - The maximum size of the connection pool.</li>
 * <li>{@link #PORT} - The port to connect to the database.</li>
 * </ul>
 * <p>
 * These credentials are typically passed to the {@link HikariCP} connection
 * pool
 * for establishing and managing database connections.
 * </p>
 */
@RequiredArgsConstructor
@Getter
public enum HikariCPDatabaseCredentials implements CredentialsKey {
    /**
     * The JDBC URL to connect to the database.
     */
    JDBC_URL("jdbcUrl"),

    /**
     * The username for the database connection.
     */
    USERNAME("username"),

    /**
     * The password for the database connection.
     */
    PASSWORD("password"),

    /**
     * The class name of the JDBC driver used for the connection.
     */
    DRIVER_CLASS_NAME("driverClassName"),

    /**
     * The maximum size of the connection pool.
     */
    MAX_POOL_SIZE("maxPoolSize"),

    /**
     * The port number used for connecting to the database.
     */
    PORT("port");

    private final String name;
}
