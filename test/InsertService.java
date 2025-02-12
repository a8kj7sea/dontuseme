package me.a8kj.zobrelib.impl.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.zobrelib.database.Database;
import me.a8kj.zobrelib.database.service.DatabaseService;

/**
 * InsertService is responsible for performing an insert operation into a
 * specified
 * table in the database.
 * <p>
 * This service ensures that the database is connected and the connection is
 * valid
 * before executing the insert operation. It inserts a name into the specified
 * table
 * and logs the number of rows affected.
 * </p>
 * 
 * @author a8kj7sea
 */
@RequiredArgsConstructor
@Getter
public class InsertService implements DatabaseService {

    private final String name;
    private final String table;

    /**
     * Executes the insert operation into the specified table using the provided
     * database.
     * Ensures that the database is connected and the connection type is valid
     * before
     * executing the statement.
     * 
     * @param database The database instance on which the insert operation will be
     *                 performed.
     * @throws IllegalStateException If the database is not connected or the
     *                               connection type is invalid.
     */
    @Override
    public void serve(Database<?> database) {
        if (!canServe(database)) {
            throw new IllegalStateException("Cannot perform insert operation. Database is not connected.");
        }

        Connection connection = (Connection) database.getConnectionContainer().getConnection();

        if (!(connection instanceof java.sql.Connection)) {
            throw new IllegalStateException("Invalid connection type. Expected SQL Connection.");
        }

        String sql = "INSERT INTO " + table + " (name) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            int rowsAffected = statement.executeUpdate();
            System.out.println("Inserted " + rowsAffected + " row(s) into " + table);
        } catch (SQLException e) {
            System.err.println("Error executing insert statement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks if the database can serve the insert operation.
     * 
     * @param database The database to check.
     * @return true if the database is connected, otherwise false.
     */
    @Override
    public boolean canServe(Database<?> database) {
        return database.isConnected();
    }
}
