module com.example.restaurant {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.restaurant to javafx.fxml;
    exports com.example.restaurant;
}