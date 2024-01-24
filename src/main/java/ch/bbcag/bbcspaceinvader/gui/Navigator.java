package ch.bbcag.bbcspaceinvader.gui;

import ch.bbcag.bbcspaceinvader.gui.scenes.GameScene;
import ch.bbcag.bbcspaceinvader.gui.scenes.LoseScene;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Navigator {

    private Stage stage;
    private GameScene gameScene;
    private LoseScene loseScene;

    public Navigator(Stage stage) {
        this.stage = stage;
        initializeScenes();
    }

    private void initializeScenes() {
        gameScene = new GameScene(stage);
        gameScene.initialize();

        loseScene = new LoseScene();
    }

    public void switchToGameScene() {
        stage.setScene(gameScene.getScene());
    }

    public void switchToLoseScene() {
        stage.setScene(loseScene.getScene());
    }
}
