package com.example.restaurantledger;

import javafx.collections.ObservableList;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDB {
    private static final String DB_URL = "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant";
    private static final String DB_USER = "restaurant";
    private static final String DB_PASSWORD = "csc365";
    private Connection connect; // Declare the connection variable

    public RestaurantDB() {
        // Initialize your database connection here (e.g., in the constructor)
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<LedgerEntry> getLedgerEntriesFromDatabase() {
        List<LedgerEntry> ledgerList = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             Statement statement = connection.createStatement();
             ResultSet rs = statement.executeQuery("SELECT * FROM Ledger")) {

            while (rs.next()) {
                int lid = rs.getInt("lid");
                Date ldate = rs.getDate("ldate");
                String note = rs.getString("note");
                double balance = rs.getDouble("balance");
                LedgerEntry l = new LedgerEntry(lid, ldate, note, balance);
                ledgerList.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ledgerList;
    }
}