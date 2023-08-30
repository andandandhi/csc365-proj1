package com.example.restaurant;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

import java.util.*;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
//        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
//        stage.setTitle("Our Application");
//        stage.setScene(scene);
//        stage.show();

        TabPane testPane = new TabPane();

        stage.setTitle("Jim's Burgers");

        RestaurantDB restaurantDB = new RestaurantDB();

        OwnerMenu ownerMenu = new OwnerMenu(restaurantDB);

        Tab menuTab = new Tab("Menu");
        menuTab.setContent(ownerMenu.getAsElement());
        testPane.getTabs().add(menuTab);

        VBox vbox = new VBox();
        vbox.getChildren().add(testPane);

        Scene scene = new Scene(vbox);
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}