package me.a8kj.database;

import me.a8kj.database.attributes.ConnectionContainer;
import me.a8kj.database.attributes.CredentialsKey;
import me.a8kj.database.attributes.DatabaseCredentials;
import me.a8kj.database.cycle.DatabaseCycle;
import me.a8kj.database.enums.ConnectionStatus;
import me.a8kj.database.service.DatabaseService;

public interface Database<U extends DatabaseCycle> {

    String getName();

    ConnectionStatus getStatus();

    default boolean isConnected() {
        return getStatus() == ConnectionStatus.CONNECTED;
    }

    void setCredentials(DatabaseCredentials<? extends Enum<? extends CredentialsKey>> credentials);

    DatabaseCredentials<? extends Enum<? extends CredentialsKey>> getCredentials();

    <C> ConnectionContainer<C> getConnectionContainer();

    void updateConnectionStatus(ConnectionStatus status);

    void connect();

    void restart();

    void disconnect();

    default void serve(DatabaseService service) {
        if (!service.canServe(this))
            throw new IllegalStateException("b8rsh a3ml serve lhay al database!");
        service.serve(this);
    }

    U getCycle();

}
