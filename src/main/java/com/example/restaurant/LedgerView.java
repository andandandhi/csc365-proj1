package com.example.restaurant;

import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Callback;

public class LedgerView extends RestaurantScene {
    ScrollPane layout;
    RestaurantDB restaurantDB;
    ListView<LedgerEntry> ledgerEntryListView;
    public LedgerView(RestaurantDB restaurantDB) {
        this.layout = new ScrollPane();
        VBox v = new VBox();
        this.ledgerEntryListView = new ListView<>();

        this.ledgerEntryListView.setCellFactory(new Callback<>() {

            @Override
            public ListCell<LedgerEntry> call(ListView<LedgerEntry> ledgerEntryListView) {
                return new ListCell<>() {

                    @Override
                    protected void updateItem(LedgerEntry entry, boolean empty) {
                        super.updateItem(entry, empty);
                        if(entry == null || empty) {

                        } else {
                            this.setText("" + entry.getDate() + " - " + entry.getBalance() + " - " + entry.getNote());
                        }
                    }
                };
            }
        });

        this.ledgerEntryListView.setItems(restaurantDB.getLedgerEntries());

        Text balanceDisplay = new Text();
        balanceDisplay.textProperty().bind(restaurantDB.balanceStringProperty());
        balanceDisplay.setFont(new Font(36));
        balanceDisplay.setTextAlignment(TextAlignment.LEFT);
        HBox box = new HBox();
        box.getChildren().add(balanceDisplay);
        System.out.println(balanceDisplay.getText());

        v.getChildren().addAll(box, this.ledgerEntryListView);
        this.layout.setContent(v);
        this.restaurantDB = restaurantDB;

        this.layout.setMaxWidth(Double.MAX_VALUE);
        this.layout.setFitToWidth(true);
    }

    @Override
    public Parent getAsElement() {
        return layout;
    }
}
