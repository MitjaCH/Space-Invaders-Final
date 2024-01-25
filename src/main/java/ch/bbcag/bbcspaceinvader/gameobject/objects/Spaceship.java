package ch.bbcag.bbcspaceinvader.gameobject.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ch.bbcag.bbcspaceinvader.gameobject.GameObject;
import ch.bbcag.bbcspaceinvader.gui.scenes.GameScene;

public class Spaceship extends GameObject {

    public static double SPEED = 300;
    private int health = 100;
    private boolean shield = false;
    private double shieldDuration = 5.0;
    public double AMMO = 25;
    private Image shieldImage = new Image(GameScene.class.getResourceAsStream("/forcefield.png"));

    public Spaceship(double x, double y, Image image) {
        super(x, y, image);
    }

    @Override
    public void update(double deltaTimeInSec) {
        if (shield) {
            shieldDuration -= deltaTimeInSec;
            if (shieldDuration <= 0) {
                deactivateShield();
            }
        }
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x, y);

        if (shield) {
            gc.drawImage(shieldImage, x - 10, y - 10);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Image getImage() {
        return image;
    }

    public void setX(double newX) {
        x = newX;
    }

    public void setY(double newY) {
        y = newY;
    }

    public void takeDamage(int damage) {
        if (shield) {
            return;
        }

        health -= damage;
        if (health <= 0) {
            System.out.println("Spaceship Destroyed!");
        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void activateShield() {
        shield = true;
        shieldDuration = 3;
    }

    public void deactivateShield() {
        shield = false;
        shieldDuration = 0;
    }

    public boolean isShieldActive() {
        return shield;
    }
}
