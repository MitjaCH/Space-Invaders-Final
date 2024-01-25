package ch.bbcag.bbcspaceinvader.gui.scenes;

import ch.bbcag.bbcspaceinvader.common.KeyControllable;
import ch.bbcag.bbcspaceinvader.gameobject.objects.*;
import ch.bbcag.bbcspaceinvader.gui.Navigator;
import ch.bbcag.bbcspaceinvader.gui.scenes.LoseScene;
import ch.bbcag.bbcspaceinvader.gameobject.objects.Spaceship;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.geometry.BoundingBox;
import ch.bbcag.bbcspaceinvader.game.BbcSpaceInvader;

import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_HEIGHT;
import static ch.bbcag.bbcspaceinvader.common.Const.SCREEN_WIDTH;

public class GameScene implements KeyControllable {

    private long lastTimeInNanoSec;
    private Navigator navigator;
    private int wave = 1;
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
    private List<Lootdropitem> lootdropsitem = new ArrayList<>();
    private List<Healthdrop> healthdrops = new ArrayList<>();
    private double shipBattery = 0;
    private Spaceship spaceship;

    private Stage stage;
    private Scene gameScene;
    private Scene loseScene;
    private LoseScene loseSceneObject;
    private int cooldown = 1;
    private boolean gameOver = false;
    private Stage primaryStage;
    public GameScene(Stage stage) {
        this.stage = stage;
        this.navigator = new Navigator();
        this.primaryStage = primaryStage;
        loseSceneObject = new LoseScene(navigator, this);
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

        alienFleet.add(new Alienship(300, 40, bombs, lootdrops, lootdropsitem));
        alienFleet.add(new Alienship(500, 40, bombs, lootdrops, lootdropsitem));

        animationTimer.start();
    }

    private void initializeWave() {
        double alienSpeed = 100 + 20 * wave;
        int numberOfAliens = 2 + wave;

        alienFleet.clear();

        double alienX = 300;
        double alienSpacing = 200;

        alienFleet.clear();

        for (int i = 0; i < numberOfAliens; i++) {
            alienFleet.add(new Alienship(alienX, 40, bombs, lootdrops, lootdropsitem));
            alienX += alienSpacing;
        }
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

        double distanceToMove = spaceship.SPEED * deltaInSec;
        if (isLeftKeyPressed && spaceship.getX() > 0)
            spaceship.setX(spaceship.getX() - distanceToMove);

        if (isRightKeyPressed && spaceship.getX() < 700)
            spaceship.setX(spaceship.getX() + distanceToMove);

        if (isSpaceKeyPressed && isShipBatteryFull(deltaInSec) && spaceship.AMMO > 0) {
            laserShots.add(new Laser(spaceship.getX() + spaceship.getImage().getWidth() / 2, 395));
            shipBattery -= 1;
            if (spaceship.AMMO > 0){
            spaceship.AMMO -= 1;
            }
            if (spaceship.AMMO == 0){
                cooldown = 1;
            }

        }
        if (spaceship.AMMO < 1){
            System.out.println("out of ammo");
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
        for (Lootdropitem lootdropitem : lootdropsitem) {
            lootdropitem.update(deltaInSec);
        }

        for (Healthdrop healthdrop : healthdrops) {
            healthdrop.update(deltaInSec);
        }

        if (alienFleet.isEmpty() && !gameOver) {
            wave++;
            initializeWave();
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
            if (bomb.collidesWith(new BoundingBox(spaceship.getX(), spaceship.getY(), spaceship.getImage().getWidth() - 5, spaceship.getImage().getHeight() -5))) {
                spaceship.takeDamage(45);
                bombs.remove(bomb);

                if (spaceship.getHealth() <= 0) {
                    switchToLoseScene();
                    gameOver = true;
                }
            }
        }

        for (Lootdrop lootdrop : lootdrops) {
            if (lootdrop.collidesWith(new BoundingBox(spaceship.getX(), 400, spaceship.getImage().getWidth(), spaceship.getImage().getHeight()))) {
                spaceship.AMMO += 5;
                lootdrops.remove(lootdrop);
            }
        }
        for (Lootdropitem lootdropitem : lootdropsitem) {
            if (lootdropitem.collidesWith(new BoundingBox(spaceship.getX(), 400, spaceship.getImage().getWidth(), spaceship.getImage().getHeight()))) {
                spaceship.AMMO = 999;
                cooldown = 10;
                lootdropsitem.remove(lootdropitem);
            }
        }
        for (Healthdrop healthdrop : healthdrops) {
            if (healthdrop.collidesWith(new BoundingBox(spaceship.getX(), 400, spaceship.getImage().getWidth(), spaceship.getImage().getHeight()))){
                spaceship.setHealth(100);
                healthdrops.remove(healthdrop);
            }
        }
    }

    private void switchToLoseScene() {
        navigator.navigateTo(loseSceneObject.getScene());
        resetGame();
    }


    private boolean isShipBatteryFull(double deltaInSec) {
        return shipBattery > cooldown;
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

        for (Lootdrop lootdrop : lootdrops){
            lootdrop.draw(gc);
        }
        for (Lootdropitem lootdropitem : lootdropsitem){
            lootdropitem.draw(gc);
        }

        for (Healthdrop healthdrop : healthdrops) {
            healthdrop.draw(gc);
        }

        String ammoLabel = "Ammo: " + spaceship.AMMO;
        String waveLabel = "Wave: " + wave;

        double healthPrecentage = spaceship.getHealth() / 100.0;
        double healthBarWidth = spaceship.getImage().getWidth() * healthPrecentage;
        double healthbarHeight = 10;
        double healthBarX = spaceship.getX();
        double healthBarY = spaceship.getY() + spaceship.getImage().getHeight() + 5;

        double redHealthBarWidth = spaceship.getImage().getWidth();
        double redHealthBarHeight = 10;
        double redHealthBarX = spaceship.getX();
        double redHealthBarY = spaceship.getY() + spaceship.getImage().getHeight() + 5;

        gc.setFill(Color.RED);
        gc.fillRect(redHealthBarX, redHealthBarY, redHealthBarWidth, redHealthBarHeight);

        gc.setFill(Color.GREEN);
        gc.fillRect(healthBarX, healthBarY, healthBarWidth, healthbarHeight);

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        gc.fillText(ammoLabel, SCREEN_WIDTH - 130, SCREEN_HEIGHT - 20);
        gc.fillText(waveLabel, SCREEN_WIDTH - 130, SCREEN_HEIGHT - 50);
    }

    public void resetGame() {
        gameOver = false;
        wave = 1;
        initializeWave();
        spaceship.setHealth(100);
        spaceship.AMMO = 10;
        laserShots.clear();
        bombs.clear();
        lootdrops.clear();
        lootdropsitem.clear();
        healthdrops.clear();
        for (Alienship alienship : alienFleet) {
            alienFleet.clear();
        }
    }

    public Scene getScene() {
        return gameScene;
    }
}
