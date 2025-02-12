package me.a8kj.database.exception;

public interface DatabaseException {
    String getMessage();

    Throwable getCause();
}
