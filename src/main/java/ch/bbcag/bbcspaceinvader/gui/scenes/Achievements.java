package ch.bbcag.bbcspaceinvader.gui.scenes;

import ch.bbcag.bbcspaceinvader.gui.Navigator;
import ch.bbcag.bbcspaceinvader.gui.scenes.GameScene;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_HEIGHT;
import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_WIDTH;

public class Achievements {

    private Scene scene;
    private Navigator navigator;
    private Achievements achievements;
    public double kills;
    public double healthTaken;
    private StartScene startScene;// Add this field


    public Achievements(Navigator navigator, StartScene startScene) {
        this.navigator = navigator;
        this.startScene = startScene;
        createScene();
    }

    private void createScene() {
        Group startRoot = new Group();

        Image startSceneImage = new Image(this.getClass().getResourceAsStream("/background_credits.png"));
        Canvas startCanvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        startCanvas.getGraphicsContext2D().drawImage(startSceneImage, 0, 0);
        startRoot.getChildren().add(startCanvas);

        VBox achievementsBox = new VBox();
        achievementsBox.setPadding(new Insets(20, 20, 20, 20));
        achievementsBox.setSpacing(10);

        HBox killsBox = createStatBox("Kills   ", kills);
        HBox healthTakenBox = createStatBox("Health Taken   ", healthTaken);

        achievementsBox.getChildren().addAll(killsBox, healthTakenBox);

        startRoot.getChildren().add(achievementsBox);

        scene = new Scene(startRoot);

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                switchToStartScene();
            }
        });
    }

    private HBox createStatBox(String title, double value) {
        HBox statBox = new HBox();
        statBox.setSpacing(10);

        Rectangle boxBackground = new Rectangle(150, 60);
        boxBackground.setStyle("-fx-fill: #ffffff; -fx-stroke: #000000;");

        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label valueLabel = new Label(Double.toString(value));
        valueLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 14));

        statBox.getChildren().addAll(boxBackground, titleLabel, valueLabel);

        return statBox;
    }

    private void switchToStartScene() {
        StartScene startScene = new StartScene(navigator);
        navigator.navigateTo(startScene.getScene());
    }

    public Scene getScene() {
        return scene;
    }
}
