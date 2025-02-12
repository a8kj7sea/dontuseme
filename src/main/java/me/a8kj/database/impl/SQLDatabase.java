package me.a8kj.database.impl;

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

@Getter
public class SQLDatabase<Cycle extends DatabaseCycle> implements Database<Cycle> {

    private final String name;
    private ConnectionStatus status;
    private Cycle cycle;
    private ConnectionContainer<Connection> connectionContainer;
    private DatabaseCredentials<BasicCredentials> credentials;
    private Connection connection;

    public SQLDatabase(String name, Cycle cycle) {
        this.name = name;
        this.status = ConnectionStatus.DISCONNECTED;
        setCredentials(new DatabaseCredentialsImpl<BasicCredentials>());
        this.cycle = cycle;
        this.cycle.setDatabase(this);
        this.connectionContainer = new ConnectionContainer<>();
    }

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

    @Override
    public void restart() {
        disconnect();
        connect();
        this.cycle.onRestart();
    }

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

    @Override
    public boolean isConnected() {
        try {
            return Database.super.isConnected() && connection != null && !connection.isClosed();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void setCredentials(DatabaseCredentials<? extends Enum<? extends CredentialsKey>> credentials) {
        this.credentials = (DatabaseCredentials<BasicCredentials>) credentials;
    }
}
