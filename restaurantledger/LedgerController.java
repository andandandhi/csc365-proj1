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
        // Initialize the TableView with data (you can replace this with your data source)
        ledgerEntries = FXCollections.observableArrayList(
                new LedgerEntry(1, Date.valueOf("2023-08-31"), "Initial Balance", 100.0),
                new LedgerEntry(2, Date.valueOf("2023-09-01"), "Expense", -50.0)
        );

        tableView.setItems(ledgerEntries);

        // Define how table columns map to LedgerEntry properties
        changeInBalanceColumn.setCellValueFactory(cellData -> cellData.getValue().balanceProperty().asObject());

        // Update the lambda expression for dateTimeColumn
        dateTimeColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<Date>(cellData.getValue().getDate()));

        noteColumn.setCellValueFactory(cellData -> cellData.getValue().noteProperty());
    }


    @FXML
    private void onAddButtonClick() {
        // Get data from text fields and create a new LedgerEntry
        Date date = Date.valueOf(textField1.getText()); // Use Date.valueOf to parse the date string
        String note = textField2.getText();
        double balance = Double.parseDouble(textField3.getText());

        // Create a new LedgerEntry
        LedgerEntry newEntry = new LedgerEntry(ledgerEntries.size() + 1, date, note, balance);

        // Add the new entry to the TableView and clear text fields
        ledgerEntries.add(newEntry);
        textField1.clear();
        textField2.clear();
        textField3.clear();
    }
}
