package ch.bbcag.bbcspaceinvader.gameobject.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ch.bbcag.bbcspaceinvader.gameobject.GameObject;
import ch.bbcag.bbcspaceinvader.gui.scenes.GameScene;

public class Spaceship extends GameObject {

    public static final double SPEED = 300;
    private int health = 100;
    public double AMMO = 10;

    public Spaceship(double x, double y, Image image) {
        super(x, y, image);
    }

    @Override
    public void update(double deltaTimeInSec) {
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(image, x, y);
    }

    // Getter methods for x and y
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    // Getter method for the image
    public Image getImage() {
        return image;
    }

    // Setter methods for x and y
    public void setX(double newX) {
        x = newX;
    }

    public void setY(double newY) {
        y = newY;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0){
            System.out.println("Spaceship Destroyed!");

        }
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }
}