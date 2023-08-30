package com.example.restaurant;

import javafx.application.Application;
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

        stage.setTitle("Jim's Burgers");

        RestaurantDB restaurantDB = new RestaurantDB();

        OwnerMenu ownerMenu = new OwnerMenu(restaurantDB, stage);

        ownerMenu.display();

    }

    public static void main(String[] args) {
        launch();
    }
}