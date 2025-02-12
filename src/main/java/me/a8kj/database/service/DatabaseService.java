package me.a8kj.database.service;

import me.a8kj.database.Database;

public interface DatabaseService {
    void serve(Database<?> database);

    boolean canServe(Database<?> database);
}
