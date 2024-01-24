package ch.bbcag.bbcspaceinvader.gui;

import ch.bbcag.bbcspaceinvader.gui.scenes.GameScene;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigator {

    private Stage stage;

    private GameScene gameScene;

    public Navigator(Stage stage) {
        this.stage = stage;
    }

    public void switchTo(Scene scene) {
        stage.setScene(scene);
    }

    public void switchToGameScene() {
        stage.setScene(gameScene.getScene());
    }
}
