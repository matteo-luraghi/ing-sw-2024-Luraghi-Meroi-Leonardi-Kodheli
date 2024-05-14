package it.polimi.ingsw.psp17;

import it.polimi.ingsw.view.gui.GUI;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

/**
 * JavaFX Application that shows the correct FXML to the player
 */
public class GUIApplication extends Application {
    private static String sceneName;
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(sceneName);
        FXMLLoader fxmlLoader = new FXMLLoader(GUIApplication.class.getResource(sceneName));
        Scene scene = new Scene(fxmlLoader.load(), 600, 500);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void loadApplication(String scene) {
        sceneName = scene;
        launch();
    }
}