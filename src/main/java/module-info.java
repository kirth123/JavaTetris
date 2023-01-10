module com.reddy.tetris {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.reddy.tetris to javafx.fxml;
    exports com.reddy.tetris;
}