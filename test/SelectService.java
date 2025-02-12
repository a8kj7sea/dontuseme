package me.a8kj.zobrelib.impl.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.a8kj.zobrelib.database.Database;
import me.a8kj.zobrelib.database.service.DatabaseService;

/**
 * Service that performs a SELECT operation on the specified database table.
 * <p>
 * This service executes a SELECT query on the given table and prints the
 * records.
 * It assumes that the table has a column named "name".
 * </p>
 * 
 * @author a8kj7sea
 */
public class SelectService implements DatabaseService {

    private final String table;

    /**
     * Constructor to initialize the SelectService with the table name.
     *
     * @param table The name of the table to query.
     */
    public SelectService(String table) {
        this.table = table;
    }

    /**
     * Executes a SELECT query on the specified table to retrieve all records.
     * It will print the "name" field of each record.
     *
     * @param database The database object that will be used for the query.
     * @throws IllegalStateException If the database is not connected.
     */
    @Override
    public void serve(Database<?> database) {
        if (!canServe(database)) {
            throw new IllegalStateException("Cannot perform select operation. Database is not connected.");
        }

        Connection connection = (Connection) database.getConnectionContainer().getConnection();

        if (!(connection instanceof java.sql.Connection)) {
            throw new IllegalStateException("Invalid connection type. Expected SQL Connection.");
        }

        String sql = "SELECT * FROM " + table;
        try (PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println("Record: " + name);
            }

        } catch (SQLException e) {
            System.err.println("Error executing select statement: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Checks if the database is connected and if the SELECT operation can be
     * performed.
     *
     * @param database The database to check the connection status.
     * @return true if the database is connected, otherwise false.
     */
    @Override
    public boolean canServe(Database<?> database) {
        return database.isConnected();
    }
}
