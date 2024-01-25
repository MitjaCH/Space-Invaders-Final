// Navigator.java
package ch.bbcag.bbcspaceinvader.gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigator {

    private static Stage primaryStage;
    private static Scene currentScene;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void navigateTo(Scene scene) {
        if (primaryStage != null) {
            primaryStage.setScene(scene);
            currentScene = scene;
        } else {
            System.out.println("Primary Stage is not Set. Call setPrimaryStage() before navigating.");
        }
    }

    public static Scene getCurrentScene() {
        return currentScene;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
