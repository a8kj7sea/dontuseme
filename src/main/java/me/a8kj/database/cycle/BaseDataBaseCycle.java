package me.a8kj.database.cycle;

import me.a8kj.database.Database;

/**
 * An abstract base implementation of the {@link DatabaseCycle} interface.
 * <p>
 * This class provides a default implementation for the
 * {@link DatabaseCycle#setDatabase(Database)}
 * method, allowing subclasses to focus on implementing the lifecycle methods
 * such as
 * {@link #onConnect()}, {@link #onDisconnect()}, and {@link #onRestart()}.
 * </p>
 * 
 * @author a8kj7sea
 */
public abstract class BaseDataBaseCycle implements DatabaseCycle {
    protected Database<?> database;

    /**
     * Sets the database instance for this cycle.
     * 
     * @param database the database instance to be set
     */
    public void setDatabase(Database<?> database) {
        this.database = database;
    }
}
