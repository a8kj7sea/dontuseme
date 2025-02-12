# ZobreLib

### **Getting Started with ZobreLib**

To begin, the library currently only supports the `HikariCP` implementation for database connections. You can use it as is, but the library is designed for flexibility and easy expansion to support more database implementations in the future.

The library provides an easy way to manage databases with **extensibility** and **flexibility**. You can create your own implementations for multiple database types (SQL, NoSQL, etc.). Here's how you can do that:

### Steps to Implement Your Database

1. **Implement the `Database` Interface**  
   First, implement the `Database` interface for your specific database. This is the main interface that manages your database connection.

2. **Implement the `BaseDatabaseCycle`**  
   Then, implement the `BaseDatabaseCycle`. This allows you to control the lifecycle of the database (connect, disconnect, restart, etc.).

3. **Create a Custom `Enum` for Your Credentials**  
   You’ll need to implement a custom `Enum` to handle the credentials needed for your database connection. These credentials include items like the JDBC URL, username, password, etc. Example shown in the [test folder](https://github.com/a8kj7sea/zobrelib/blob/main/test/BasicCredentials.java).

4. **Using the Services System**  
   The service system lets you “serve” the database, either through the cycle implementation or the database instance itself. Right now, the system doesn’t support async, but that might change in the future.

### Example of Setting Up a Database Connection

Here’s an example to connect to a MariaDB database:

```java
// Initialize database credentials for MariaDB
DatabaseCredentials<BasicCredentials> credentials = new DatabaseCredentialsImpl<>();
credentials.addCredential(BasicCredentials.URL, String.class,
        "jdbc:mariadb://localhost:3308/zobe?characterEncoding=latin1");
credentials.addCredential(BasicCredentials.USERNAME, String.class, "root");
credentials.addCredential(BasicCredentials.PASSWORD, String.class, "easypass");

// Create a DatabaseCycle instance and set it for the database connection
DatabaseCycle cycle = new TestCycle();

// Create an instance of SQLDatabase (configured for MariaDB) and set the credentials
Database<DatabaseCycle> database = new SimpleSQLDatabase<>("mariadb", cycle);
database.setCredentials(credentials);

// Connect to the database and perform the "Select" operation
database.connect();
database.serve(new SelectService("test"));

// Disconnect from the database
database.disconnect();
```

### Additional Notes:

- **HikariCP Support:** Make sure to include the HikariCP dependency in your project:
  
  ```xml
  <dependency>
      <groupId>com.zaxxer</groupId>
      <artifactId>HikariCP</artifactId>
      <version>5.0.1</version> <!-- Use the latest version -->
  </dependency>
  ```
