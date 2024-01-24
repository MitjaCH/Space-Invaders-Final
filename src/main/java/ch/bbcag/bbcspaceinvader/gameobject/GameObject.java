package ch.bbcag.bbcspaceinvader.gameobject;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public abstract class GameObject {

    protected double x;
    protected double y;
    protected Image image;

    public GameObject(double x, double y, Image image) {
        this.x = x;
        this.y = y;
        this.image = image;
    }

    public abstract void update(double deltaTimeInSec);

    public abstract void draw(GraphicsContext gc);

    public BoundingBox getBoundingBox() {
        return new BoundingBox(x, y, image.getWidth() - 2, image.getHeight() - 2);
    }

    // Getter and Setter for x
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    // Getter and Setter for y
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    // Getter and Setter for image
    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}