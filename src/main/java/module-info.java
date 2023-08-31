module com.example.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.example.restaurant to javafx.fxml;
    exports com.example.restaurant;
}