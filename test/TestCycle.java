package me.a8kj.zobrelib.impl.test;

import java.util.Random;

import me.a8kj.zobrelib.database.cycle.BaseDataBaseCycle;

/**
 * Test cycle implementation that interacts with the database.
 * <p>
 * This cycle is responsible for generating random data and performing insert operations
 * into the database. It includes methods to handle the connection, disconnection,
 * and restart of the database cycle.
 * </p>
 * 
 * @author a8kj7sea
 */
public class TestCycle extends BaseDataBaseCycle {
    final String table = "test";

    /**
     * Method invoked when the database connection is established.
     * It generates a random number of records and inserts them into the database.
     */
    @Override
    public void onConnect() {
        int random = new Random().nextInt(7);

        String name = "";

        for (int x = 0; x <= random; x++) {
            name = generateRandomString(random, "abcdffdfdf3v3g3ggsafafafafafaf");
            database.serve(new InsertService(name, table));
        }
        System.err.println("Create Table process done");
    }

    /**
     * Method invoked when the database connection is disconnected.
     * This method performs any necessary cleanup after disconnection.
     */
    @Override
    public void onDisconnect() {
        System.err.println("Bye ya zorbe");
    }

    /**
     * Method invoked when the database connection is restarted.
     * This method handles any actions required during a restart.
     */
    @Override
    public void onRestart() {
        System.out.println("Restart ya zobre");
    }

    /**
     * Helper method to generate a random string of a specified length using a given character set.
     * 
     * @param length The length of the random string to generate.
     * @param charset The character set used to generate the string.
     * @return The generated random string.
     */
    private String generateRandomString(int length, String charset) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int x = 0; x < length; x++) {
            stringBuilder.append(charset.charAt(new Random().nextInt(charset.length())));
        }
        return stringBuilder.toString();
    }
}
