package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;

public class Example {

    public static String CS = "jdbc:mariadb://localhost:3306/elections?user=vagrant&localSocket=/var/run/mysqld/mysqld.sock";

    private void readData(Connection c) {
        String SQL = "SELECT id, name FROM Party";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            ResultSet r = s.executeQuery();
            while (r.next()) {
                int id = r.getInt("id");
                String name = r.getString("name");
                System.out.println("Party #" + id + " is: " + name);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void readSingleData(Connection c, int id) {
        String SQL = "SELECT name FROM Party WHERE id = ?";
        try (PreparedStatement s = c.prepareStatement(SQL)) {
            s.setInt(1, id);
            ResultSet r = s.executeQuery();
            if (r.next()) {
                String name = r.getString("name");
                System.out.println(name);
            } else {
                System.out.println("No party with this ID.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        Example example = new Example();
        try (Connection c = DriverManager.getConnection(CS)) {
            if (args.length == 0) {
                example.readData(c);
            } else if (args.length == 1) {
                try {
                    int id = Integer.parseInt(args[0]);
                    example.readSingleData(c, id);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid id. Must be a number.");
                    System.exit(0);
                }
            } else {
                System.out.println("Invalid arguments.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}