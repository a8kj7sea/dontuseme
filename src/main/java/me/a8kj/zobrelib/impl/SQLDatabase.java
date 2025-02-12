package me.a8kj.zobrelib.impl;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

import lombok.Getter;
import me.a8kj.zobrelib.database.Database;
import me.a8kj.zobrelib.database.attributes.ConnectionContainer;
import me.a8kj.zobrelib.database.attributes.CredentialsKey;
import me.a8kj.zobrelib.database.attributes.DatabaseCredentials;
import me.a8kj.zobrelib.database.attributes.DatabaseCredentialsImpl;
import me.a8kj.zobrelib.database.cycle.DatabaseCycle;
import me.a8kj.zobrelib.database.enums.ConnectionMessages;
import me.a8kj.zobrelib.database.enums.ConnectionStatus;
import me.a8kj.zobrelib.database.exception.DatabaseExceptionBase;
import me.a8kj.zobrelib.database.exception.impl.InvalidPropertiesException;

/**
 * SQLDatabase class implements the Database interface and manages the
 * connection to a SQL database using HikariCP.
 * 
 * @param <Cycle> The type of the cycle (DatabaseCycle).
 */

 // btw i hate my life :) 
@Getter
public class SQLDatabase<Cycle extends DatabaseCycle> implements Database<Cycle> {

    private final String name;
    private ConnectionStatus status;
    private Cycle cycle;
    private ConnectionContainer<Connection> connectionContainer;
    private DatabaseCredentials<HikariCPDatabaseCredentials> credentials;
    private HikariDataSource hikariDataSource;
    private Connection connection;

    /**
     * Constructor to initialize the SQLDatabase with the specified name and cycle.
     * 
     * @param name  The name of the database.
     * @param cycle The cycle object that defines actions during connection
     *              lifecycle.
     */
    public SQLDatabase(String name, Cycle cycle) {
        this.name = name;
        this.status = ConnectionStatus.DISCONNECTED;
        setCredentials(new DatabaseCredentialsImpl<HikariCPDatabaseCredentials>());
        this.cycle = cycle;
        this.cycle.setDatabase(this);
        this.connectionContainer = new ConnectionContainer<>();
    }

    /**
     * Updates the connection status and prints the relevant message.
     * 
     * @param status The new connection status to set.
     */
    @Override
    public void updateConnectionStatus(ConnectionStatus status) {
        this.status = status;
        switch (status) {
            case CONNECTED:
                System.out.println(ConnectionMessages.SUCCESSFULLY_CONNECTED.format(name));
                break;
            case DISCONNECTED:
                System.out.println(ConnectionMessages.CLOSED_CONNECTION.format(name));
                break;
            case ERROR:
                System.out.println(ConnectionMessages.FAILED_CONNECTION.format(name));
                break;
            default:
                System.out.println("Unknown status");
                break;
        }
    }

    /**
     * Establishes a connection to the database using the provided credentials.
     * It checks if the credentials are not null or missing before proceeding.
     * 
     * @throws DatabaseExceptionBase If connection cannot be established or
     *                               credentials are invalid.
     */
    @Override
    public void connect() {
        if (status == ConnectionStatus.CONNECTED) {
            throw new DatabaseExceptionBase("Already connected to the database.");
        }

        String jdbcUrl = credentials.getCredential(HikariCPDatabaseCredentials.JDBC_URL, String.class);
        String username = credentials.getCredential(HikariCPDatabaseCredentials.USERNAME, String.class);
        String password = credentials.getCredential(HikariCPDatabaseCredentials.PASSWORD, String.class);
        String driverClassName = credentials.getCredential(HikariCPDatabaseCredentials.DRIVER_CLASS_NAME, String.class);
        Integer port = credentials.getCredential(HikariCPDatabaseCredentials.PORT, Integer.class);
        Integer maxPoolSize = credentials.getCredential(HikariCPDatabaseCredentials.MAX_POOL_SIZE, Integer.class);

        if (jdbcUrl == null || username == null || password == null || driverClassName == null || port == null
                || maxPoolSize == null) {
            throw new InvalidPropertiesException(
                    "Missing required credentials: Please provide all the necessary credentials.");
        }

        try {

            Class.forName(driverClassName);

            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(jdbcUrl);
            hikariConfig.setUsername(username);
            hikariConfig.setPassword(password);
            hikariConfig.setDriverClassName(driverClassName);
            hikariConfig.setMaximumPoolSize(maxPoolSize);

            hikariDataSource = new HikariDataSource(hikariConfig);

            connection = hikariDataSource.getConnection();
            connectionContainer.setConnection(connection);
            updateConnectionStatus(ConnectionStatus.CONNECTED);
            this.cycle.onConnect();

        } catch (SQLException e) {
            updateConnectionStatus(ConnectionStatus.ERROR);
            throw new DatabaseExceptionBase("Failed to connect to the database: " + e.getMessage(), e);
        } catch (InvalidPropertiesException e) {
            updateConnectionStatus(ConnectionStatus.ERROR);
            throw new DatabaseExceptionBase("Invalid credentials: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            updateConnectionStatus(ConnectionStatus.ERROR);
            e.printStackTrace();
        }
    }

    /**
     * Restarts the database connection by disconnecting and reconnecting.
     * 
     * @throws DatabaseExceptionBase If any error occurs while restarting the
     *                               connection.
     */
    @Override
    public void restart() {
        disconnect();
        connect();
        this.cycle.onRestart();
    }

    /**
     * Disconnects from the database if connected.
     * 
     * @throws DatabaseExceptionBase If no active connection exists to disconnect.
     */
    @Override
    public void disconnect() {
        if (status == ConnectionStatus.DISCONNECTED) {
            throw new DatabaseExceptionBase("No active connection to disconnect.");
        }

        try {
            if (isConnected()) {
                connection.close();
                connectionContainer.setConnection(null);
                updateConnectionStatus(ConnectionStatus.DISCONNECTED);
                this.cycle.onDisconnect();
            }
        } catch (SQLException e) {
            throw new DatabaseExceptionBase("Failed to disconnect from the database: " + e.getMessage(), e);
        }
    }

    /**
     * Checks whether the database is connected.
     * 
     * @return true if the database is connected, otherwise false.
     */
    @Override
    public boolean isConnected() {
        try {
            return Database.super.isConnected() && connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Sets the credentials for the database connection.
     * 
     * @param credentials The credentials to set for the connection.
     */
    @SuppressWarnings("unchecked")
    @Override
    public void setCredentials(DatabaseCredentials<? extends Enum<? extends CredentialsKey>> credentials) {
        this.credentials = (DatabaseCredentials<HikariCPDatabaseCredentials>) credentials;
    }
}
