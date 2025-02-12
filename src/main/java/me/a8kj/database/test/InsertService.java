package me.a8kj.database.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.a8kj.database.Database;
import me.a8kj.database.service.DatabaseService;

@RequiredArgsConstructor
@Getter
public class InsertService implements DatabaseService {

    private final String name;
    private final String table;

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

    @Override
    public boolean canServe(Database<?> database) {
        return database.isConnected();
    }
}
