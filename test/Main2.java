package me.a8kj.zobrelib.impl.test;

import me.a8kj.zobrelib.database.Database;
import me.a8kj.zobrelib.database.attributes.DatabaseCredentials;
import me.a8kj.zobrelib.database.attributes.DatabaseCredentialsImpl;
import me.a8kj.zobrelib.database.cycle.DatabaseCycle;
import me.a8kj.zobrelib.impl.HikariCPDatabaseCredentials;

/**
 * Main class that demonstrates the process of connecting to a MariaDB database,
 * performing a database operation (selecting data), and then disconnecting.
 * <p>
 * It initializes database credentials, connects to the database, performs the
 * operation,
 * and finally disconnects from the database using HikariCP connection pooling.
 * </p>
 * 
 * @author a8kj7sea
 */
public class Main2 {
    public static void main(String[] args) {

        // Initialize database credentials for MariaDB using HikariCP
        DatabaseCredentials<HikariCPDatabaseCredentials> credentials = new DatabaseCredentialsImpl<>();
        credentials.addCredential(HikariCPDatabaseCredentials.JDBC_URL, String.class,
                "jdbc:mariadb://localhost:3308/zobe?characterEncoding=latin1");
        credentials.addCredential(HikariCPDatabaseCredentials.USERNAME, String.class, "root");
        credentials.addCredential(HikariCPDatabaseCredentials.PASSWORD, String.class, "easypass");
        credentials.addCredential(HikariCPDatabaseCredentials.DRIVER_CLASS_NAME, String.class,
                "org.mariadb.jdbc.Driver");
        credentials.addCredential(HikariCPDatabaseCredentials.PORT, Integer.class, 3308); // MariaDB port
        credentials.addCredential(HikariCPDatabaseCredentials.MAX_POOL_SIZE, Integer.class, 10); // HikariCP max pool
                                                                                                 // size

        // Create a DatabaseCycle instance and set it for the database connection
        DatabaseCycle cycle = new TestCycle(); // Your custom cycle implementation

        // Create an instance of SQLDatabase (configured for MariaDB) and set the
        // credentials
        Database<DatabaseCycle> database = new me.a8kj.zobrelib.impl.SQLDatabase<>("mariadb", cycle);
        database.setCredentials(credentials);

        // Connect to the database and perform the "Select" operation
        try {
            database.connect();
            database.serve(new SelectService("test")); // Example service for selection query
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Disconnect from the database
            database.disconnect();
        }
    }
}
