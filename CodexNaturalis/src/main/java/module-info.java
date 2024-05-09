module it.polimi.ingsw.psp17 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.google.gson;
    requires java.rmi;

    opens it.polimi.ingsw.psp17 to javafx.fxml;
    exports it.polimi.ingsw.psp17;

    exports it.polimi.ingsw.connection.rmi to java.rmi;
}