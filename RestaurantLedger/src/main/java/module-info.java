module com.example.restaurantledger {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens com.example.restaurantledger to javafx.fxml;
    exports com.example.restaurantledger;
}
