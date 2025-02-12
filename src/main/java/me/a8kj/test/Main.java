package me.a8kj.test;

import me.a8kj.database.Database;
import me.a8kj.database.attributes.DatabaseCredentials;
import me.a8kj.database.attributes.DatabaseCredentialsImpl;
import me.a8kj.database.cycle.DatabaseCycle;
import me.a8kj.test.impl.BasicCredentials;
import me.a8kj.test.impl.SQLDatabase;

/**
 * Main class that demonstrates the process of connecting to a MariaDB database,
 * performing a database operation (selecting data), and then disconnecting.
 * <p>
 * It initializes database credentials, connects to the database, performs the
 * operation,
 * and finally disconnects from the database.
 * </p>
 * 
 * @author a8kj7sea
 */
public class Main {
    public static void main(String[] args) {

        // Initialize database credentials for MariaDB
        DatabaseCredentials<BasicCredentials> credentials = new DatabaseCredentialsImpl<>();
        credentials.addCredential(BasicCredentials.URL, String.class,
                "jdbc:mariadb://localhost:3306/zobe?characterEncoding=latin1");
        credentials.addCredential(BasicCredentials.USERNAME, String.class, "root");
        credentials.addCredential(BasicCredentials.PASSWORD, String.class, "easypass");

        // Create a DatabaseCycle instance and set it for the database connection
        DatabaseCycle cycle = new TestCycle();

        // Create an instance of SQLDatabase (configured for MariaDB) and set the
        // credentials
        Database<DatabaseCycle> database = new SQLDatabase<>("mariadb", cycle);
        database.setCredentials(credentials);

        // Connect to the database and perform the "Select" operation
        database.connect();
        database.serve(new SelectService("test"));

        // Disconnect from the database
        database.disconnect();
    }
}
