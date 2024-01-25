package ch.bbcag.bbcspaceinvader.gui.scenes;

import ch.bbcag.bbcspaceinvader.gui.Navigator;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_HEIGHT;
import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_WIDTH;

public class LoseScene {

    private Scene scene;
    private Navigator navigator;
    private GameScene gameScene; // Add reference to GameScene

    public LoseScene(Navigator navigator, GameScene gameScene) {
        this.navigator = navigator;
        this.gameScene = gameScene;
        createScene();
    }

    private void createScene() {
        Group loseRoot = new Group();

        Image loseSceneImage = new Image(this.getClass().getResourceAsStream("/background_gameover.png"));
        Canvas loseCanvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        loseCanvas.getGraphicsContext2D().drawImage(loseSceneImage, 0, 0);
        loseRoot.getChildren().add(loseCanvas);

        scene = new Scene(loseRoot);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                switchToGameScene();
            }
        });
    }

    private void switchToGameScene() {
        gameScene.resetGame(); // Reset the game when switching to GameScene
        navigator.navigateTo(gameScene.getScene());
    }

    public Scene getScene() {
        return scene;
    }
}
