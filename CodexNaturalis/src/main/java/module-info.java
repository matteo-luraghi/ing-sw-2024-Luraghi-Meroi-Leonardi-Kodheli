module it.polimi.ingsw.psp17 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;

    opens it.polimi.ingsw.psp17 to javafx.fxml;
    exports it.polimi.ingsw.psp17;
}