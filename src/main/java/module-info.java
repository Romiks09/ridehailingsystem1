module com.example.ridehailingsystem1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ridehailingsystem1 to javafx.fxml;
    exports com.example.ridehailingsystem1;
}