package ch.bbcag.bbcspaceinvader.gui.scenes;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.Image;

import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_HEIGHT;
import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_WIDTH;

public class LoseScene {

    private Scene scene;

    public LoseScene() {
        createScene();
    }

    private void createScene() {
        Group loseRoot = new Group();
        Canvas loseCanvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        loseRoot.getChildren().add(loseCanvas);

        javafx.scene.canvas.GraphicsContext loseGC = loseCanvas.getGraphicsContext2D();
        loseCanvas.setCache(true);

        Image loseSceneImage = new Image(this.getClass().getResourceAsStream("/background_gameover.png"));
        loseGC.drawImage(loseSceneImage, 0, 0);

        scene = new Scene(loseRoot);
    }

    public Scene getScene() {
        return scene;
    }
}
