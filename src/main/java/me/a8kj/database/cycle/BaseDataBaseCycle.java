package me.a8kj.database.cycle;

import me.a8kj.database.Database;

public abstract class BaseDataBaseCycle implements DatabaseCycle {
    protected Database<?> database;

    public void setDatabase(Database<?> database) {
        this.database = database;
    }
}
