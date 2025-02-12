package me.a8kj.database.exception;

public class DatabaseExceptionBase extends RuntimeException implements DatabaseException {

    public DatabaseExceptionBase(String message) {
        super(message);
    }

    public DatabaseExceptionBase(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public Throwable getCause() {
        return super.getCause();
    }
}
