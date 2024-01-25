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

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}