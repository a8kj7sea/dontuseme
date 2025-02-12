package me.a8kj.zobrelib.database.cycle;

import me.a8kj.zobrelib.database.Database;

/**
 * Represents the lifecycle events for a database connection.
 * <p>
 * This interface defines methods for handling the lifecycle of a database
 * connection.
 * Implementations of this interface are expected to provide specific behavior
 * for
 * actions such as connecting, disconnecting, and restarting the connection.
 * </p>
 * 
 * @author a8kj7sea
 */
public interface DatabaseCycle {

    /**
     * Called when the database connection is established.
     * This method is intended to contain logic for actions that need to be
     * performed
     * after successfully connecting to the database.
     */
    void onConnect();

    /**
     * Called when the database connection is closed.
     * This method is intended to contain logic for actions that need to be
     * performed
     * after disconnecting from the database.
     */
    void onDisconnect();

    /**
     * Called when the database connection is restarted.
     * This method is intended to contain logic for actions that need to be
     * performed
     * when the database connection is restarted.
     */
    void onRestart();

    /**
     * Sets the database instance for this cycle.
     * 
     * @param database the database instance to be set
     */
    void setDatabase(Database<?> database);
}
