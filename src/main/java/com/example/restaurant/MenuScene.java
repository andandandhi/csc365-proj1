package com.example.restaurant;

import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class MenuScene
{
    RestaurantDB restaurantDB;
    VBox root;
    Scene scene;
    ArrayList<ListView<Dish>> views;

    public MenuScene(RestaurantDB restaurantDB)
    {
        this.root = new VBox();
        this.scene = new Scene(this.root, Util.dimx, Util.dimy);

        this.restaurantDB = restaurantDB;

        EnumSet.allOf(RestaurantDB.Category.class).forEach(cat -> {

            ListView<Dish> newView = new ListView<>(FXCollections.observableList(restaurantDB.getDishesByCategory(cat)));
            newView.setCellFactory(dish -> new ListCell<>() {
                protected void updateItem(Dish item, boolean empty) {
                    if (empty || item == null || item.toString() == null) {
                        setText(null);
                    } else {
                        setText(item.toString());
                    }
                }
            });

            views.add(cat.ordinal(), newView);
        });

    }

    public void display(Stage stage)
    {
        stage.setScene(this.scene);
    }

    public void addDish(Dish d, RestaurantDB.Category category)
    {
        views.get(category.ordinal()).getItems().add(d);

        /* Add dish to database here */
    }

    public void removeDish(int index, RestaurantDB.Category category)
    {
        views.get(category.ordinal()).getItems().remove(index);

        /* Remove dish from database here */
    }


}