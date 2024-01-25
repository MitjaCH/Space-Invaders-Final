// StartScene.java
package ch.bbcag.bbcspaceinvader.gui.scenes;

import ch.bbcag.bbcspaceinvader.gui.Navigator;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_HEIGHT;
import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_WIDTH;

public class StartScene {

    private Scene scene;
    private Navigator navigator;

    public StartScene(Navigator navigator) {
        this.navigator = navigator;
        createScene();
    }

    private void createScene() {
        Group startRoot = new Group();

        Image startSceneImage = new Image(this.getClass().getResourceAsStream("/background_welcome.png"));
        Canvas startCanvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        startCanvas.getGraphicsContext2D().drawImage(startSceneImage, 0, 0);
        startRoot.getChildren().add(startCanvas);

        scene = new Scene(startRoot);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                switchToGameScene();
            }
        });
    }

    private void switchToGameScene() {
        GameScene gameScene = new GameScene(navigator.getPrimaryStage());
        gameScene.initialize();
        navigator.navigateTo(gameScene.getScene());
    }

    public Scene getScene() {
        return scene;
    }
}
