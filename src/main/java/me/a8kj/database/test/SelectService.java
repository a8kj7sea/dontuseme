package me.a8kj.database.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import me.a8kj.database.Database;
import me.a8kj.database.service.DatabaseService;

public class SelectService implements DatabaseService {

    private final String table;

    public SelectService(String table) {
        this.table = table;
    }

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

    @Override
    public boolean canServe(Database<?> database) {
        return database.isConnected();
    }
}
