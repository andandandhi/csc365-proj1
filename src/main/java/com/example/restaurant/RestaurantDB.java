package com.example.restaurant;

import javafx.beans.Observable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.time.LocalDate;
import java.sql.*;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDB {
    private Connection connect;
    private final ObservableList<Dish> dishes;
    private final ObservableList<Employee> employees;
    private final ObservableList<Table> tables;
    private final ObservableList<LedgerEntry> ledgerEntries;

    private final ObservableList<Order> orders;

    private double balance;

    private final SimpleStringProperty balanceString;

    public RestaurantDB() throws SQLException {
//        setConnect(DriverManager.getConnection(
//                "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365"));


        setConnect(DriverManager.getConnection(
                "jdbc:mysql://@csc365.c1yrbhsogize.us-west-1.rds.amazonaws.com:3306/restaurant?user=admin&password=calpolycsc365"));

        dishes = FXCollections.observableList(fetchDishes());
        employees = FXCollections.observableList(fetchEmployees());
        tables = FXCollections.observableList(fetchTables());
        ledgerEntries = FXCollections.observableList(fetchLedgerEntries());
        orders = FXCollections.observableList(fetchOrders()); /* MUST BE CALLED AFTER DISHES AND TABLES */

        balance = ledgerEntries.stream().mapToDouble(LedgerEntry::getBalance).sum();
        balanceString = new SimpleStringProperty();
        balanceString.set("$" + (int)(balance * 100)/100.0);
    }

    public Connection getConnect() {
        return connect;
    }

    public void setConnect(Connection connect) throws SQLException {
        this.connect = connect;
        this.connect.setAutoCommit(false);
    }

    public void editDish(Dish dish, String dname, String description, double price, DishType category)
    {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            PreparedStatement pStatement = getConnect().prepareStatement(
                    """
                    UPDATE Dishes
                    SET dname = ?, description = ?, price = ?, category = ?
                    WHERE did = ?
                    """
            );

            pStatement.setString(1, dname);
            pStatement.setString(2, description);
            pStatement.setDouble(3, price);
            pStatement.setString(4, category.toString());
            pStatement.setInt(5, dish.getDid());

            pStatement.executeUpdate();

            getConnect().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        dish.setDname(dname);
        dish.setDescription(description);
        dish.setPrice(price);
        dish.setCategory(category);

        this.getDishes().set(this.getDishes().indexOf(dish), dish);
    }

    public void addDish(Dish dish) {
        int did = -1;
        String dname = dish.getDname();
        String description = dish.getDescription();
        double price = dish.getPrice();
        String category = dish.getCategory().toString();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            String updateString1 =  """
                                    INSERT INTO Dishes(dname, description, price, category)
                                    VALUES( ? , ? , ? , ? )
                                    """;
            PreparedStatement preparedStatement1 = getConnect().prepareStatement(updateString1);
            preparedStatement1.setString(1, dname);
            preparedStatement1.setString(2, description);
            preparedStatement1.setDouble(3, price);
            preparedStatement1.setString(4, category);
            preparedStatement1.executeUpdate();

            String queryString2 = """
                    SELECT LAST_INSERT_ID('Dishes');
                    """;
            PreparedStatement preparedStatement2 = getConnect().prepareStatement(queryString2);
            ResultSet rs2 = preparedStatement2.executeQuery();
            rs2.next();
            rs2.getInt(1);

            getConnect().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        dish.setDid(did);
        getDishes().add(dish);
    }

    public void removeDish(Dish dish) {
        int did = dish.getDid();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            String updateString1 =  """
                                    DELETE FROM Dishes
                                    WHERE did = ?
                                    """;
            PreparedStatement preparedStatement1 = getConnect().prepareStatement(updateString1);
            preparedStatement1.setInt(1, did);
            preparedStatement1.executeUpdate();

            getConnect().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        /* Remove all orders for this dish. */
        this.orders.stream()
                .filter(o -> o.getDid() == dish.getDid())
                .forEach(this.orders::remove);
        this.dishes.remove(dish);
    }

    private List<Dish> fetchDishes() {

        List<Dish> dishList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            Statement statement = getConnect().createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Dishes");
            while (rs.next()) {
                int did = rs.getInt(1);
                String dName = rs.getString(2);
                String description = rs.getString(3);
                double price = rs.getDouble(5);
                String categoryString = rs.getString(4);
                Dish d = new Dish(did, dName, description, price, categoryString);
                dishList.add(d);
            }

            getConnect().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return dishList;
    }

    public List<Employee> fetchEmployees(){
        List<Employee> employeeList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            Statement statement = getConnect().createStatement();
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

            getConnect().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return employeeList;
    }

    public List<Table> fetchTables(){
        List<Table> tableList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            Statement statement = getConnect().createStatement();
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

            getConnect().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return tableList;
    }

    /**
     * does not include running balance
     */
    public List<LedgerEntry> fetchLedgerEntries(){
        List<LedgerEntry> ledgerList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            Statement statement = getConnect().createStatement();
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

            getConnect().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return ledgerList;
    }

    private List<Order> fetchOrders() {
        List<Order> orderList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            Statement statement = getConnect().createStatement();
            ResultSet rs = statement.executeQuery(
                    "SELECT * FROM Orders");
            while (rs.next()) {
                int oid = rs.getInt(1);
                int tid = rs.getInt(2);
                int did = rs.getInt(3);
                Dish correspondingDish = this.dishes.stream().filter(d -> d.getDid() == did).findFirst().get(); //TODO: what if no dish found?
                Order o = new Order(oid, tid, did, correspondingDish);
                orderList.add(o);
            }

            getConnect().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderList;
    }

    /**
     * Assign server to a table. Set table state to Ordering
     * 
     * Use: tid, eid
     */
    public void assignServer(Table table, Employee employee) {
        int eid = employee.getEid();
        int tid = table.getTid();

        if(employee == null)
        {
            table.changeServer(null);

            try {
                Class.forName("com.mysql.jdbc.Driver");
                getConnect().setAutoCommit(false);

                String updateString = """
                    UPDATE Tables
                    SET tstate = 'ORDERING', eid = ?
                    WHERE tid = ?
                    """;
                PreparedStatement preparedStatement = getConnect().prepareStatement(updateString);
                preparedStatement.setNull(1, Types.INTEGER);
                preparedStatement.setInt(2, tid);
                preparedStatement.executeUpdate();

                getConnect().commit();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            String updateString = """
                    UPDATE Tables
                    SET tstate = 'ORDERING', eid = ?
                    WHERE tid = ?
                    """;
            PreparedStatement preparedStatement = getConnect().prepareStatement(updateString);
            preparedStatement.setInt(1, eid);
            preparedStatement.setInt(2, tid);
            preparedStatement.executeUpdate();

            getConnect().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setTstate(TableState.ORDERING);
        table.setEid(eid);
        table.changeServer(employee);

        this.getTables().set(this.getTables().indexOf(table), table);
    }


    public void addTable(TableDisplay display) {
        int tid = -1;
        int seats = (int)(Math.random() * 10 + 2);
        double total = 0;
        TableState tstate = TableState.VACANT;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            String updateString1 =  """
                                    INSERT INTO Tables(eid, seats, total, tstate)
                                    VALUES( ? , ? , ? , ? )
                                    """;
            PreparedStatement preparedStatement1 = connect.prepareStatement(updateString1);
            preparedStatement1.setNull(1, Types.INTEGER);
            preparedStatement1.setInt(2, seats);
            preparedStatement1.setDouble(3, total);
            preparedStatement1.setString(4, tstate.toString());
            preparedStatement1.executeUpdate();

            String queryString2 = """
                    SELECT LAST_INSERT_ID('Table')""";
            PreparedStatement preparedStatement2 = connect.prepareStatement(queryString2);
            ResultSet rs2 = preparedStatement2.executeQuery();
            rs2.next();
            tid = rs2.getInt(1);

            getConnect().commit();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        Table table = new Table(tid, 0, seats, total, tstate.toString());

        TableCard newCard = new TableCard(table, display);
        display.getLayout().getChildren().add(newCard.getAsElement());
        table.setTid(tid);
        tables.add(table);
    }


    public void removeTable(Table table) {
        int tid = table.getTid();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            String updateString1 =  """
                                    DELETE FROM Tables
                                    WHERE tid = ?
                                    """;
            PreparedStatement preparedStatement1 = connect.prepareStatement(updateString1);
            preparedStatement1.setInt(1, tid);
            preparedStatement1.executeUpdate();

            getConnect().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        tables.remove(table);
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
    public void addOrder(Table table, Dish dish) {
        Order order = new Order(-1, table.getTid(), dish.getDid(), dish);
        table.setTstate(TableState.WAITING);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            String updateString1 =  """
                                   INSERT INTO Orders (tid, did)
                                   VALUES( ? , ? )
                                   """;
            PreparedStatement preparedStatement1 = getConnect().prepareStatement(updateString1);
            preparedStatement1.setInt(1, order.getTid());
            preparedStatement1.setInt(2, order.getDid());
            preparedStatement1.executeUpdate();

            String getOrderIndexString = """
                    SELECT LAST_INSERT_ID('Orders')
                    """;
            PreparedStatement preparedStatement2 = getConnect().prepareStatement(getOrderIndexString);
            ResultSet rs2 = preparedStatement2.executeQuery();
            rs2.next();
            int newOid = rs2.getInt(1);
            order.setOid(newOid);


            String updateString3 =  "UPDATE Tables\n" +
                                    "SET tstate = 'WAITING'" +
                                    "WHERE tid = ? ";
            PreparedStatement preparedStatement3 = getConnect().prepareStatement(updateString3);
            preparedStatement3.setInt(1, table.getTid());
            preparedStatement3.executeUpdate();

            getConnect().commit();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        this.orders.add(order);
    }

    /**
     * cancelOrder
     * serveOrder
     * both by order id
     *
     */
    public void cancelOrder(Order order, TableCard card) {
        int oid = order.getOid();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            String updateString1 =  """
                                    DELETE FROM Orders
                                    WHERE oid = ?;
                                    """;
            PreparedStatement preparedStatement1 = connect.prepareStatement(updateString1);
            preparedStatement1.setInt(1, oid);
            preparedStatement1.executeUpdate();

            getConnect().commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        //TODO: include javaside effects of Order when merging
        this.orders.remove(order);
        // TODO: remove from this.orders too?
        card.getOrderView().refresh();
    }

    /**
     * Record total of order prices in the table tuple, clears all orders associated with the table, set table state
     * to served.
     * Uses:
     * - tid
     * Modifies:
     * - Tables
     * - Orders
     */
    public void serveAllOrders(Table table, TableCard card) {
        int tid = table.getTid();

        double totalBill = this.orders
                .stream()
                .filter(o -> o.getTid() == table.getTid())
                .mapToDouble(o -> o.getDish().getPrice())
                .sum();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            String updateString1 = """
                    UPDATE Tables
                    SET tstate = 'SERVED', total = total + ?
                    WHERE tid = ? 
                    """;
            PreparedStatement preparedStatement1 = getConnect().prepareStatement(updateString1);
            preparedStatement1.setDouble(1, totalBill);
            preparedStatement1.setInt(2, tid);
            preparedStatement1.executeUpdate();

            String updateString2 = """
                    DELETE FROM Orders
                    WHERE tid = ?
                    """;
            PreparedStatement preparedStatement2 = getConnect().prepareStatement(updateString2);
            preparedStatement2.setInt(1, tid);
            preparedStatement2.executeUpdate();

            getConnect().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setTstate(TableState.SERVED);
        table.setTotal(table.getTotal() + totalBill);

        this.orders.removeIf(o -> o.getTid() == table.getTid());
        card.getOrderView().refresh();

        this.getTables().set(this.getTables().indexOf(table), table);
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
    public void vacateTable(Table table, double tip) {

        int tid = table.getTid();
        int lid = -1;
        Date date = Date.valueOf(LocalDate.now());
        String note = "Table " + table.getTid() + " vacated with subtotal: " +
                table.getTotal() + "; tip: " + tip;

        Employee employee = employees
                .stream()
                .filter(e -> e.getEid() == table.getEid())
                .findFirst()
                .get();
        int eid = employee.getEid();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            String updateString1 = """
                    UPDATE Tables
                    SET tstate = 'VACANT'
                    WHERE tid = ?
                    """;
            PreparedStatement preparedStatement1 = getConnect().prepareStatement(updateString1);
            preparedStatement1.setInt(1, tid);
            preparedStatement1.executeUpdate();

            String updateString2 =  "INSERT INTO Ledger(ldate, note, balance) \n" +
                                    "VALUES( ? , ? , ?)";
            PreparedStatement preparedStatement2 = getConnect().prepareStatement(updateString2);
            preparedStatement2.setDate(1, date);
            preparedStatement2.setString(2, note);
            preparedStatement2.setDouble(3, table.getTotal());
            preparedStatement2.executeUpdate();

            String queryString3 =   "SELECT LAST_INSERT_ID('Ledger')";
            PreparedStatement preparedStatement3 = getConnect().prepareStatement(queryString3);
            ResultSet rs3 = preparedStatement3.executeQuery();
            rs3.next();
            lid = rs3.getInt(1);

            String updateString4 = """
                    UPDATE Employees
                    SET earned = earned + ?
                    WHERE eid = ?""";
            PreparedStatement preparedStatement4 = getConnect().prepareStatement(updateString4);
            preparedStatement4.setDouble(1, tip);
            preparedStatement4.setInt(2, eid);
            preparedStatement4.executeUpdate();

            String updateString5 =  "UPDATE Tables " +
                                    "SET total = 0 " +
                                    "WHERE tid = ?";
            PreparedStatement preparedStatement5 = getConnect().prepareStatement(updateString5);
            preparedStatement5.setInt(1, tid);
            preparedStatement5.executeUpdate();

            getConnect().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setTstate(TableState.VACANT);
        table.changeServer(null);
        employee.addEarned(tip);

        this.getOrders().removeIf(o -> o.getTid() == table.getTid());

        this.balance += table.getTotal();
        this.balanceString.set("$" + (int) (this.balance * 100) / 100.0);

        this.getTables().add(this.getTables().indexOf(table), table);
        getLedgerEntries().add(new LedgerEntry(lid, date, note, table.getTotal()));
        table.setTotal(0);
    }

    public ObservableList<Dish> getDishes() {
        return dishes;
    }

    public ObservableList<Employee> getEmployees() {
        return employees;
    }

    public ObservableList<Table> getTables() {
        return tables;
    }

    public ObservableList<LedgerEntry> getLedgerEntries() {
        return ledgerEntries;
    }

    public ObservableList<Order> getOrders() {
        return orders;
    }

    public SimpleStringProperty balanceStringProperty() {
        return balanceString;
    }
}
