package me.a8kj.database.attributes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConnectionContainer<C> {
    private C connection;
}
