package com.example.restaurant;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.util.Callback;
import java.io.IOException;
import java.util.List;

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

//        OwnerMenu ownerMenu = new OwnerMenu(restaurantDB);
//
//        Tab menuTab = new Tab("Menu");
//        menuTab.setContent(ownerMenu.getAsElement());
//        testPane.getTabs().add(menuTab);
//
//        final RestaurantDB resDB = restaurantDB;
//
//        ListView<Table> tableListView = new ListView<>();
//        tableListView.setCellFactory(tableListView1 -> new ListCell<Table>() {
//              @Override
//              protected void updateItem(Table table, boolean empty) {
//                    super.updateItem(table, empty);
//
//                    if(table == null || empty)
//                    {
//                        this.setText("Missing table");
//                    } else {
//                        this.getChildren().add(new TableCard(table, resDB).getAsElement());
//                    }
//              }
//        });
//
//        tableListView.setItems(restaurantDB.getTables());
//
//        Tab tablesTab = new Tab("Tables");
//        tablesTab.setContent(tableListView);
//        testPane.getTabs().add(tablesTab);
//
//        VBox vbox = new VBox();
//        vbox.getChildren().add(testPane);
//        Scene scene = new Scene(vbox);
//        stage.setScene(scene);
//        stage.show();

        Table testTable = restaurantDB.getTables().get(0);
        TableCard testTableCard = new TableCard(testTable, restaurantDB);
        Scene testTableScene = new Scene(testTableCard.getAsElement());
        stage.setScene(testTableScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}