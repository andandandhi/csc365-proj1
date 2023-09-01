package com.example.restaurant;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.io.IOException;

public class RestaurantApplication extends Application {
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

        TabPane mainTabPane = new TabPane();

        OwnerMenu ownerMenu = new OwnerMenu(restaurantDB);
        Tab ownerMenuTab = new Tab("Menu");
        ownerMenuTab.setContent(ownerMenu.getAsElement());
        mainTabPane.getTabs().add(ownerMenuTab);

        TableDisplay tableDisplay = new TableDisplay(restaurantDB);
        Tab tableDisplayTab = new Tab("Tables");
        tableDisplayTab.setContent(tableDisplay.getAsElement());
        mainTabPane.getTabs().add(tableDisplayTab);

        LedgerView ledgerView = new LedgerView(restaurantDB);
        Tab ledgerDisplayTab = new Tab("Ledger");
        ledgerDisplayTab.setContent(ledgerView.getAsElement());
        mainTabPane.getTabs().add(ledgerDisplayTab);

        Scene mainScene = new Scene(mainTabPane, RestaurantScene.xDim, RestaurantScene.yDim);
        stage.setScene(mainScene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}