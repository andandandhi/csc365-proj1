package com.example.restaurant;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.util.List;
import java.util.StringJoiner;

class OwnerMenu extends RestaurantScene
{
    private Stage stage;
    private ListView<Dish> listView;
    private TabPane tabs;
    private Scene scene;

    public OwnerMenu(RestaurantDB restaurantDB , Stage stage)
    {
        this.stage = stage;
        this.listView = new ListView<>();
        this.dishes = FXCollections.observableList(restaurantDB.getDishes(false));

        listView.prefWidthProperty().bind(stage.widthProperty());
        listView.prefHeightProperty().bind(stage.heightProperty());
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.listView.setCellFactory(new Callback<ListView<Dish>, ListCell<Dish>>() {

            /* Overrides the call method to custom-define how cells are created */
            @Override
            public ListCell<Dish> call(ListView<Dish> dishListView) {
                /* Creates a new ListCell that displays a formatted string for the dish*/
                return new ListCell<Dish>() {
                    @Override
                    protected void updateItem(Dish item, boolean empty)
                    {
                        super.updateItem(item, empty);

                        if(empty || item == null)
                        {
                            setText(null);
                            setGraphic(null);
                        }
                        else
                        {
                            Text content = new Text(item.getDname() + " • "
                                    + "$" + item.getPrice() +
                                    "\n" + item.getDescription()
                            );

                            content.wrappingWidthProperty().bind(listView.widthProperty().add(-40));

                            setWrapText(true);

                            setGraphic(content);
                        }
                    }
                };
            }
        });

        this.listView.setItems(this.dishes.subList());

        Button addButton = new Button("Add");
        addButton.setOnAction(e -> letAddItem(this.dishes));

        /* Launch a new edit window for each dish selected for editing */
        Button editButton = new Button("Edit");
        editButton.setOnAction(e -> this.listView.getSelectionModel()
                .getSelectedIndices()
                .forEach(i -> letEditItem(this.dishes, i)));

        Button removeButton = new Button("Remove");
        removeButton.setOnAction(e -> this.listView.getSelectionModel().getSelectedItems().forEach(
                item -> this.dishes.remove(item)
        ));

        ToolBar controls = new ToolBar(
                addButton,
                editButton,
                removeButton
        );

        controls.setOrientation(Orientation.HORIZONTAL);

        VBox appetizerLayout = new VBox();
        Tab appetizerTab = new Tab("APPETIZERS");
        appetizerTab.setContent(appetizerLayout);
        this.tabs.getTabs().add(appetizerTab);
        layout.getChildren().add(this.listView);
        layout.getChildren().add(controls);
        this.scene = new Scene(layout, RestaurantScene.xDim, RestaurantScene.yDim);
    }

    /* Swaps to the menu view on the stage provided */
    public void display() {
        stage.setScene(this.scene);
        stage.show();
    }

    public void letAddItem(ObservableList<Dish> dishList) {
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
                        "APPETIZER" // TODO: update with appropriate value
                );
                // TODO: is the dish automatically displayed in the ListView
                // upon being added to the underlying ObservableList?
                dishes.add(newDish);
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

    public void letEditItem(ObservableList<Dish> dishes, int targetIndex) {
        Dish target = dishes.get(targetIndex);
        Stage addStage = new Stage();
        VBox layout = new VBox();

        GridPane gp = new GridPane();
        gp.setHgap(6);
        gp.setVgap(6);
        gp.setPadding(new Insets(6, 6, 6, 6));

        Label nameText = new Label("Name");
        TextField nameField = new TextField(target.getDname());
        nameField.maxWidth(Double.MAX_VALUE);

        Label priceText = new Label("Price");
        TextField priceField = new TextField(String.valueOf(target.getPrice()));
        priceField.prefWidth(300);

        Label descriptionText = new Label("Description");
        TextArea descriptionField = new TextArea(target.getDescription());
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

                target.setDname(nameResponse);
                target.setDescription(descriptionResponse);
                target.setPrice(price);

                dishes.set(targetIndex, target);

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

}