package com.example.restaurant;

import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDB {
    static   Connection connect;

    public List<String> getDishNames(){
        List<String> dishNamesList = new ArrayList<String>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            Statement statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Dishes");
            while (rs.next()) {
                String dName = rs.getString(2);
                dishNamesList.add(dName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dishNamesList;
    }

    public List<Dish> getDishes(){
        List<Dish> dishList = new ArrayList<Dish>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            Statement statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Dishes");
            while (rs.next()) {
                int did = rs.getInt(1);
                String dName = rs.getString(2);
                String description = rs.getString(3);
                double price = rs.getDouble(4);
                String categoryString = rs.getString(5);
                Dish d = new Dish(did, dName, description, price, categoryString);
                dishList.add(d);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dishList;
    }

    public List<Employee> getEmployees(){
        List<Employee> employeeList = new ArrayList<Employee>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            Statement statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Employees");
            while (rs.next()) {
                int eid = rs.getInt(1);
                String ename = rs.getString(2);
                double earned = rs.getDouble(3);
                String role = rs.getString(4);
                Employee e = new Employee(eid, ename, earned, role);
                employeeList.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public List<Table> getTables(){
        List<Table> tableList = new ArrayList<Table>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            Statement statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Tables");
            while (rs.next()) {
                int tid = rs.getInt(1);
                int eid = rs.getInt(2);
                int seats = rs.getInt(3);
                double total = rs.getDouble(4);
                String tstateString = rs.getString(5);
                Table t = new Table(tid, eid, seats, total, tstateString);
                tableList.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableList;
    }

    /**
     * does not include running balance
     * TODO: use prepared statements
     */
    public List<LedgerEntry> getLedgerEntries(){
        List<LedgerEntry> ledgerList = new ArrayList<LedgerEntry>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            Statement statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Employees");
            while (rs.next()) {
                int lid = rs.getInt(1);
                String date = rs.getString(2);
                String note = rs.getString(3);
                double balance = rs.getDouble(4);
                LedgerEntry l = new LedgerEntry(lid, date, note, balance);
                ledgerList.add(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ledgerList;
    }

    /**
     * Assign server to a table. Set table state to Ordering
     */
    public void assignServer(Table table, Employee employee) {
        table.setTstate(TableState.ORDERING);
        int eid = employee.getEid();
        int tid = table.getTid();
        table.setEid(eid);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            String updateString =   "UPDATE Tables\n" +
                                    "SET tstate = 'ORDERING', eid = ? \n" +
                                    "WHERE tid = ?";
            PreparedStatement preparedStatement = connect.prepareStatement(updateString);
            preparedStatement.setInt(1, eid);
            preparedStatement.setInt(2, tid);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds a list of dishes associated with a table and sets the table state to Waiting.
     *
     * Modifies:
     * - Orders
     * - Tables
     */
    public void addOrders() {

    }

    /**
     * Record total of order prices in the table tuple, clears all orders associated with the table, set table state
     * to served.
     *
     * Modifies:
     * - Tables
     * - Orders
     */
    public void serveAllOrders() {

    }

    public void clearAllOrders()
    {

    }

    /**
     * Applies the tip to the table's total. Marks the table as vacant, increments employee's earned
     * attribute, and adds the total * tip quantity. Does NOT remove employee earnings from
     * balance in the ledger (that occures when employees are paid).
     *
     * Modifies:
     * - Tables
     * - Employees
     * - Ledger
     */
    public void vacateTable() {

    }

    /**
     * Decrements the employee's earned amount and records the changes in the ledger.
     *
     * Modifies:
     * - Employees
     * - Ledger
     */
    public void payEmployee() {

    }

    public static void main(String[] args) {
        RestaurantDB q = new RestaurantDB();
//        List<String> dn = q.getDishNames();
//        for(int i = 0; i < dn.size(); i++){
//            System.out.println("Dish Name = " + dn.get(i));
//        }
        List<Table> lt = q.getTables();
        List<Employee> le = q.getEmployees();

        for(int k = 0; k < lt.size(); k++){
            System.out.println(lt.get(k).toString());
        }

        q.assignServer(lt.get(0), le.get(3));

        List<Table> lt2 = q.getTables();
        for(int k = 0; k < lt2.size(); k++){
            System.out.println(lt2.get(k).toString());
        }

    }


}
