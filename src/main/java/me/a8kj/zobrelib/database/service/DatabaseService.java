package me.a8kj.zobrelib.database.service;

import me.a8kj.zobrelib.database.Database;

/**
 * Interface for defining database services that can interact with a database.
 * <p>
 * A database service provides operations that can be performed on a database.
 * The service defines whether it can be executed on a particular database
 * through the {@link #canServe(Database)} method
 * and performs its actions using the {@link #serve(Database)} method.
 * </p>
 * 
 * @author a8kj7sea
 */
public interface DatabaseService {

    /**
     * Executes the service on the provided database.
     * 
     * @param database the database on which the service will operate.
     */
    void serve(Database<?> database);

    /**
     * Checks whether this service can be performed on the provided database.
     * 
     * @param database the database to check compatibility with.
     * @return true if the service can be executed on the database, false otherwise.
     */
    boolean canServe(Database<?> database);
}
