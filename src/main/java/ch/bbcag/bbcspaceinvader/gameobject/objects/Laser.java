package ch.bbcag.bbcspaceinvader.gameobject.objects;

import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ch.bbcag.bbcspaceinvader.gameobject.GameObject;

public class Laser extends GameObject {

    private static final double SPEED = 200;

    public Laser(double x, double y) {
        super(x, y, new Image(Laser.class.getResourceAsStream("/projectile.png")));
    }

    @Override
    public void update(double deltaTimeInSec) {
        double distanceToMove = deltaTimeInSec * SPEED;
        setY(getY() - distanceToMove);
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(getImage(), getX(), getY());
    }

    public boolean collidesWith(Alienship alien) {
        return getBoundingBox().intersects(alien.getBoundingBox());
    }

    public boolean collidesWith(Bomb bomb) {
        return getBoundingBox().intersects(bomb.getBoundingBox());
    }
}