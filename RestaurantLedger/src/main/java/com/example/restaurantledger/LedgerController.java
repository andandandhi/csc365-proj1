package com.example.restaurantledger;
import java.sql.Date;

import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class LedgerController {
    @FXML
    private Label welcomeText;

    @FXML
    private TextField textField1;

    @FXML
    private TextField textField2;

    @FXML
    private TextField textField3;

    @FXML
    private Button addButton;

    @FXML
    private TableView<LedgerEntry> tableView;

    @FXML
    private TableColumn<LedgerEntry, Double> changeInBalanceColumn;

    @FXML
    private TableColumn<LedgerEntry, Date> dateTimeColumn; // Use java.sql.Date here

    @FXML
    private TableColumn<LedgerEntry, String> noteColumn;

    private ObservableList<LedgerEntry> ledgerEntries;

    public void initialize() {
        // Initialize the TableView with data from the database
        System.out.println("Controller initialized");
        RestaurantDB restaurantDB = new RestaurantDB();
        ledgerEntries = FXCollections.observableArrayList(restaurantDB.getLedgerEntriesFromDatabase());

        tableView.setItems(ledgerEntries);

        // Define how table columns map to LedgerEntry properties
        changeInBalanceColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asObject());
        dateTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getDate()));
        noteColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
    }



    @FXML
    private void onAddButtonClick() {
        // Get data from text fields and create a new LedgerEntry
        Date date = Date.valueOf(textField2.getText()); // Use Date.valueOf to parse the date string
        String note = textField3.getText();
        double balance = Double.parseDouble(textField1.getText());

        // Create a new LedgerEntry
        LedgerEntry newEntry = new LedgerEntry(ledgerEntries.size() + 1, date, note, balance);

        // Add the new entry to the TableView and clear text fields
        ledgerEntries.add(newEntry);
        textField1.clear();
        textField2.clear();
        textField3.clear();
    }
}