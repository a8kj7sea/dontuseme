package me.a8kj.database.cycle;

import me.a8kj.database.Database;

public interface DatabaseCycle {
    void onConnect();

    void onDisconnect();

    void onRestart();

    void setDatabase(Database<?> database);
}
