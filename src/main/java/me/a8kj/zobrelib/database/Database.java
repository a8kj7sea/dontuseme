package me.a8kj.zobrelib.database;

import me.a8kj.zobrelib.database.attributes.ConnectionContainer;
import me.a8kj.zobrelib.database.attributes.CredentialsKey;
import me.a8kj.zobrelib.database.attributes.DatabaseCredentials;
import me.a8kj.zobrelib.database.cycle.DatabaseCycle;
import me.a8kj.zobrelib.database.enums.ConnectionStatus;
import me.a8kj.zobrelib.database.service.DatabaseService;

/**
 * Interface representing a database connection and operations on it.
 * <p>
 * This interface defines the fundamental operations for interacting with a
 * database,
 * including connecting, disconnecting, updating connection status, and
 * performing services.
 * It also manages credentials and lifecycle states for the database connection.
 * </p>
 * 
 * @param <U> The type of the database cycle that manages the connection.
 * @author a8kj7sea
 */
public interface Database<U extends DatabaseCycle> {

    /**
     * Retrieves the name of the database.
     * 
     * @return The name of the database.
     */
    String getName();

    /**
     * Retrieves the current status of the database connection.
     * 
     * @return The current connection status.
     */
    ConnectionStatus getStatus();

    /**
     * Checks if the database is currently connected.
     * 
     * @return true if the database is connected, false otherwise.
     */
    default boolean isConnected() {
        return getStatus() == ConnectionStatus.CONNECTED;
    }

    /**
     * Sets the credentials required to connect to the database.
     * 
     * @param credentials The credentials to be set for the database connection.
     */
    void setCredentials(DatabaseCredentials<? extends Enum<? extends CredentialsKey>> credentials);

    /**
     * Retrieves the credentials used to connect to the database.
     * 
     * @return The credentials used for the database connection.
     */
    DatabaseCredentials<? extends Enum<? extends CredentialsKey>> getCredentials();

    /**
     * Retrieves the connection container, which holds the current connection to the
     * database.
     * 
     * @param <C> The type of the connection.
     * @return The container holding the current connection.
     */
    <C> ConnectionContainer<C> getConnectionContainer();

    /**
     * Updates the connection status of the database.
     * 
     * @param status The new status of the database connection.
     */
    void updateConnectionStatus(ConnectionStatus status);

    /**
     * Establishes a connection to the database.
     */
    void connect();

    /**
     * Restarts the database connection.
     */
    void restart();

    /**
     * Disconnects from the database.
     */
    void disconnect();

    /**
     * Executes a service on the database if it can be served.
     * 
     * @param service The service to be executed on the database.
     * @throws IllegalStateException If the service cannot be executed due to the
     *                               database state.
     */
    default void serve(DatabaseService service) {
        if (!service.canServe(this))
            throw new IllegalStateException("b8rsh a3ml serve lhay al database!");
        service.serve(this);
    }

    /**
     * Retrieves the database cycle, which defines actions when the database is
     * connected, disconnected, or restarted.
     * 
     * @return The cycle of the database connection.
     */
    U getCycle();

}
