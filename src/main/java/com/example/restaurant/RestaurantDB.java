package com.example.restaurant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    
    public RestaurantDB() throws SQLException {
        setConnect(DriverManager.getConnection(
                "jdbc:mysql://ambari-node5.csc.calpoly.edu:3306/restaurant?user=restaurant&password=csc365"));

        dishes = FXCollections.observableList(fetchDishes());
        employees = FXCollections.observableList(fetchEmployees());
        tables = FXCollections.observableList(fetchTables());
        ledgerEntries = FXCollections.observableList(fetchLedgerEntries());
        orders = FXCollections.observableList(fetchOrders()); /* MUST BE CALLED AFTER DISHES AND TABLES */
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

    public void addEmployee(Employee employee) {

    }

    public void removeEmployee(Employee employee) {
        // TODO: what if employee is removed while serving table?
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

    public void removeTable(Table table) {

    }

    public void addTable(Table table) {
        
    }

    private List<Dish> fetchDishes() {

        List<Dish> dishList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dishList;
    }

    public List<Employee> fetchEmployees(){
        List<Employee> employeeList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ledgerList;
    }

    private List<Order> fetchOrders() {
        List<Order> orderList = new ArrayList<>();
        try {
            Class.forName("com.mysql.jdbc.Driver");
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
                String updateString = """
                    UPDATE Tables
                    SET tstate = 'ORDERING', eid = ?
                    WHERE tid = ?
                    """;
                PreparedStatement preparedStatement = getConnect().prepareStatement(updateString);
                preparedStatement.setNull(1, Types.INTEGER);
                preparedStatement.setInt(2, tid);
                preparedStatement.executeUpdate();
                this.connect.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String updateString = """
                    UPDATE Tables
                    SET tstate = 'ORDERING', eid = ?
                    WHERE tid = ?
                    """;
            PreparedStatement preparedStatement = getConnect().prepareStatement(updateString);
            preparedStatement.setInt(1, eid);
            preparedStatement.setInt(2, tid);
            preparedStatement.executeUpdate();
            this.connect.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setTstate(TableState.ORDERING);
        table.setEid(eid);
        table.changeServer(employee);

        this.getTables().set(this.getTables().indexOf(table), table);
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
            String updateString =  """
                                   INSERT INTO Orders
                                   (tid, did)
                                   VALUES( ? , ? )
                                   """;
            PreparedStatement preparedStatement = getConnect().prepareStatement(updateString);
            preparedStatement.setInt(1, order.getTid());
            preparedStatement.setInt(2, order.getDid());
            preparedStatement.executeUpdate();

            String getOrderIndexString = """
                    SELECT LAST_INSERT_ID('Orders')
                    """;
            PreparedStatement preparedStatement1 = getConnect().prepareStatement(getOrderIndexString);
            ResultSet rs = preparedStatement1.executeQuery();
            rs.next();
            int newOid = rs.getInt(1);
            order.setOid(newOid);

            Class.forName("com.mysql.jdbc.Driver");
            String updateString2 =  "UPDATE Tables\n" +
                                    "SET tstate = 'WAITING'" +
                                    "WHERE tid = ? ";
            PreparedStatement preparedStatement2 = getConnect().prepareStatement(updateString2);
            preparedStatement2.setInt(1, table.getTid());
            preparedStatement2.executeUpdate();

            this.connect.commit();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        this.orders.add(order);

        this.getTables().set(this.getTables().indexOf(table), table);
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

        double totalBill = this.orders
                .stream()
                .filter(o -> o.getTid() == table.getTid())
                .mapToDouble(o -> o.getDish().getPrice())
                .sum();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            String updateString =
                    "UPDATE Tables\n" +
                    "SET tstate = 'SERVED'" +
                    "WHERE tid = ? ;";
            PreparedStatement preparedStatement = getConnect().prepareStatement(updateString);
            preparedStatement.setInt(1, table.getTid());
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String queryString = """
                    SELECT SUM(Dishes.price)
                    FROM Orders
                    JOIN Dishes ON Orders.did = Dishes.did
                    WHERE Orders.tid = table_id;
                    """;
            PreparedStatement preparedStatement = getConnect().prepareStatement(queryString);
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

        double finalBill = table.getTotal() + tip;

        /* TODO: Will break if employees are removed in the middle of the list */
        Employee employee = employees.get(table.getEid());
        int eid = employee.getEid();

        try {
            Class.forName("com.mysql.jdbc.Driver");
            getConnect().setAutoCommit(false);

            String updateString1 = """
                    UPDATE Tables\s
                    SET tstate = 'VACANT'\s
                    WHERE tid = ?\s
                    """;
            PreparedStatement preparedStatement1 = getConnect().prepareStatement(updateString1);
            preparedStatement1.setInt(1, tid);
            preparedStatement1.executeUpdate();

            String updateString2 =  "INSERT INTO Ledger(ldate, note, balance) \n" +
                                    "VALUES( ? , ? , ?)";
            PreparedStatement preparedStatement2 = getConnect().prepareStatement(updateString2);
            preparedStatement2.setDate(1, date);
            preparedStatement2.setString(2, note);
            preparedStatement2.setDouble(3, finalBill);
            preparedStatement2.executeUpdate();

            String queryString3 =   "SELECT LAST_INSERT_ID('Ledger')";
            PreparedStatement preparedStatement3 = getConnect().prepareStatement(queryString3);
            ResultSet rs3 = preparedStatement3.executeQuery();
            rs3.next();
            lid = rs3.getInt(1);

            String updateString4 =  "UPDATE Employees \n" +
                                    "SET earned = earned + ? \n" +
                                    "WHERE eid = ?";
            PreparedStatement preparedStatement4 = getConnect().prepareStatement(updateString4);
            preparedStatement4.setDouble(1, tip);
            preparedStatement4.setInt(2, eid);
            preparedStatement4.executeUpdate();

            getConnect().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

        table.setTstate(TableState.VACANT);
        table.changeServer(null);
        employee.addEarned(tip);

        this.getOrders().stream()
                .filter(o -> o.getTid() == o.getTid()).forEach(this.orders::remove);

        this.getTables().add(this.getTables().indexOf(table), table);
        this.getEmployees().add(this.getEmployees().indexOf(employee), employee);

        getLedgerEntries().add(new LedgerEntry(lid, date, note, finalBill));
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
            getConnect().setAutoCommit(false);

            String updateString1 = """
                    UPDATE Employees
                    SET earned = 0\s
                    WHERE eid = ?
                    """;
            PreparedStatement preparedStatement1 = getConnect().prepareStatement(updateString1);
            preparedStatement1.setInt(1, eid);
            preparedStatement1.executeUpdate();

            String updateString2 =  """
                                    INSERT INTO Ledger(ldate, note, balance) \n" +
                                    VALUES( ? , ? , ?)
                                    """;
            PreparedStatement preparedStatement2 = getConnect().prepareStatement(updateString2);
            preparedStatement2.setDate(1, date);
            preparedStatement2.setString(2, note);
            preparedStatement2.setDouble(3, tipsPaid);
            preparedStatement2.executeUpdate();

            String queryString3 =   "SELECT LAST_INSERT_ID('Ledger')";
            PreparedStatement preparedStatement3 = getConnect().prepareStatement(queryString3);
            ResultSet rs3 = preparedStatement3.executeQuery();
            lid = rs3.getInt(1);

            getConnect().commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        employee.setEarned(employee.getEarned() + amount);
        this.getEmployees().add(this.getEmployees().indexOf(employee), employee);

        getLedgerEntries().add(new LedgerEntry(lid, date, note, tipsPaid));
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

    public void serveOrder(Order order) {

    }

    public void cancelOrder(Order order) {

    }
}
