package ch.bbcag.bbcspaceinvader.game;

import ch.bbcag.bbcspaceinvader.gui.scenes.GameScene;
import javafx.application.Application;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class BbcSpaceInvader extends Application {

    private GameScene gameScene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Bbc Space Invaders");

        gameScene = new GameScene(primaryStage);
        gameScene.initialize();

        gameScene.getScene().setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        gameScene.getScene().setOnKeyReleased(e -> handleKeyRelease(e.getCode()));

        primaryStage.show();
    }

    private void handleKeyPress(KeyCode code) {
        switch (code) {
            case LEFT:
                gameScene.setLeftKeyPressed(true);
                break;
            case RIGHT:
                gameScene.setRightKeyPressed(true);
                break;
            case SPACE:
                gameScene.setSpaceBarPressed(true);
                break;
        }
    }

    private void handleKeyRelease(KeyCode code) {
        switch (code) {
            case LEFT:
                gameScene.setLeftKeyPressed(false);
                break;
            case RIGHT:
                gameScene.setRightKeyPressed(false);
                break;
            case SPACE:
                gameScene.setSpaceBarPressed(false);
                break;
        }
    }
}
