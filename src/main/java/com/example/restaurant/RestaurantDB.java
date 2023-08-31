package com.example.restaurant;

import javafx.collections.ObservableList;

import java.time.LocalDate;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDB {
    static Connection connect;

    private ObservableList<Dish> dishes;
    private ObservableList<Employee> employees;
    private ObservableList<Table> tables;
    private ObservableList<LedgerEntry> ledgerEntries;
    private ObservableList<Order> orders;
    public RestaurantDB(){

    }
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

    public List<Dish> getDishes(boolean test){
        if(test)
        {
            ArrayList<Dish> testDishes = new ArrayList<>();

            testDishes.add(new Dish(
                    1,
                    "Pizza",
                    "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                    14.99,
                    "APPETIZER"
            ));

            testDishes.add(new Dish(
                    1,
                    "Pizza",
                    "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                    14.99,
                    "ENTREE"
            ));

            testDishes.add(new Dish(
                    1,
                    "Pizza",
                    "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                    14.99,
                    "DESSERT"
            ));
            return testDishes;
        }

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

    public List<Employee> getEmployees(){
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

    public List<Table> getTables(){
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
    public List<LedgerEntry> getLedgerEntries(){
        List<LedgerEntry> ledgerList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            Statement statement = connect.createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Employees");
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
    }

    /**
     * Adds a list of dishes associated with a table and sets the table state to Waiting.
     *
     * Modifies:
     * - Orders
     * - Tables
     */
    public void addOrders(List<Order> OrderList, Table table, List<Dish> DishList) {
        table.setTstate(TableState.WAITING);
        for(int i = 0; i < DishList.size(); i++){
            OrderList.add(new Order(table.getTid(), DishList.get(i).getDid()));
        }
        for(int i = 0; i < DishList.size(); i++){
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
                preparedStatement.setInt(2, DishList.get(i).getDid());
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

        }
    }

    /**
     * Record total of order prices in the table tuple, clears all orders associated with the table, set table state
     * to served.
     * //TODO: move declarations into try statements, drop orders
     * Modifies:
     * - Tables
     * - Orders
     */
    public double serveAllOrders(List<Order> orderList, Table table) {
        /** include:
         * String updateString1 =   "DROP FROM Orders";
         *             PreparedStatement preparedStatement1 = connect.prepareStatement(updateString1);
         *             preparedStatement1.executeUpdate();
         */
        table.setTstate(TableState.SERVED);
        int tid = table.getTid();
        double totalBill = 0;
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
            String queryString =   "SELECT SUM(Dishes.price)\n" +
                    "FROM Orders\n" +
                    "JOIN Dishes ON Orders.did = Dishes.did\n" +
                    "WHERE Orders.tid = table_id;";
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

        return totalBill;
    }

    /**
     * TODO: clear for specific table
     * Modifies:
     * -Orders
     */
    public void clearAllOrders(List<Order> orderList) {
        orderList.clear();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            String updateString =   "DROP FROM Orders";
            PreparedStatement preparedStatement = connect.prepareStatement(updateString);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Applies the tip to the table's total. Marks the table as vacant, increments employee's earned
     * attribute, and adds the total * tip quantity. Does NOT remove employee earnings from
     * balance in the ledger (that occurs when employees are paid).
     *
     * Modifies:
     * - Tables
     * - Employees
     * - Ledger
     * TODO: make serveAllOrders modify table.total
     *
     * @Args: tip: input 12.3 means a $12.30 tip
     */
    public void vacateTable(Table table, List<Employee> employeeList, List<LedgerEntry> ledger, double tip) {


        int tid = table.getTid();
        table.setTstate(TableState.VACANT);

        int lid = -1;
        Date date = Date.valueOf(LocalDate.now());
        String note = "Table " + table.getTid() + " vacated with subtotal: " +
                table.getTotal() + "; tip: " + tip;

        double finalBill = table.getTotal() + tip;

        Employee employee = employeeList.get(table.getEid());
        int eid = employee.getEid();
        employee.addEarned(tip);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connect = DriverManager.getConnection(
                    "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365");
            connect.setAutoCommit(false);

            String updateString1 =  "UPDATE Tables \n" +
                                    "SET tstate = 'VACANT' \n" +
                                    "WHERE tid = ? ";
            PreparedStatement preparedStatement1 = connect.prepareStatement(updateString1);
            preparedStatement1.setInt(1, tid);
            preparedStatement1.executeUpdate();
            //connect.commit();

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
        ledger.add(new LedgerEntry(lid, date, note, finalBill));
    }

    /**
     * Decrements the employee's earned amount and records the changes in the ledger.
     *
     *
     * Modifies:
     * - Employees
     * - Ledger
     * TODO: decide Ledger or List<LedgerEntry>
     */
    public void payEmployee(Employee employee, List<LedgerEntry> ledger) {
        int eid = employee.getEid();
        String ename = employee.getEname();
        double earned = employee.getEarned();
        employee.setEarned(0);

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

        ledger.add(new LedgerEntry(lid, date, note, tipsPaid));
    }

//    public static void main(String[] args) {
//        RestaurantDB q = new RestaurantDB();
////        List<String> dn = q.getDishNames();
////        for(int i = 0; i < dn.size(); i++){
////            System.out.println("Dish Name = " + dn.get(i));
////        }
//        List<Table> lt = q.getTables();
//        List<Employee> le = q.getEmployees();
//
//        for(int k = 0; k < lt.size(); k++){
//            System.out.println(lt.get(k).toString());
//        }
//
//        q.assignServer(lt.get(0), le.get(3));
//
//        List<Table> lt2 = q.getTables();
//        for(int k = 0; k < lt2.size(); k++){
//            System.out.println(lt2.get(k).toString());
//        }
//
//    }


}
