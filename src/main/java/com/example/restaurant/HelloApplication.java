package com.example.restaurant;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        TabPane testPane = new TabPane();

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