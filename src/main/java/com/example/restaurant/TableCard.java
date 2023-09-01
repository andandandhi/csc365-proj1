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

    private TableDisplay tableDisplay;

    public TableCard(Table table, TableDisplay tableDisplay) {
        this.restaurantDB = tableDisplay.restaurantDB; /* TODO: belongs somewhere else? */
        this.table = table;
        this.orders = new FilteredList<>(restaurantDB.getOrders(), o -> o.getTid() == table.getTid());
        this.tableDisplay = tableDisplay;
        prepOrderView();

        VBox v = new VBox();
        String cssLayout = """
                -fx-border-color: black;
                -fx-border-insets: 5;
                -fx-border-width: 1;
                -fx-border-style: solid;
                """;
        v.setStyle(cssLayout);
        v.getChildren().add(makeTableCard());
        this.layout = v;
    }

    private void handleOrderCancellation(Order order)
    {
        this.restaurantDB.cancelOrder(order, this);
    }

//    private void handleOrderServe(Order order)
//    {
//        this.restaurantDB.serveOrder(order, this);
//    }

    private void prepOrderView() {
        this.orderView = new ListView<>();
        this.getOrderView().setPrefHeight(200);

        this.getOrderView().getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.getOrderView().setCellFactory(new Callback<>() {
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
                            // Button serve = new Button("Serve");
                            // serve.setOnAction(e -> handleOrderServe(order));
                            buttons.getButtons().add(cancel);
                            // buttons.getButtons().add(serve);
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

        this.getOrderView().setItems(this.getOrders());
    }

    public Parent getAsElement() {
        return this.layout;
    }

    private VBox makeTableCard() {
        VBox cardLayout = new VBox();
        cardLayout.setSpacing(4);
        cardLayout.setPadding(new Insets(4, 4, 4, 4));

        cardLayout.getChildren().addAll(
                this.makeHeader(),
                this.makeServerLabel(),
                this.makeTotalLabel(),
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
        removeButton.setOnAction(e -> {
            restaurantDB.removeTable(this.table);
            this.tableDisplay.getLayout().getChildren().remove(this.layout);
        });

        removeButton.setMaxWidth(Double.MAX_VALUE);
        return removeButton;
    }

    private HBox makeHeader() {
        /* Produce a header containing the table ID and table state */
        HBox layout = new HBox();

        Text headerString = new Text();
        headerString.textProperty().bind(this.table.getHeaderString());
        headerString.setFont(new Font(16));

        headerString.setTextAlignment(TextAlignment.LEFT);

        layout.getChildren().add(headerString);

        return layout;
    }
    private ListView<Order> makeOrderView() {
        return this.getOrderView();
    }

    private Text makeServerLabel() {
        Text serverLabel = new Text();
        serverLabel.minWidth(Region.USE_COMPUTED_SIZE);

        serverLabel.textProperty().bind(table.getServerString());
        serverLabel.setTextAlignment(TextAlignment.LEFT);
        return serverLabel;
    }

    private HBox makeServerAssignmentButton()
    {
        Button commit = new Button("Assign server");

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

    private Text makeTotalLabel() {
        Text label = new Text();
        label.textProperty().bind(this.table.getTotalString());
        return label;
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
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setHeaderText("Invalid input");
                a.setContentText("Tips should be decimals.");
                a.show();
                return;
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
            this.restaurantDB.serveAllOrders(this.table, this);
        });

        return serveButton;
    }

    public FilteredList<Order> getOrders() {
        return orders;
    }

    public ListView<Order> getOrderView() {
        return orderView;
    }
}
