module com.example.synthesizer {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.GUI to javafx.fxml;
    exports com.example.GUI;
    exports Core;
    opens Core to javafx.fxml;
}