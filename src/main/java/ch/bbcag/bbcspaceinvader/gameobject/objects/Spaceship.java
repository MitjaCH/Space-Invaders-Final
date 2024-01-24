package ch.bbcag.bbcspaceinvader.gameobject.objects;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ch.bbcag.bbcspaceinvader.gameobject.GameObject;

public class Spaceship extends GameObject {

    public static final double SPEED = 200;

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

    // Additional methods or properties specific to Spaceship
}