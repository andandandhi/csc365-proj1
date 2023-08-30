package com.example.restaurant;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

        ArrayList<Dish> testDishes = new ArrayList<>();

        testDishes.add(new Dish(
                1,
                "Pizza",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                14.99,
                "DINNER"
        ));

        testDishes.add(new Dish(
                1,
                "Pizza",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                14.99,
                "DINNER"
        ));

        testDishes.add(new Dish(
                1,
                "Pizza",
                "Sed ut perspiciatis unde omnis iste natus error sit voluptatem accusantium doloremque laudantium, totam rem aperiam, eaque ipsa quae ab illo inventore veritatis et quasi architecto beatae vitae dicta sunt explicabo.",
                14.99,
                "DINNER"
        ));

        CustomerMenu customerMenu = new CustomerMenu(testDishes, stage);

        customerMenu.display();

    }

    public static void main(String[] args) {
        launch();
    }
}