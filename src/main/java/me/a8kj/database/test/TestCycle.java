package me.a8kj.database.test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import me.a8kj.database.cycle.BaseDataBaseCycle;

public class TestCycle extends BaseDataBaseCycle {
    final String table = "test";

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

    @Override
    public void onDisconnect() {
        
        System.err.println("Bye ya zorbe");
    }

    @Override
    public void onRestart() {

        System.out.println("Restart ya zobre");
    }

    private String generateRandomString(int length, String charset) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int x = 0; x < length; x++) {
            stringBuilder.append(charset.charAt(new Random().nextInt(charset.length())));

        }

        return stringBuilder.toString();
    }

}
