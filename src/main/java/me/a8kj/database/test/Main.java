package me.a8kj.database.test;

import me.a8kj.database.Database;
import me.a8kj.database.attributes.DatabaseCredentials;
import me.a8kj.database.attributes.DatabaseCredentialsImpl;
import me.a8kj.database.cycle.DatabaseCycle;
import me.a8kj.database.impl.BasicCredentials;
import me.a8kj.database.impl.SQLDatabase;

public class Main {
    public static void main(String[] args) {

        DatabaseCredentials<BasicCredentials> credentials = new DatabaseCredentialsImpl<>();
        credentials.addCredential(BasicCredentials.URL, String.class, "jdbc:mariadb://localhost:3306/zobe?characterEncoding=latin1");
        credentials.addCredential(BasicCredentials.USERNAME, String.class, "root");
        credentials.addCredential(BasicCredentials.PASSWORD, String.class, "easypass");

        DatabaseCycle cycle = new TestCycle();

        Database<DatabaseCycle> database = new SQLDatabase<>("mariadb", cycle);
        database.setCredentials(credentials);

        database.connect();
        database.serve(new SelectService("test"));

        database.disconnect();
    }
}
