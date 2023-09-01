package com.example.restaurant;

import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.EventListener;

public class TableDisplay extends RestaurantScene{

    private VBox layout = new VBox();

    RestaurantDB restaurantDB;
    public TableDisplay(RestaurantDB restaurantDB) {
        this.restaurantDB = restaurantDB;
    }

    @Override
    public Parent getAsElement() {

        restaurantDB.getTables().forEach(t -> {
            TableCard card = new TableCard(t, this);
            getLayout().getChildren().add(card.getAsElement());
        });

        getLayout().setFillWidth(true);

        getLayout().getChildren().forEach(c -> {
            c.prefWidth(RestaurantScene.xDim - 40);
            VBox.setVgrow(c, Priority.ALWAYS);
        });

        ScrollPane sp = new ScrollPane();
        sp.setMaxWidth(Double.MAX_VALUE);
        sp.setFitToWidth(true);
        sp.setContent(getLayout());

        Button addButton = new Button("Add table");
        addButton.setOnAction(e -> {
            restaurantDB.addTable(this);
        });

        addButton.setPadding(new Insets(4, 4, 4, 4));

        VBox v = new VBox();
        v.getChildren().addAll(sp, addButton);

        return v;
    }

    public VBox getLayout() {
        return layout;
    }

}
