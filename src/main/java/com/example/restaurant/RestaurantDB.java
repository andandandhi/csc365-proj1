package com.example.restaurant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDB {
    static Connection connect;
    public final ObservableList<Dish> dishes;
    public final ObservableList<Employee> employees;
    public final ObservableList<Table> tables;
    public final ObservableList<LedgerEntry> ledgerEntries;
    
    public RestaurantDB() throws SQLException {
        connect = DriverManager.getConnection(
                "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");

        dishes = FXCollections.observableList(fetchDishes());
        employees = FXCollections.observableList(fetchEmployees());
        tables = FXCollections.observableList(fetchTables());
        ledgerEntries = FXCollections.observableList(fetchLedgerEntries());
    }

    public void editDish(Dish dish, String dname, String description, double price, DishType category)
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect.setAutoCommit(false);

            PreparedStatement pStatement = connect.prepareStatement(
                    """
                    UPDATE Dishes
                    SET dname = ?, description = ?, price = ?, category = ?
                    """
            );

            pStatement.setString(1, dname);
            pStatement.setString(2, description);
            pStatement.setDouble(3, price);
            pStatement.setString(4, category.toString());

            pStatement.executeUpdate();

            connect.commit();
            connect.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        dish.setDname(dname);
        dish.setDescription(description);
        dish.setPrice(price);
        dish.setCategory(category);

        this.dishes.add(this.dishes.indexOf(dish), dish);
    }

    public void addDish(Dish dish) {
        int did = -1;
        String dname = dish.getDname();
        String description = dish.getDescription();
        double price = dish.getPrice();
        String category = dish.getCategory().toString();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            String updateString1 =  """
                                    INSERT INTO Dishes(dname, description, price, category)
                                    VALUES( ? , ? , ? , ? )
                                    """;
            PreparedStatement preparedStatement1 = connect.prepareStatement(updateString1);
            preparedStatement1.setString(1, dname);
            preparedStatement1.setString(2, description);
            preparedStatement1.setDouble(3, price);
            preparedStatement1.setString(4, category);
            preparedStatement1.executeUpdate();

            String queryString2 = """
                    SELECT LAST_INSERT_ID """;
            PreparedStatement preparedStatement2 = connect.prepareStatement(queryString2);
            ResultSet rs2 = preparedStatement2.executeQuery();
            rs2.getInt(1);
            connect.commit();
            connect.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        dish.setDid(did);
        dishes.add(dish);
    }

    public void removeDish(Dish dish) {
        int did = dish.getDid();
        String dname = dish.getDname();
        String description = dish.getDescription();
        double price = dish.getPrice();
        String category = dish.getCategory().toString();

        List<Dish> dishList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            String updateString1 =  """
                                    DELETE FROM Dishes
                                    WHERE did = ?
                                    """;
            PreparedStatement preparedStatement1 = connect.prepareStatement(updateString1);
            preparedStatement1.setInt(1, did);
            preparedStatement1.executeUpdate();

            connect.commit();
            connect.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        dishes.remove(dish);
    }

    public void addEmployee(Employee employee) {

    }

    public void removeEmployee(Employee employee) {

    }

    /**
     * Modifies:
     * - LedgerEntries
     * - Employees
     */
    public void addOwed(Employee employee, double addition) {

    }

    public void addLedgerEntry(LedgerEntry ledgerEntry) {

    }

    public void removeOrder(Table table, int index) {

    }

    public void removeTable(Table table) {

    }

    public void addTable(Table table) {

    }

    private List<Dish> fetchDishes() {

        List<Dish> dishList = new ArrayList<>();
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

    public List<Employee> fetchEmployees(){
        List<Employee> employeeList = new ArrayList<>();
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

    public List<Table> fetchTables(){
        // TODO: load order data into tables' dishes (ObservableList) property
        List<Table> tableList = new ArrayList<>();
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
    public List<LedgerEntry> fetchLedgerEntries(){
        List<LedgerEntry> ledgerList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            Statement statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Ledger");
            while (rs.next()) {
                int lid = rs.getInt(1);
                Date date = rs.getDate(2);
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
     * 
     * Use: tid, eid
     */
    public void assignServer(Table table, Employee employee) {
        int eid = employee.getEid();
        int tid = table.getTid();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            String updateString = """
                    UPDATE Tables
                    SET tstate = 'ORDERING', eid = ?
                    WHERE tid = ?
                    """;
            PreparedStatement preparedStatement = connect.prepareStatement(updateString);
            preparedStatement.setInt(1, eid);
            preparedStatement.setInt(2, tid);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setTstate(TableState.ORDERING);
        table.setEid(eid);

        this.tables.add(this.tables.indexOf(table), table);
    }

    /**
     * Adds a list of dishes associated with a table and sets the table state to Waiting.
     *
     * Uses:
     * - Dishes
     * - tid
     * Modifies:
     * - Orders
     * - Tables
     */
    public void addOrders(Table table, List<Dish> dishList) {
        table.setTstate(TableState.WAITING);
        for(int i = 0; i < dishList.size(); i++){
            table.getOrders().add(dishList.get(i));
        }
        for(int i = 0; i < dishList.size(); i++){
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connect = DriverManager.getConnection(
                        "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
                String updateString =  """
                                       INSERT INTO Orders
                                       VALUES( ? , ? )
                                       """;
                PreparedStatement preparedStatement = connect.prepareStatement(updateString);
                preparedStatement.setInt(1, table.getTid());
                preparedStatement.setInt(2, dishList.get(i).getDid());
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Class.forName("com.mysql.jdbc.Driver");
                connect = DriverManager.getConnection(
                        "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
                String updateString =   "UPDATE Tables\n" +
                                        "SET State = 'WAITING'" +
                                        "WHERE tid = ? ";
                PreparedStatement preparedStatement = connect.prepareStatement(updateString);
                preparedStatement.setInt(1, table.getTid());
                preparedStatement.executeUpdate();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            this.tables.add(this.tables.indexOf(table), table);
        }
    }

    /**
     * Record total of order prices in the table tuple, clears all orders associated with the table, set table state
     * to served.
     * //TODO: move declarations into try statements, drop orders
     * Uses:
     * - tid
     * Modifies:
     * - Tables
     * - Orders
     */
    public void serveAllOrders(Table table) {
        /** include:
         * String updateString1 =   "DROP FROM Orders";
         *             PreparedStatement preparedStatement1 = connect.prepareStatement(updateString1);
         *             preparedStatement1.executeUpdate();
         */

        double totalBill = table.getOrders().stream().mapToDouble(Dish::getPrice).sum();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            String updateString =
                    "UPDATE Tables\n" +
                    "SET State = 'SERVED'" +
                    "WHERE tid = ? ;";
            PreparedStatement preparedStatement = connect.prepareStatement(updateString);
            preparedStatement.setInt(1, table.getTid());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            String queryString = """
                    SELECT SUM(Dishes.price)
                    FROM Orders
                    JOIN Dishes ON Orders.did = Dishes.did
                    WHERE Orders.tid = table_id;
                    """;
            PreparedStatement preparedStatement = connect.prepareStatement(queryString);
            //preparedStatement.setInt(1, tid);
            ResultSet rs = preparedStatement.executeQuery(
                    "SELECT * FROM Employees");
            while (rs.next()) {
                totalBill = rs.getDouble(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setTstate(TableState.SERVED);
        table.setTotal(totalBill);

        this.tables.add(this.tables.indexOf(table), table);
    }



    /**
     * Applies the tip to the table's total. Marks the table as vacant, increments employee's earned
     * attribute, and adds the total * tip quantity. Does NOT remove employee earnings from
     * balance in the ledger (that occurs when employees are paid).
     *
     * Uses:
     * - tid
     * Modifies:
     * - Tables
     * - Employees
     * - Ledger
     *
     * @Args: tip: input 12.3 means a $12.30 tip
     */
    public void vacateTable(Table table, List<Employee> employeeList, double tip) {

        int tid = table.getTid();
        int lid = -1;
        Date date = Date.valueOf(LocalDate.now());
        String note = "Table " + table.getTid() + " vacated with subtotal: " +
                table.getTotal() + "; tip: " + tip;

        double finalBill = table.getTotal() + tip;

        Employee employee = employeeList.get(table.getEid());
        int eid = employee.getEid();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            connect.setAutoCommit(false);

            String updateString1 = """
                    UPDATE Tables\s
                    SET tstate = 'VACANT'\s
                    WHERE tid = ?\s
                    """;
            PreparedStatement preparedStatement1 = connect.prepareStatement(updateString1);
            preparedStatement1.setInt(1, tid);
            preparedStatement1.executeUpdate();

            String updateString2 =  "INSERT INTO Ledger(ldate, note, balance) \n" +
                                    "VALUES( ? , ? , ?)";
            PreparedStatement preparedStatement2 = connect.prepareStatement(updateString2);
            preparedStatement2.setDate(1, date);
            preparedStatement2.setString(2, note);
            preparedStatement2.setDouble(3, finalBill);
            preparedStatement2.executeUpdate();

            String queryString3 =   "SELECT LAST_INSERT_ID";
            PreparedStatement preparedStatement3 = connect.prepareStatement(queryString3);
            ResultSet rs3 = preparedStatement3.executeQuery();
            lid = rs3.getInt(1);

            String updateString4 =  "UPDATE Employees \n" +
                                    "SET earned = earned + ? \n" +
                                    "WHERE eid = ?";
            PreparedStatement preparedStatement4 = connect.prepareStatement(updateString4);
            preparedStatement4.setDouble(1, tip);
            preparedStatement4.setInt(2, eid);
            ResultSet rs4 = preparedStatement4.executeQuery();
            lid = rs4.getInt(1);

            connect.commit();

            connect.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setTstate(TableState.VACANT);
        employee.addEarned(tip);

        this.tables.add(this.tables.indexOf(table), table);
        this.employees.add(this.employees.indexOf(employee), employee);

        ledgerEntries.add(new LedgerEntry(lid, date, note, finalBill));
    }

    /**
     * Decrements the employee's earned amount and records the changes in the ledger.
     *
     * Uses:
     * - eid
     * Modifies:
     * - Employees
     * - Ledger
     */
    public void payEmployee(Employee employee, double amount) throws RuntimeException {
        int eid = employee.getEid();
        String ename = employee.getEname();
        double earned = employee.getEarned();

        int lid = -1;
        double tipsPaid = earned * -1;
        Date date = Date.valueOf(LocalDate.now());
        String note = "Employee(" + eid + ", " + ename + ") was paid on " + date +
                ", for: $" + earned;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            connect.setAutoCommit(false);

            String updateString1 = """
                    UPDATE Employees
                    SET earned = 0\s
                    WHERE eid = ?
                    """;
            PreparedStatement preparedStatement1 = connect.prepareStatement(updateString1);
            preparedStatement1.setInt(1, eid);
            preparedStatement1.executeUpdate();

            String updateString2 =  """
                                    INSERT INTO Ledger(ldate, note, balance) \n" +
                                    VALUES( ? , ? , ?)
                                    """;
            PreparedStatement preparedStatement2 = connect.prepareStatement(updateString2);
            preparedStatement2.setDate(1, date);
            preparedStatement2.setString(2, note);
            preparedStatement2.setDouble(3, tipsPaid);
            preparedStatement2.executeUpdate();

            String queryString3 =   "SELECT LAST_INSERT_ID";
            PreparedStatement preparedStatement3 = connect.prepareStatement(queryString3);
            ResultSet rs3 = preparedStatement3.executeQuery();
            lid = rs3.getInt(1);

            connect.commit();
            connect.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        employee.setEarned(employee.getEarned() + amount);
        this.employees.add(this.employees.indexOf(employee), employee);

        ledgerEntries.add(new LedgerEntry(lid, date, note, tipsPaid));
    }
}
