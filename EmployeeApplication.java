package com.example.restaurantemployee;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class EmployeeApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("EmployeeSampleSkeleton.fxml"));

        Scene scene = new Scene(root, 522, 651);

        primaryStage.setTitle("Restaurant Employee Application");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
