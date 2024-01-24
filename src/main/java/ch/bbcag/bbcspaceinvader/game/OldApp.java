/*
package ch.bbcag.bbcspaceinvader.game;

import ch.bbcag.bbcspaceinvader.gameobject.objects.Alienship;
import ch.bbcag.bbcspaceinvader.gameobject.objects.Bomb;
import ch.bbcag.bbcspaceinvader.gameobject.GameObject;
import ch.bbcag.bbcspaceinvader.gameobject.objects.Laser;
import ch.bbcag.bbcspaceinvader.gameobject.objects.Spaceship;
import ch.bbcag.bbcspaceinvader.common.KeyEventHandler;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import javafx.geometry.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_HEIGHT;
import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_WIDTH;

public class BbcSpaceInvader extends Application {

    private static final double SPEED_OF_SPACESHIP = 100;

    private static long lastTimeInNanoSec;
    private Canvas canvas;
    private GraphicsContext gc;
    private Image gameBackground = new Image(this.getClass().getResourceAsStream("/background_game.png"));
    private Image loseSceneImage = new Image(this.getClass().getResourceAsStream("/background_gameover.png"));
    private boolean isLeftKeyPressed = false;
    private boolean isRightKeyPressed = false;
    private boolean isSpaceKeyPressed = false;
    private List<Alienship> alienFleet = new CopyOnWriteArrayList<>();
    private List<Bomb> bombs = new CopyOnWriteArrayList<>();
    private List<Laser> laserShots = new ArrayList<>();
    private double shipBattery = 0;
    private Spaceship spaceship;

    private Scene gameScene;
    private Scene loseScene;

    public void setLeftKeyPressed(boolean leftKeyPressed) {
        this.isLeftKeyPressed = leftKeyPressed;
    }

    public void setRightKeyPressed(boolean rightKeyPressed) {
        this.isRightKeyPressed = rightKeyPressed;
    }

    public void setSpaceKeyPressed(boolean spaceKeyPressed) {
        this.isSpaceKeyPressed = spaceKeyPressed;
    }

    @Override
    public void start(Stage stage) throws Exception {
        Group root = new Group();
        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();
        canvas.setCache(true);

        gameScene = new Scene(root);
        loseScene = createLoseScene();

        spaceship = new Spaceship(350, 400, new Image(this.getClass().getResourceAsStream("/Spaceship.png")));

        gameScene.setOnKeyPressed(new KeyEventHandler(this));
        gameScene.setOnKeyReleased(this::onKeyReleased);

        stage.setTitle("Bbc SpaceInvader");
        stage.setScene(gameScene);
        stage.show();

        lastTimeInNanoSec = System.nanoTime();

        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long currentTimeInNanoSec) {
                long deltaInNanoSec = currentTimeInNanoSec - lastTimeInNanoSec;
                double deltaInSec = deltaInNanoSec / 1e9;

                lastTimeInNanoSec = currentTimeInNanoSec;

                update(deltaInSec);
                paint();
            }
        };

        alienFleet.add(new Alienship(300, 40, bombs));
        alienFleet.add(new Alienship(500, 40, bombs));

        animationTimer.start();
    }

    private Scene createLoseScene() {
        Group loseRoot = new Group();
        Canvas loseCanvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        loseRoot.getChildren().add(loseCanvas);

        GraphicsContext loseGC = loseCanvas.getGraphicsContext2D();
        loseCanvas.setCache(true);

        loseGC.drawImage(loseSceneImage, 0, 0);

        return new Scene(loseRoot);
    }

    private void onKeyReleased(KeyEvent e) {
        if (e.getCode() == javafx.scene.input.KeyCode.LEFT)
            isLeftKeyPressed = false;
        if (e.getCode() == javafx.scene.input.KeyCode.RIGHT)
            isRightKeyPressed = false;
        if (e.getCode() == javafx.scene.input.KeyCode.SPACE)
            isSpaceKeyPressed = false;
    }

    private void update(double deltaInSec) {
        chargeBatteryOfSpaceship(deltaInSec);
        checkCollision();

        double distanceToMove = SPEED_OF_SPACESHIP * deltaInSec;
        if (isLeftKeyPressed && spaceship.getX() > 0)
            spaceship.setX(spaceship.getX() - distanceToMove);

        if (isRightKeyPressed && spaceship.getX() < 700)
            spaceship.setX(spaceship.getX() + distanceToMove);

        if (isSpaceKeyPressed && isShipBatteryFull(deltaInSec)) {
            laserShots.add(new Laser(spaceship.getX() + spaceship.getImage().getWidth() / 2, 395));
            shipBattery -= 1;
        }

        for (Alienship alien : alienFleet) {
            alien.update(deltaInSec);
        }

        for (Laser shot : laserShots) {
            shot.update(deltaInSec);
        }

        for (Bomb bomb : bombs) {
            bomb.update(deltaInSec);
        }
    }

    private void chargeBatteryOfSpaceship(double deltaInSec) {
        shipBattery += deltaInSec;
    }

    private void checkCollision() {
        for (Laser shot : laserShots) {
            for (Alienship alien : alienFleet) {
                if (shot.collidesWith(alien)) {
                    alienFleet.remove(alien);
                }
            }
        }

        for (Laser shot : laserShots) {
            for (Bomb bomb : bombs) {
                if (shot.collidesWith(bomb)) {
                    bombs.remove(bomb);
                }
            }
        }

        for (Bomb bomb : bombs) {
            if (bomb.collidesWith(new BoundingBox(spaceship.getX(), 400, spaceship.getImage().getWidth(), spaceship.getImage().getHeight()))) {
                // Switch to the LoseScene
                switchToLoseScene();
            }
        }
    }

    private void switchToLoseScene() {
        Stage stage = (Stage) canvas.getScene().getWindow();
        stage.setScene(loseScene);
        // Add any additional logic or cleanup as needed
    }

    private boolean isShipBatteryFull(double deltaInSec) {
        return shipBattery > 1;
    }

    private void paint() {
        gc.clearRect(0, 0, 800, 600);
        gc.drawImage(gameBackground, 0, 0);
        spaceship.draw(gc);

        for (Alienship alien : alienFleet) {
            alien.draw(gc);
        }

        for (Laser shot : laserShots) {
            shot.draw(gc);
        }

        for (Bomb bomb : bombs) {
            bomb.draw(gc);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
} */