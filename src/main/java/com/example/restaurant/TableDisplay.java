package com.example.restaurant;

import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class TableDisplay extends RestaurantScene{

    VBox layout = new VBox();
    RestaurantDB restaurantDB;
    public TableDisplay(RestaurantDB restaurantDB) {
        this.restaurantDB = restaurantDB;
    }

    @Override
    public Parent getAsElement() {

//        ListView<Table> tableOfTables = new ListView<>();
//        Property<ObservableList<Table>> tableListProperty = new SimpleObjectProperty<>(restaurantDB.getTables());
//        tableOfTables.itemsProperty().bind(tableListProperty);

//        tableOfTables.setCellFactory(new Callback<ListView<Table>, ListCell<Table>>() {
//            @Override
//            public ListCell<Table> call(ListView<Table> tableListView) {
//                return new ListCell<>() {
//                    @Override
//                    protected void updateItem(Table table, boolean empty)
//                    {
//                        super.updateItem(table, empty);
//                        if(table == null || empty)
//                        {
//
//                        } else
//                        {
//                            TableCard newCard = new TableCard(table, restaurantDB);
//                            Parent newCardElement = newCard.getAsElement();
//                            this.getChildren().add(newCard.getAsElement());
//                            this.prefHeightProperty().bind(((VBox)newCardElement).heightProperty());
//                            this.prefWidthProperty().bind(((VBox)newCardElement).widthProperty());
//                        }
//                    }
//                };
//            }
//        });

        restaurantDB.getTables().forEach(t -> {
            TableCard card = new TableCard(t, restaurantDB);
            layout.getChildren().add(card.getAsElement());
        });

        layout.setFillWidth(true);

        layout.getChildren().forEach(c -> {
            c.prefWidth(RestaurantScene.xDim - 40);
            VBox.setVgrow(c, Priority.ALWAYS);
        });

        ScrollPane sp = new ScrollPane();
        sp.setMaxWidth(Double.MAX_VALUE);
        sp.setFitToWidth(true);
        sp.setContent(layout);

        return sp;
    }
}
