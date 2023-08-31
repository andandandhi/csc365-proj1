package com.example.restaurantledger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;

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
    private TableColumn<LedgerEntry, String> dateTimeColumn;

    @FXML
    private TableColumn<LedgerEntry, String> noteColumn;

    public void initialize() {

    }

    @FXML
    private void onAddButtonClick() {

    }
}
