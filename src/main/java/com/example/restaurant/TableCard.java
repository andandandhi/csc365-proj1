package com.example.restaurant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Arrays;

public class TableCard extends RestaurantScene {

    private final Table table;

    private final VBox layout;

    private final RestaurantDB restaurantDB;

    private final FilteredList<Order> orders;

    private ListView<Order> orderView;

    public TableCard(Table table, RestaurantDB restaurantDB) {
        this.restaurantDB = restaurantDB; /* TODO: belongs somewhere else? */
        this.table = table;
        /* TODO: uncomment when Andrew writes restaurantDB.getOrdersForTable() */
        // this.orders = FXCollections.observableList(restaurantDB.getOrdersForTable());
        this.orders = new FilteredList<Order>(restaurantDB.getOrders());
        this.orders.setPredicate(o -> o.getTid() == table.getTid());
        prepOrderView();

        this.layout = makeTableCard();
    }

    private void handleOrderCancellation(Order order)
    {
        this.restaurantDB.cancelOrder(order);
    }

    private void handleOrderServe(Order order)
    {
        this.restaurantDB.serveOrder(order);
    }

    private void prepOrderView() {
        this.orderView = new ListView<>();

        this.orderView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.orderView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Order> call(ListView<Order> employeeListView) {
                return new ListCell<>() {

                    @Override
                    protected void updateItem(Order order, boolean empty) {
                        super.updateItem(order, empty);


                        if (order == null || empty) {
                            this.setText("Missing dish");
                        } else {

                            HBox entry = new HBox();
                            Text dishName = new Text(order.getDish().getDname());
                            Button cancel = new Button("Cancel");
                            cancel.setOnAction(e -> handleOrderCancellation(order));
                            Button serve = new Button("Serve");
                            cancel.setOnAction(e -> handleOrderServe(order));

                            entry.getChildren().addAll(
                                    dishName,
                                    serve,
                                    cancel
                            );
                        }
                    }
                };
            }
        });

        this.orderView.setItems(this.orders);
    }

    public Parent getAsElement() {
        return this.layout;
    }

    private VBox makeTableCard() {
        VBox cardLayout = new VBox();

        cardLayout.fillWidthProperty().set(true);

        switch (this.table.getTstate()) {
            case VACANT -> {
                cardLayout.getChildren().addAll(
                        this.makeHeader(),
                        this.makeServerAssignmentButton(),
                        this.makeRemoveButton()
                );
            }

            case ORDERING, SERVED -> {
                cardLayout.getChildren().addAll(
                        this.makeHeader(),
                        this.makeServerLabel(),
                        this.makeOrderView(),
                        this.makeAddOrderButton(),
                        this.makeVacateButton(),
                        this.makeRemoveButton()
                );
            }

            case WAITING -> {
                cardLayout.getChildren().addAll(
                        this.makeHeader(),
                        this.makeServerLabel(),
                        this.makeOrderView(),
                        this.makeAddOrderButton(),
                        this.makeServeAllButton(),
                        this.makeVacateButton(),
                        this.makeRemoveButton()
                );
            }

            default -> {
                Text errorText = new Text();
                errorText.setText("Table " + this.table.getTid() + " has unknown state.");
                /* TODO: what to do in the default case? */
                cardLayout.getChildren().addAll(
                        errorText
                );
            }
        }

        cardLayout.getChildren().forEach(c -> HBox.setHgrow(c, Priority.ALWAYS));
        cardLayout.fillWidthProperty().set(true);
        return cardLayout;
    }

    private Button makeRemoveButton() {
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> restaurantDB.removeTable(this.table));
        return removeButton;
    }

    private HBox makeHeader() {
        /* Produce a header containing the table ID and table state */
        HBox layout = new HBox();

        Text tableName = new Text("Table " + table.getTid());
        tableName.setTextAlignment(TextAlignment.LEFT);

        Text stateLabel = new Text( /* Capitalize appropriately */
                TableState.VACANT.toString().charAt(0)
                        + TableState.VACANT.toString().substring(1).toLowerCase()
        );
        stateLabel.setTextAlignment(TextAlignment.RIGHT);

        layout.getChildren().addAll(tableName, stateLabel);

        return layout;
    }
    private ListView<Order> makeOrderView() {
        return this.orderView;
    }

    private Text makeServerLabel() {
        Text serverLabel = new Text();
        serverLabel.minWidth(Region.USE_COMPUTED_SIZE);

        /* TODO: possible for a serverLabel to be created when a table doesn't have a server? */
        serverLabel.setText("Server: "
                + this.table.getEid() + ": "
                + restaurantDB.getEmployees()
                    .stream()
                    .findFirst()
                    .get()
                    .getEname()
        );
        /* TODO: get order names */

        serverLabel.setTextAlignment(TextAlignment.LEFT);

        return serverLabel;
    }

    private HBox makeServerAssignmentButton()
    {
        Button commit = new Button("Assign server");
        commit.minWidth(Region.USE_COMPUTED_SIZE);

        ComboBox<Employee> serverSelection = new ComboBox<>();
        serverSelection.minWidth(Region.USE_COMPUTED_SIZE);
        serverSelection.setConverter(new StringConverter<Employee>() {
            @Override
            public String toString(Employee employee) {
                return employee == null ? "Select server" : employee.getEid() + ": " + employee.getEname();
            }

            @Override
            public Employee fromString(String s) {
                return null;
            }
        });
//        serverSelection.setCellFactory(new Callback<ListView<Employee>, ListCell<Employee>>() {
//            @Override
//            public ListCell<Employee> call(ListView<Employee> employeeListView) {
//                return new ListCell<>() {
//                  @Override
//                  protected void updateItem(Employee employee, boolean empty)
//                  {
//                      super.updateItem(employee, empty);
//
//                      if(employee == null || empty)
//                      {
//                          this.setText("Missing employee");
//                      } else {
//                          this.setText(employee.getEid() + " - " + employee.getEname());
//                      }
//                  }
//                };
//            }
//        });

        serverSelection.setItems(this.restaurantDB.getEmployees());

        /* Upon pressing the commit button, the employee assignment event should be
        * executed. */
        commit.setOnAction(x -> {
            Employee selectedEmployee = serverSelection.getSelectionModel().getSelectedItem();
            if(selectedEmployee != null)
                restaurantDB.assignServer(
                        this.table,
                        selectedEmployee
                );
        });

        HBox layout = new HBox();
        layout.getChildren().addAll(commit, serverSelection);
        return layout;
    }


    private HBox makeAddOrderButton()
    {
        Button commit = new Button();
        ComboBox<Dish> dishSelection = new ComboBox<>();
        dishSelection.setCellFactory(new Callback<ListView<Dish>, ListCell<Dish>>() {
            @Override
            public ListCell<Dish> call(ListView<Dish> employeeListView) {
                return new ListCell<>() {
                    @Override
                    protected void updateItem(Dish dish, boolean empty)
                    {
                        super.updateItem(dish, empty);

                        if(dish == null || empty)
                        {
                            this.setText("Missing dish");
                        } else {
                            this.setText(dish.getDname());
                        }
                    }
                };
            }
        });

        /* Add all dishes to this ComboBox */
        dishSelection.setItems(restaurantDB.getDishes());

        commit.setOnAction(x -> {
            Dish dishToAdd = dishSelection.getSelectionModel().getSelectedItem();
            if(dishToAdd != null) {
                restaurantDB.addOrder(this.table, dishToAdd);
            }
        });

        HBox layout = new HBox();
        layout.getChildren().addAll(commit, dishSelection);
        return layout;
    }

    private HBox makeVacateButton()
    {
        Button commit = new Button();
        TextField tipField = new TextField();

        commit.setOnAction(e -> {
            String tipResponse = tipField.getText().trim();

            if(tipResponse.length() == 0)
            {
                restaurantDB.vacateTable(this.table, 0);
            }

            double tip = 0;
            try {
                tip = Double.parseDouble(tipResponse);
            }
            catch (RuntimeException r)
            {
                /* TODO: print alert message */
            }

            restaurantDB.vacateTable(this.table, tip);
        });

        HBox layout = new HBox();
        layout.getChildren().addAll(commit, tipField);
        return layout;
    }

    private Button makeServeAllButton()
    {
        Button serveButton = new Button("Serve all orders");

        serveButton.setOnAction(e -> {
            this.restaurantDB.serveAllOrders(this.table);
        });

        return serveButton;
    }
}
