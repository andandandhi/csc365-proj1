package com.example.restaurant;

import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import javafx.util.Callback;
import javafx.util.StringConverter;

public class TableCard extends RestaurantScene {

    private final Table table;

    private final VBox layout;

    private final RestaurantDB restaurantDB;

    private final FilteredList<Order> orders;

    private ListView<Order> orderView;

    public TableCard(Table table, RestaurantDB restaurantDB) {
        this.restaurantDB = restaurantDB; /* TODO: belongs somewhere else? */
        this.table = table;
        this.orders = new FilteredList<>(restaurantDB.getOrders(), o -> o.getTid() == table.getTid());
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
        this.orderView.setPrefHeight(200);

        this.orderView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.orderView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Order> call(ListView<Order> employeeListView) {
                ListCell<Order> newCell = new ListCell<>() {

                    @Override
                    protected void updateItem(Order order, boolean empty) {
                        super.updateItem(order, empty);

                        if (order == null || empty) {
                            return;
                        } else {

                            GridPane entry = new GridPane();
                            ButtonBar buttons = new ButtonBar();
                            Text dishName = new Text(order.getDish().getDname());
                            Button cancel = new Button("Cancel");
                            cancel.setOnAction(e -> handleOrderCancellation(order));
                            Button serve = new Button("Serve");
                            cancel.setOnAction(e -> handleOrderServe(order));
                            buttons.getButtons().add(cancel);
                            buttons.getButtons().add(serve);
                            entry.add(dishName, 0, 0);
                            entry.add(buttons, 1, 0);

                            entry.setHgap(20);

                            dishName.maxWidth(Double.MAX_VALUE);

                            ColumnConstraints col1 = new ColumnConstraints();
                            col1.setHgrow(Priority.NEVER);

                            ColumnConstraints col2_3 = new ColumnConstraints(60);
                            col2_3.setHgrow(Priority.NEVER);
                            entry.getColumnConstraints().addAll(col1, col2_3, col2_3);

                            this.getChildren().clear();
                            this.getChildren().add(entry);
                        }
                    }
                };

                newCell.paddingProperty().set(new Insets(5, 5, 5, 5));

                return newCell;
            }
        });

        this.orderView.setItems(this.orders);
    }

    public Parent getAsElement() {
        String cssLayout = """
                -fx-border-color: black;
                -fx-border-insets: 5;
                -fx-border-width: 1;
                -fx-border-style: solid;
                """;
        VBox vbox = new VBox();
        vbox.setStyle(cssLayout);
        vbox.getChildren().add(this.layout);
        return vbox;
    }

    private VBox makeTableCard() {
        VBox cardLayout = new VBox();
        cardLayout.setSpacing(4);
        cardLayout.setPadding(new Insets(4, 4, 4, 4));

        cardLayout.getChildren().addAll(
                this.makeHeader(),
                this.makeServerLabel(),
                this.makeOrderView(),
                this.makeServerAssignmentButton(),
                this.makeAddOrderButton(),
                this.makeServeAllButton(),
                this.makeVacateButton(),
                this.makeRemoveButton()
        );

        cardLayout.setFillWidth(true);
        cardLayout.getChildren().forEach(c -> HBox.setHgrow(c, Priority.ALWAYS));
        return cardLayout;
    }

    private Button makeRemoveButton() {
        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> restaurantDB.removeTable(this.table));
        removeButton.setMaxWidth(Double.MAX_VALUE);
        return removeButton;
    }

    private HBox makeHeader() {
        /* Produce a header containing the table ID and table state */
        HBox layout = new HBox();

//        Text tableName = new Text("Table "
//                + table.getTid()
//                + " - "
//                + this.table.getTstate().toString().charAt(0)
//                + this.table.getTstate().toString().substring(1).toLowerCase()
//        );

        Text headerString = new Text();
        headerString.textProperty().bind(this.table.getHeaderString());
        headerString.setFont(new Font(16));

        headerString.setTextAlignment(TextAlignment.LEFT);

        layout.getChildren().add(headerString);

        return layout;
    }
    private ListView<Order> makeOrderView() {
        return this.orderView;
    }

    private Text makeServerLabel() {
        Text serverLabel = new Text();
        serverLabel.minWidth(Region.USE_COMPUTED_SIZE);

        /* TODO: possible for a serverLabel to be created when a table doesn't have a server? */
//        serverLabel.setText("Server: "
//                + this.table.getEid() + " - "
//                + restaurantDB.getEmployees()
//                    .stream()
//                    .findFirst()
//                    .get()
//                    .getEname()
//        );

        serverLabel.textProperty().bind(table.getServerString());
        serverLabel.setTextAlignment(TextAlignment.LEFT);
        return serverLabel;
    }

    private HBox makeServerAssignmentButton()
    {
        Button commit = new Button("Assign server");

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

        ComboBox<Employee> serverSelection = new ComboBox<>();
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

        serverSelection.setItems(restaurantDB.getEmployees());

        serverSelection.setMaxWidth(Double.MAX_VALUE);

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
        serverSelection.setMaxWidth(Double.MAX_VALUE);
        layout.setMaxWidth(Double.MAX_VALUE);
        layout.getChildren().addAll(commit, serverSelection);

        return layout;
    }


    private HBox makeAddOrderButton()
    {
        Button commit = new Button("Add order");
        ComboBox<Dish> dishSelection = new ComboBox<>();

        dishSelection.setConverter(new StringConverter<Dish>() {
            @Override
            public String toString(Dish dish) {
                return dish == null ? "Select dish" : dish.getDname();
            }

            @Override
            public Dish fromString(String s) {
                return null;
            }
        });
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
        Button commit = new Button("Vacate");
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
