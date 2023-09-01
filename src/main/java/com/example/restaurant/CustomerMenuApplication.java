package com.example.restaurant;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class CustomerMenuApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        stage.setTitle("Jim's Burgers");

        RestaurantDB restaurantDB = null;
        try {
            restaurantDB = new RestaurantDB();
        } catch(Exception e) {
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setHeaderText("Error.");
            a.setTitle("Error");
            a.setContentText("Could not connect to remote database.");

            try {
                this.stop();
            } catch(Exception e1) {
                e1.printStackTrace();
            }
        }

        CustomerMenu customerMenu = new CustomerMenu(restaurantDB);
        Scene sc = new Scene(customerMenu.getAsElement(), RestaurantScene.xDim, RestaurantScene.yDim);
        stage.setScene(sc);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}