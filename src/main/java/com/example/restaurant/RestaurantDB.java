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
                String categoryString = rs.getString(5)
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

    public static void main(String[] args) {
        RestaurantDB q = new RestaurantDB();
        List<String> dn = q.getDishNames();
        for(int i = 0; i < dn.size(); i++){
            System.out.println("Dish Name = " + dn.get(i));
        }
    }

}
