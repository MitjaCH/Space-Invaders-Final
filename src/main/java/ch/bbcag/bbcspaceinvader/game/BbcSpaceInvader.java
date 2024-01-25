// BbcSpaceInvader.java
package ch.bbcag.bbcspaceinvader.game;

import ch.bbcag.bbcspaceinvader.gui.Navigator;
import ch.bbcag.bbcspaceinvader.gui.scenes.StartScene;  // Import the StartScene
import javafx.application.Application;
import javafx.stage.Stage;

public class BbcSpaceInvader extends Application {

    private Navigator navigator;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bbc Space Invaders");

        navigator = new Navigator();
        navigator.setPrimaryStage(primaryStage);

        StartScene startScene = new StartScene(navigator);
        navigator.navigateTo(startScene.getScene());

        primaryStage.show();
    }
}
