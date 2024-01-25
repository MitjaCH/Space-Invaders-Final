package ch.bbcag.bbcspaceinvader.gui.scenes;

import ch.bbcag.bbcspaceinvader.common.KeyControllable;
import ch.bbcag.bbcspaceinvader.gameobject.objects.*;
import ch.bbcag.bbcspaceinvader.gui.Navigator;
import ch.bbcag.bbcspaceinvader.gui.scenes.LoseScene;
import ch.bbcag.bbcspaceinvader.gameobject.objects.Spaceship;
import ch.bbcag.bbcspaceinvader.common.KeyEventHandler;
import ch.bbcag.bbcspaceinvader.gameobject.objects.Lootdrop;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
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
    private boolean isXKeyPressed = false;
    private List<Alienship> alienFleet = new CopyOnWriteArrayList<>();
    private List<Bomb> bombs = new CopyOnWriteArrayList<>();
    private List<Laser> laserShots = new ArrayList<>();
    private List<Lootdrop> lootdrops = new CopyOnWriteArrayList<>();
    private List<Minigundrop> lootdropsitem = new CopyOnWriteArrayList<>();
    private List<Healthdrop> healthdrops = new CopyOnWriteArrayList<>();
    private List<Shielddrop> shielddrops = new CopyOnWriteArrayList<>();
    private String notificationMessage = "";
    private double notificationTimer = 0.0;
    private static final double NOTIFICATION_DISPLAY_TIME = 3.0;
    private double shipBattery = 0;
    private Spaceship spaceship;

    private Stage stage;
    private Scene gameScene;
    private Scene loseScene;
    private LoseScene loseSceneObject;
    private double cooldown = 1;
    private boolean gameOver = false;
    private boolean minigun = false;
    private int frameCount = 0;
    private long lastFPSTime;
    private double fps;
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

        lastTimeInNanoSec = System.nanoTime();
        lastFPSTime = System.nanoTime();

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



        alienFleet.add(new Alienship(300, 40, bombs, lootdrops, lootdropsitem, healthdrops, shielddrops));
        alienFleet.add(new Alienship(500, 40, bombs, lootdrops, lootdropsitem, healthdrops, shielddrops));

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
            alienFleet.add(new Alienship(alienX, 40, bombs, lootdrops, lootdropsitem, healthdrops, shielddrops));
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

    @Override
    public void setXKeyPressed(boolean pressed) {
        isXKeyPressed = pressed;
    }

    private void onKeyReleased(KeyEvent e) {
        if (e.getCode() == KeyCode.LEFT)
            isLeftKeyPressed = false;
        if (e.getCode() == KeyCode.RIGHT)
            isRightKeyPressed = false;
        if (e.getCode() == KeyCode.SPACE)
            isSpaceKeyPressed = false;
        if (e.getCode() == KeyCode.F2)
            isXKeyPressed = false;
    }

    private void update(double deltaInSec) {
        chargeBatteryOfSpaceship(deltaInSec);
        checkCollision();

        double distanceToMove = spaceship.SPEED * deltaInSec;
        if (isLeftKeyPressed && spaceship.getX() > 0)
            spaceship.setX(spaceship.getX() - distanceToMove);

        if (isRightKeyPressed && spaceship.getX() < 740)
            spaceship.setX(spaceship.getX() + distanceToMove);

        if (isXKeyPressed) {
            System.out.println("Key Pressed");
            showInputDialog();
            isXKeyPressed = false;
        }

        if (isSpaceKeyPressed && isShipBatteryFull(deltaInSec) && spaceship.AMMO > 0) {
            laserShots.add(new Laser(spaceship.getX() + spaceship.getImage().getWidth() / 2, 395));
            if (minigun == false) {
                shipBattery = 1;
                shipBattery -= 1;
            }
            if (spaceship.AMMO > 0){
                spaceship.AMMO -= 1;
            }
            if (spaceship.AMMO <= 10) {
                if (minigun = true) {
                    minigun = false;
                    cooldown = 1;
                    displayNotification("Minigun is off!");
                }
                minigun = false;
            }


        }

        if (!notificationMessage.isEmpty()) {
            notificationTimer += deltaInSec;

            if (notificationTimer >= NOTIFICATION_DISPLAY_TIME) {
                notificationMessage = "";
                notificationTimer = 0.0;
            }
        }
        if (spaceship.AMMO < 1){
            displayNotification("Out of Ammo!");
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
        for (Minigundrop lootdropitem : lootdropsitem) {
            lootdropitem.update(deltaInSec);
        }

        for (Healthdrop healthdrop : healthdrops) {
            healthdrop.update(deltaInSec);
        }
        for (Shielddrop shielddrop : shielddrops) {
            shielddrop.update(deltaInSec);
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
                    isLeftKeyPressed = false;
                    isRightKeyPressed = false;
                    isSpaceKeyPressed = false;
                    switchToLoseScene();
                    gameOver = true;
                }
            }
        }

        for (Shielddrop shielddrop : shielddrops) {
            if (shielddrop.collidesWith(spaceship.getBoundingBox())) {
                spaceship.activateShield();
                shielddrops.remove(shielddrop);
            }
        }



        for (Lootdrop lootdrop : lootdrops) {
            if (lootdrop.collidesWith(new BoundingBox(spaceship.getX(), 400, spaceship.getImage().getWidth(), spaceship.getImage().getHeight()))) {
                spaceship.AMMO += 10;
                lootdrops.remove(lootdrop);
            }
        }
        for (Minigundrop lootdropitem : lootdropsitem) {
            if (lootdropitem.collidesWith(new BoundingBox(spaceship.getX(), 400, spaceship.getImage().getWidth(), spaceship.getImage().getHeight()))) {
                spaceship.AMMO += 300;
                cooldown = 0;
                minigun = true;
                displayNotification("Minigun Enabled!");
                lootdropsitem.remove(lootdropitem);
            }
        }
        for (Healthdrop healthdrop : healthdrops) {
            if (healthdrop.collidesWith(new BoundingBox(spaceship.getX(), 400, spaceship.getImage().getWidth(), spaceship.getImage().getHeight()))){
                spaceship.setHealth(100);
                healthdrops.remove(healthdrop);
            }
        }
        for (Shielddrop shielddrop : shielddrops) {
            if (shielddrop.collidesWith(new BoundingBox(spaceship.getX(), 400, spaceship.getImage().getWidth(), spaceship.getImage().getHeight()))){
                shielddrops.remove(shielddrop);
                //Spaceship.shield.draw();
            }
        }
    }

    private void showInputDialog() {
        Platform.runLater(() -> {
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Admin Console");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter a Command:");

            dialog.showAndWait().ifPresent(result -> {
                if ("Kebi".equalsIgnoreCase(result.trim())) {
                    spaceship.setHealth(1000);
                    displayNotification("Kebab Mode enabled: Eating kebabs for more health...");
                }
                if ("Justin".equalsIgnoreCase(result.trim())) {
                    spaceship.SPEED = 30;
                    displayNotification("Justin Mode enabled: You smoked too many Cigarettes you are slower now...");
                }
                if ("youdontknowmeson".equalsIgnoreCase(result.trim())) {
                    spaceship.SPEED = 1000;
                    displayNotification("David Goggins Mode enabled: THEY DONT KNOW ME SON");
                }
                if ("Minigun".equalsIgnoreCase(result.trim())) {
                    minigun = true;
                    spaceship.AMMO = 300;
                    displayNotification("Minigun enabled!");
                }
                if ("God".equalsIgnoreCase(result.trim())) {
                    minigun = true;
                    spaceship.AMMO = 999999999;
                    spaceship.setHealth(999999999);
                    displayNotification("God-mode enabled! (aka Justin)");
                }
                if ("Ezy Win".equalsIgnoreCase(result.trim())){
                    wave = 64;
                    displayNotification("Ezy Win enabled! (aka Fettsack mode)");
                } else {
                    System.out.println("User defined: " + result);
                }
            });
        });
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
        for (Minigundrop lootdropitem : lootdropsitem){
            lootdropitem.draw(gc);
        }

        for (Healthdrop healthdrop : healthdrops) {
            healthdrop.draw(gc);
        }
        for (Shielddrop shielddrop : shielddrops) {
            shielddrop.draw(gc);
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

        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        String versionLabel = "Space-Invaders | VER 0.1.0 | By Louis & Mitja";
        double versionLabelX = 10;
        double versionLabelY = 20;

        String fpsLabel = "FPS: " + String.format("%.2f", fps);
        gc.fillText(fpsLabel, SCREEN_WIDTH - 130, SCREEN_HEIGHT - 80);

        gc.fillText(versionLabel, versionLabelX, versionLabelY);

        if (!notificationMessage.isEmpty()) {
            gc.setFill(Color.YELLOW);
            gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));

            Text notificationText = new Text(notificationMessage);
            notificationText.setFont(gc.getFont());

            double notificationX = SCREEN_WIDTH - notificationText.getLayoutBounds().getWidth() - 10;
            double notificationY = 20;

            gc.fillText(notificationMessage, notificationX, notificationY);
        }
    }

    public void resetGame() {
        gameOver = false;
        wave = wave - wave;
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

    private void displayNotification(String message) {
        notificationMessage = message;
        notificationTimer = 0.0;
    }
}
