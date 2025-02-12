package me.a8kj.zobrelib.database;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import me.a8kj.zobrelib.database.attributes.ConnectionContainer;
import me.a8kj.zobrelib.database.attributes.CredentialsKey;
import me.a8kj.zobrelib.database.attributes.DatabaseCredentials;
import me.a8kj.zobrelib.database.cycle.DatabaseCycle;
import me.a8kj.zobrelib.database.enums.ConnectionStatus;
import me.a8kj.zobrelib.database.service.DatabaseService;
import me.a8kj.zobrelib.database.service.ServiceWithType;

/**
 * Interface representing a database connection and operations on it.
 * <p>
 * This interface defines the fundamental operations for interacting with a
 * database,
 * including connecting, disconnecting, updating connection status, and
 * performing services.
 * It also manages credentials and lifecycle states for the database connection.
 * </p>
 * 
 * @param <U> The type of the database cycle that manages the connection.
 * @author a8kj7sea
 */
public interface Database<U extends DatabaseCycle> {

    /**
     * Retrieves the name of the database.
     * 
     * @return The name of the database.
     */
    String getName();

    /**
     * Retrieves the current status of the database connection.
     * 
     * @return The current connection status.
     */
    ConnectionStatus getStatus();

    /**
     * Checks if the database is currently connected.
     * 
     * @return true if the database is connected, false otherwise.
     */
    default boolean isConnected() {
        return getStatus() == ConnectionStatus.CONNECTED;
    }

    /**
     * Sets the credentials required to connect to the database.
     * 
     * @param credentials The credentials to be set for the database connection.
     */
    void setCredentials(DatabaseCredentials<? extends Enum<? extends CredentialsKey>> credentials);

    /**
     * Retrieves the credentials used to connect to the database.
     * 
     * @return The credentials used for the database connection.
     */
    DatabaseCredentials<? extends Enum<? extends CredentialsKey>> getCredentials();

    /**
     * Retrieves the connection container, which holds the current connection to the
     * database.
     * 
     * @param <C> The type of the connection.
     * @return The container holding the current connection.
     */
    <C> ConnectionContainer<C> getConnectionContainer();

    /**
     * Updates the connection status of the database.
     * 
     * @param status The new status of the database connection.
     */
    void updateConnectionStatus(ConnectionStatus status);

    /**
     * Establishes a connection to the database.
     */
    void connect();

    /**
     * Restarts the database connection.
     */
    void restart();

    /**
     * Disconnects from the database.
     */
    void disconnect();

    /**
     * Executes a service on the database if it can be served.
     * 
     * @param service The service to be executed on the database.
     * @throws IllegalStateException If the service cannot be executed due to the
     *                               database state.
     */
    default void serve(DatabaseService service) {
        if (!service.canServe(this))
            throw new IllegalStateException("b8rsh a3ml serve lhay al database!");
        service.serve(this);
    }

    /**
     * Executes a service on the database based on the given execution type.
     *
     * @param service    The service to be executed on the database.
     * @param type       The type of execution (SYNC, ASYNC, TIMEOUT, RETRY).
     * @param timeout    The timeout duration (only used if type is TIMEOUT).
     * @param unit       The time unit for the timeout (only used if type is
     *                   TIMEOUT).
     * @param maxRetries The maximum number of retries (only used if type is RETRY).
     * @param retryDelay The delay between retries in the given time unit (only used
     *                   if type is RETRY).
     * @throws IllegalStateException If the service cannot be executed due to the
     *                               database state.
     */
    default CompletableFuture<Void> serve(DatabaseService service, ServiceWithType type,
            long timeout, TimeUnit unit,
            int maxRetries, long retryDelay) {
        if (!service.canServe(this))
            throw new IllegalStateException("Cannot serve this database!");

        switch (type) {
            case SYNC:
                service.serve(this);
                return CompletableFuture.completedFuture(null);
            case ASYNC:
                return service.serveAsync(this);
            case TIMEOUT:
                return service.serveAsyncWithTimeout(this, timeout, unit);
            case RETRY:
                return serveAsyncWithRetry(service, maxRetries, retryDelay, unit);
            default:
                throw new IllegalArgumentException("Invalid service execution type!");
        }
    }

    /**
     * Asynchronously serves a database service with retry logic in case of failure.
     * 
     * This method attempts to execute the {@link DatabaseService#serve(Database)}
     * method
     * asynchronously. If the execution fails (throws an exception), it retries the
     * operation
     * up to the specified maximum number of retries, with a delay between attempts.
     * The retries
     * will be attempted on a separate scheduler thread.
     * 
     * @param service    The {@link DatabaseService} instance that will be served.
     * @param maxRetries The maximum number of retry attempts in case of failure.
     * @param retryDelay The delay between retry attempts.
     * @param unit       The time unit for the retry delay (e.g.,
     *                   {@link TimeUnit#SECONDS}).
     * @return A {@link CompletableFuture} representing the asynchronous result of
     *         the operation.
     *         If the operation succeeds, the future will be completed with a
     *         {@code null} value.
     *         If the operation fails after the specified retries, the future will
     *         be completed
     *         exceptionally with a {@link RuntimeException}.
     * 
     * @throws IllegalArgumentException if {@code maxRetries} is less than or equal
     *                                  to 0.
     * @throws NullPointerException     if {@code service} is {@code null}.
     */
    default CompletableFuture<Void> serveAsyncWithRetry(DatabaseService service, int maxRetries,
            long retryDelay, TimeUnit unit) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        Runnable attemptExecution = new Runnable() {
            private int attempts = 0;

            @Override
            public void run() {
                DatabaseService.EXECUTOR.submit(() -> {
                    try {
                        service.serve(Database.this);
                        future.complete(null);
                    } catch (Exception e) {
                        attempts++;
                        if (attempts >= maxRetries) {
                            future.completeExceptionally(
                                    new RuntimeException("Service execution failed after retries", e));
                        } else {
                            scheduler.schedule(this, retryDelay, unit);
                        }
                    }
                });
            }
        };

        attemptExecution.run();
        future.whenComplete((result, ex) -> scheduler.shutdown());

        return future;
    }

    /**
     * Retrieves the database cycle, which defines actions when the database is
     * connected, disconnected, or restarted.
     * 
     * @return The cycle of the database connection.
     */
    U getCycle();

}
