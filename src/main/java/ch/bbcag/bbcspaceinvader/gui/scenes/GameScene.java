package ch.bbcag.bbcspaceinvader.gui.scenes;

import ch.bbcag.bbcspaceinvader.common.KeyControllable;
import ch.bbcag.bbcspaceinvader.gameobject.objects.*;
import ch.bbcag.bbcspaceinvader.gui.scenes.LoseScene;
import ch.bbcag.bbcspaceinvader.common.KeyEventHandler;
import ch.bbcag.bbcspaceinvader.gameobject.objects.Lootdrop;
import javafx.animation.AnimationTimer;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.geometry.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_HEIGHT;
import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_WIDTH;

public class GameScene implements KeyControllable {

    private static final double SPEED_OF_SPACESHIP = 100;

    private long lastTimeInNanoSec;
    private Canvas canvas;
    private GraphicsContext gc;
    private Image gameBackground = new Image(GameScene.class.getResourceAsStream("/background_game.png"));
    private boolean isLeftKeyPressed = false;
    private boolean isRightKeyPressed = false;
    private boolean isSpaceKeyPressed = false;
    private List<Alienship> alienFleet = new CopyOnWriteArrayList<>();
    private List<Bomb> bombs = new CopyOnWriteArrayList<>();
    private List<Laser> laserShots = new ArrayList<>();
    private List<Lootdrop> lootdrops = new ArrayList<>();
    private double shipBattery = 0;
    private Spaceship spaceship;

    private Stage stage;
    private Scene gameScene;
    private Scene loseScene;
    private LoseScene loseSceneObject;

    public GameScene(Stage stage) {
        this.stage = stage;
        loseSceneObject = new LoseScene();
    }

    public void initialize() {
        Group root = new Group();
        canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);
        root.getChildren().add(canvas);

        gc = canvas.getGraphicsContext2D();
        canvas.setCache(true);

        gameScene = new Scene(root);
        loseScene = loseSceneObject.getScene();

        spaceship = new Spaceship(350, 400, new Image(GameScene.class.getResourceAsStream("/Spaceship.png")));

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

        alienFleet.add(new Alienship(300, 40, bombs, lootdrops));
        alienFleet.add(new Alienship(500, 40, bombs, lootdrops));

        animationTimer.start();
    }

    @Override
    public void setLeftKeyPressed(boolean pressed) {
        isLeftKeyPressed = pressed;
    }

    @Override
    public void setRightKeyPressed(boolean pressed) {
        isRightKeyPressed = pressed;
    }

    @Override
    public void setSpaceBarPressed(boolean pressed) {
        isSpaceKeyPressed = pressed;
    }

    private void onKeyReleased(KeyEvent e) {
        if (e.getCode() == KeyCode.LEFT)
            isLeftKeyPressed = false;
        if (e.getCode() == KeyCode.RIGHT)
            isRightKeyPressed = false;
        if (e.getCode() == KeyCode.SPACE)
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
        for (Lootdrop lootdrop : lootdrops) {
            lootdrop.update(deltaInSec);
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
                switchToLoseScene();
            }
        }

        for (Lootdrop lootdrop : lootdrops) {
            if (lootdrop.collidesWith(new BoundingBox(spaceship.getX(), 400, spaceship.getImage().getWidth(), spaceship.getImage().getHeight()))) {
                //give item
            }
        }
    }

    private void switchToLoseScene() {
        stage.setScene(loseScene);
    }

    private boolean isShipBatteryFull(double deltaInSec) {
        return shipBattery > 1;
    }

    private void paint() {
        gc.clearRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
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

    public Scene getScene() {
        return gameScene;
    }
}
