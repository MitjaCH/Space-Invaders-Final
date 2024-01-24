package ch.bbcag.bbcspaceinvader.gui.scenes;

import ch.bbcag.bbcspaceinvader.gui.Navigator;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_HEIGHT;
import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_WIDTH;

public class LoseScene {

    private Scene scene;
    private Navigator navigator;

    public LoseScene(Navigator navigator) {
        this.navigator = navigator;
        createScene();
    }

    private void createScene() {
        Group loseRoot = new Group();

        Image loseSceneImage = new Image(this.getClass().getResourceAsStream("/background_gameover.png"));
        Canvas loseCanvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        loseCanvas.getGraphicsContext2D().drawImage(loseSceneImage, 0, 0);
        loseRoot.getChildren().add(loseCanvas);

        loseRoot.getChildren().addAll();

        scene = new Scene(loseRoot);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE) {
                switchToGameScene();
            }
        });
    }

    public Scene getScene() {
        return scene;
    }

    private void switchToGameScene() {
        navigator.switchToGameScene();
    }
}
