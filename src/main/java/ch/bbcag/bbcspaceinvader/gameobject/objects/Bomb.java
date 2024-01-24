package ch.bbcag.bbcspaceinvader.gameobject.objects;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import ch.bbcag.bbcspaceinvader.gameobject.GameObject;

public class Bomb extends GameObject {

    private static final double SPEED = 50;

    public Bomb(double x, double y) {
        super(x, y, new Image(Bomb.class.getResourceAsStream("/Bomb.png")));
    }

    @Override
    public void update(double deltaTimeInSec) {
        double distanceToMove = deltaTimeInSec * SPEED;
        setY(getY() + distanceToMove);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(getImage(), getX(), getY());
    }

    public boolean collidesWith(BoundingBox boundingBox) {
        return getBoundingBox().intersects(boundingBox);
    }
}