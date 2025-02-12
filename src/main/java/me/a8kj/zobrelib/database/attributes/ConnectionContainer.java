package me.a8kj.zobrelib.database.attributes;

import lombok.Getter;
import lombok.Setter;

/**
 * A container class for holding a connection of type {@link C}.
 * <p>
 * This class is used to store and retrieve a connection object. The type of the
 * connection is specified by {@link C}.
 * </p>
 * 
 * @param <C> the type of the connection (e.g., {@link java.sql.Connection})
 * @author a8kj7sea
 */
@Getter
@Setter
public class ConnectionContainer<C> {
    private C connection;
}
