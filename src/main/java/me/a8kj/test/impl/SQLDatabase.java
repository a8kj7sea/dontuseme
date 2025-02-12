package me.a8kj.test.impl;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import lombok.Getter;
import me.a8kj.database.Database;
import me.a8kj.database.attributes.ConnectionContainer;
import me.a8kj.database.attributes.CredentialsKey;
import me.a8kj.database.attributes.DatabaseCredentials;
import me.a8kj.database.attributes.DatabaseCredentialsImpl;
import me.a8kj.database.cycle.DatabaseCycle;
import me.a8kj.database.enums.ConnectionStatus;
import me.a8kj.database.exception.DatabaseExceptionBase;
import me.a8kj.database.exception.impl.InvalidPropertiesException;
import me.a8kj.database.enums.ConnectionMessages;

/**
 * SQLDatabase class implements the Database interface and manages the
 * connection
 * to a SQL database.
 * <p>
 * This class is responsible for handling the connection lifecycle, including
 * establishing the connection, disconnecting, restarting, and updating the
 * status
 * of the connection. It uses credentials provided through the
 * DatabaseCredentials
 * interface and interacts with the specified DatabaseCycle for actions like
 * connect, disconnect, and restart.
 * </p>
 * 
 * @param <Cycle> The type of the cycle (DatabaseCycle).
 * @author a8kj7sea
 */
@Getter
public class SQLDatabase<Cycle extends DatabaseCycle> implements Database<Cycle> {

    private final String name;
    private ConnectionStatus status;
    private Cycle cycle;
    private ConnectionContainer<Connection> connectionContainer;
    private DatabaseCredentials<BasicCredentials> credentials;
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
        setCredentials(new DatabaseCredentialsImpl<BasicCredentials>());
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
     * 
     * @throws DatabaseExceptionBase If connection cannot be established.
     */
    @Override
    public void connect() {
        if (status == ConnectionStatus.CONNECTED) {
            throw new DatabaseExceptionBase("Already connected to the database.");
        }

        try {
            String url = credentials.getCredential(BasicCredentials.URL, String.class);
            String username = credentials.getCredential(BasicCredentials.USERNAME, String.class);
            String password = credentials.getCredential(BasicCredentials.PASSWORD, String.class);
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
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
            if (connection != null && !connection.isClosed()) {
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
        this.credentials = (DatabaseCredentials<BasicCredentials>) credentials;
    }
}
