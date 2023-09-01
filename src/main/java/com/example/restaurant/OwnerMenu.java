package com.example.restaurant;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringJoiner;

class OwnerMenu extends RestaurantScene
{
    private final ArrayList<ListView<Dish>> views;
    private final TabPane categoryTabs;
    private ArrayList<FilteredList<Dish>> filteredLists;

    private final Pane layout;

    private final RestaurantDB restaurantDB;

    public OwnerMenu(RestaurantDB restaurantDB)
    {
        this.restaurantDB = restaurantDB;

        this.categoryTabs = new TabPane();

        this.filteredLists = new ArrayList<>();

        this.views = new ArrayList<>();

        Arrays.stream(DishType.values()).toList().forEach(dishType -> {
            FilteredList<Dish> newFilteredList = new FilteredList<>(restaurantDB.getDishes());
            newFilteredList.setPredicate(dish -> dish.getCategory() == dishType);
            this.filteredLists.add(dishType.ordinal(), newFilteredList);

            ListView<Dish> newView = new ListView<>(newFilteredList);
            newView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            this.views.add(newView);

            newView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            newView.setCellFactory(new Callback<>() {

                /* Overrides the call method to custom-define how cells are created */
                @Override
                public ListCell<Dish> call(ListView<Dish> dishListView) {
                    /* Creates a new ListCell that displays a formatted string for the dish*/
                    return new ListCell<>() {
                        @Override
                        protected void updateItem(Dish item, boolean empty) {
                            super.updateItem(item, empty);

                            if (empty || item == null) {
                                setText(null);
                                setGraphic(null);
                            } else {
                                Text content = new Text(item.getDname() + " • "
                                        + "$" + item.getPrice() +
                                        "\n" + item.getDescription()
                                );

                                content.wrappingWidthProperty().bind(newView.widthProperty().add(-40));

                                setWrapText(true);

                                setGraphic(content);
                            }
                        }
                    };
                }
            });

            String categoryString = dishType.toString();
            Tab newTab = new Tab(categoryString.charAt(0) + categoryString.substring(1).toLowerCase() + "s");
            newTab.setContent(newView);
            this.categoryTabs.getTabs().add(newTab);
        });

        this.categoryTabs.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, tab, t1) -> {
                    int categoryIndex = categoryTabs.getTabs().indexOf(tab);
                    views.get(categoryIndex).getSelectionModel().clearSelection();
                }
        );

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> letAddItem());

        /* Launch a new edit window for each dish selected for editing */
        Button editButton = new Button("Edit");

        editButton.setOnAction(e -> this.letEditItems());

        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> {
            int selectedTabInd = this.categoryTabs.getSelectionModel().getSelectedIndex();
            Dish selectedDish = this.views.get(selectedTabInd).getSelectionModel().getSelectedItem();
            restaurantDB.removeDish(selectedDish);
        });

        ToolBar controls = new ToolBar(
                addButton,
                editButton,
                removeButton
        );

        controls.setOrientation(Orientation.HORIZONTAL);

        layout = new VBox();
        layout.getChildren().add(this.categoryTabs);
        layout.getChildren().add(controls);
    }

    /* Returns the root element for the menu view, allowing it to be
     * embedded in a layout */
    public Pane getAsElement() {
        return this.layout;
    }

    public void letAddItem() {
        Stage addStage = new Stage();
        VBox layout = new VBox();

        GridPane gp = new GridPane();
        gp.setHgap(6);
        gp.setVgap(6);
        gp.setPadding(new Insets(6, 6, 6, 6));

        Label nameText = new Label("Name");
        TextField nameField = new TextField();
        nameField.maxWidth(Double.MAX_VALUE);

        Label priceText = new Label("Price");
        TextField priceField = new TextField();
        priceField.prefWidth(300);

        Label descriptionText = new Label("Description");
        TextArea descriptionField = new TextArea();
        descriptionField.prefWidth(300);
        descriptionField.setWrapText(true);

        gp.add(nameText, 0, 0);
        gp.add(nameField, 1, 0);
        gp.add(priceText, 0, 1);
        gp.add(priceField, 1, 1);
        gp.add(descriptionText, 0, 2);
        gp.add(descriptionField, 1, 2);

        /* Sets the width of the label column to the width of the description
         * label (the longest label of the three) */
        ColumnConstraints labelCol = new ColumnConstraints(descriptionText.getPrefWidth());
        labelCol.setHgrow(Priority.NEVER);
        /* Allows the second column to fill remaining space. */
        ColumnConstraints fieldCol = new ColumnConstraints();
        fieldCol.setHgrow(Priority.ALWAYS);
        gp.getColumnConstraints().add(labelCol);
        gp.getColumnConstraints().add(fieldCol);

        /* Constrains the first two rows to one line and allows
         * the description to fill remaining space. */
        RowConstraints nameRow = new RowConstraints();
        gp.getRowConstraints().add(nameRow);

        RowConstraints priceRow = new RowConstraints();
        gp.getRowConstraints().add(priceRow);

        RowConstraints descRow = new RowConstraints();
        descRow.setFillHeight(true);
        descRow.setVgrow(Priority.ALWAYS);
        gp.getRowConstraints().add(descRow);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> addStage.close());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            boolean wasError = false;
            String nameResponse = nameField.getText().trim();
            String priceResponse = priceField.getText().trim();
            String descriptionResponse = descriptionField.getText().trim();

            StringJoiner errorMessage = new StringJoiner("\n\n");

            /* Name parsing */
            if(nameResponse.length() == 0) {
                errorMessage.add("Name cannot be empty");
            }

            /* Price parsing */
            double price = -1;
            try {
                price = Double.parseDouble(priceResponse);
            } catch(Exception ex) {
                try {
                    price = Integer.parseInt(priceResponse);
                } catch (Exception ex1) {
                    wasError = true;
                }
            }

            if (price < 0 || price == 0) {
                errorMessage.add("Price must be a nonzero positive number.");
                wasError = true;
            }

            /* Description parsing */
            if(descriptionResponse.length() == 0) {
                errorMessage.add("Description cannot be empty.");
                wasError = true;
            }

            if(wasError) {
                /* Display error message without saving, keeping the dish addition window open */
                Alert a = new Alert(Alert.AlertType.INFORMATION, errorMessage.toString());
                a.setTitle("Creation issues");
                a.setHeaderText("Creation issues");
                a.show();
            }
            else {
                Dish newDish = new Dish(
                        -1, /* Negative ID for a temporary dish */
                        nameResponse,
                        descriptionResponse,
                        price,
                        DishType.values()[(this.categoryTabs.getSelectionModel().getSelectedIndex())].toString() // TODO: update with appropriate value
                );
                this.restaurantDB.addDish(newDish);
                /* Update database with new dish, closing the dish addition window */
                // TODO: add new entry to database here.
                addStage.close();
            }
        });

        ToolBar options = new ToolBar(
                cancelButton,
                saveButton
        );

        layout.getChildren().add(gp);
        layout.getChildren().add(options);

        Scene addScene = new Scene(layout, RestaurantScene.xDim, 500);
        addStage.setScene(addScene);
        addStage.show();
    }
    public void letEditItems() {
        ListView<Dish> targetView = this.views.get(this.categoryTabs.getSelectionModel().getSelectedIndex());
        ObservableList<Dish> selectedItems = targetView.getSelectionModel().getSelectedItems();

        selectedItems.forEach(this::letEditItem);
    }

    /***
     * Takes the index of a dish object to modify in the dishes ObservableList and
     * creates an editing window for that object.
     * @param dish
     */
    public void letEditItem(Dish dish) {
        Stage addStage = new Stage();
        VBox layout = new VBox();

        GridPane gp = new GridPane();
        gp.setHgap(6);
        gp.setVgap(6);
        gp.setPadding(new Insets(6, 6, 6, 6));

        Label nameText = new Label("Name");
        TextField nameField = new TextField(dish.getDname());
        nameField.maxWidth(Double.MAX_VALUE);

        Label priceText = new Label("Price");
        TextField priceField = new TextField(String.valueOf(dish.getPrice()));
        priceField.prefWidth(300);

        Label descriptionText = new Label("Description");
        TextArea descriptionField = new TextArea(dish.getDescription());
        descriptionField.prefWidth(300);
        descriptionField.setWrapText(true);

        gp.add(nameText, 0, 0);
        gp.add(nameField, 1, 0);
        gp.add(priceText, 0, 1);
        gp.add(priceField, 1, 1);
        gp.add(descriptionText, 0, 2);
        gp.add(descriptionField, 1, 2);

        /* Sets the width of the label column to the width of the description
         * label (the longest label of the three) */
        ColumnConstraints labelCol = new ColumnConstraints(descriptionText.getPrefWidth());
        labelCol.setHgrow(Priority.NEVER);
        /* Allows the second column to fill remaining space. */
        ColumnConstraints fieldCol = new ColumnConstraints();
        fieldCol.setHgrow(Priority.ALWAYS);
        gp.getColumnConstraints().add(labelCol);
        gp.getColumnConstraints().add(fieldCol);

        /* Constrains the first two rows to one line and allows
         * the description to fill remaining space. */
        RowConstraints nameRow = new RowConstraints();
        gp.getRowConstraints().add(nameRow);

        RowConstraints priceRow = new RowConstraints();
        gp.getRowConstraints().add(priceRow);

        RowConstraints descRow = new RowConstraints();
        descRow.setFillHeight(true);
        descRow.setVgrow(Priority.ALWAYS);
        gp.getRowConstraints().add(descRow);

        Button cancelButton = new Button("Cancel");
        cancelButton.setOnAction(e -> addStage.close());

        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            boolean wasError = false;
            String nameResponse = nameField.getText().trim();
            String priceResponse = priceField.getText().trim();
            String descriptionResponse = descriptionField.getText().trim();

            StringJoiner errorMessage = new StringJoiner("\n\n");

            /* Name parsing */
            if(nameResponse.length() == 0) {
                errorMessage.add("Name cannot be empty");
            }

            /* Price parsing */
            double price = -1;
            try {
                price = Double.parseDouble(priceResponse);
            } catch(Exception ex) {
                try {
                    price = Integer.parseInt(priceResponse);
                } catch (Exception ex1) {
                    wasError = true;
                }
            }

            if (price < 0 || price == 0) {
                errorMessage.add("Price must be a nonzero positive number.");
                wasError = true;
            }

            /* Description parsing */
            if(descriptionResponse.length() == 0) {
                errorMessage.add("Description cannot be empty.");
                wasError = true;
            }

            if(wasError) {
                /* Display error message without saving, keeping the dish addition window open */
                new Alert(Alert.AlertType.INFORMATION, errorMessage.toString()).show();
            }
            else {
                this.restaurantDB.editDish(dish, nameResponse, descriptionResponse, price, dish.getCategory());
                addStage.close();
            }
        });

        ToolBar options = new ToolBar(
                cancelButton,
                saveButton
        );

        layout.getChildren().add(gp);
        layout.getChildren().add(options);

        Scene addScene = new Scene(layout, RestaurantScene.xDim, 500);
        addStage.setScene(addScene);
        addStage.show();
    }

}