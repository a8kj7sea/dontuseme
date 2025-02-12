package me.a8kj.zobrelib.database.service;

import java.util.concurrent.*;
import me.a8kj.zobrelib.database.Database;

/**
 * Interface for defining database services that can interact with a database.
 * <p>
 * A database service provides operations that can be performed on a database.
 * The service defines whether it can be executed on a particular database
 * through the {@link #canServe(Database)} method
 * and performs its actions using the {@link #serve(Database)} method.
 * </p>
 *
 * @author a8kj7sea
 */
public interface DatabaseService {

    ExecutorService EXECUTOR = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors() * 2);

    /**
     * Executes the service synchronously on the provided database.
     *
     * @param database the database on which the service will operate.
     */
    void serve(Database<?> database);

    /**
     * Executes the service asynchronously on the provided database using a custom
     * thread pool.
     *
     * @param database the database on which the service will operate.
     * @return a CompletableFuture representing the service execution.
     */
    default CompletableFuture<Void> serveAsync(Database<?> database) {
        return CompletableFuture.runAsync(() -> serve(database), EXECUTOR);
    }

    /**
     * Executes the service asynchronously with a timeout.
     *
     * @param database the database on which the service will operate.
     * @param timeout  the maximum execution time.
     * @param unit     the time unit of the timeout.
     * @return a CompletableFuture representing the service execution.
     */
    default CompletableFuture<Void> serveAsyncWithTimeout(Database<?> database, long timeout, TimeUnit unit) {
        CompletableFuture<Void> future = CompletableFuture.runAsync(() -> serve(database), EXECUTOR);
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

        return future.applyToEither(
                CompletableFuture.runAsync(() -> {
                    try {
                        Thread.sleep(unit.toMillis(timeout));
                        if (!future.isDone()) {
                            future.completeExceptionally(new TimeoutException("Service execution timed out"));
                        }
                    } catch (InterruptedException ignored) {
                    }
                }, scheduler),
                res -> res);
    }

    /**
     * Executes the service asynchronously with retry logic.
     *
     * @param database   the database on which the service will operate.
     * @param maxRetries the maximum number of retry attempts.
     * @param retryDelay the delay between retries.
     * @param unit       the time unit of the retry delay.
     * @return a CompletableFuture representing the service execution.
     */
    default CompletableFuture<Void> serveAsyncWithRetry(Database<?> database, int maxRetries, long retryDelay,
            TimeUnit unit) {
        return CompletableFuture.runAsync(() -> {
            int attempts = 0;
            while (attempts < maxRetries) {
                try {
                    serve(database);
                    return;
                } catch (Exception e) {
                    attempts++;
                    if (attempts >= maxRetries) {
                        throw new CompletionException("Service failed after " + maxRetries + " attempts", e);
                    }
                    try {
                        // hoon lw 9ar backoff
                        Thread.sleep(unit.toMillis(retryDelay) * attempts);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }, EXECUTOR);
    }

    /**
     * Checks whether this service can be performed on the provided database.
     *
     * @param database the database to check compatibility with.
     * @return true if the service can be executed on the database, false otherwise.
     */
    boolean canServe(Database<?> database);
}
