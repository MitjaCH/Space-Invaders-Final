package ch.bbcag.bbcspaceinvader.gameobject.objects;

import ch.bbcag.bbcspaceinvader.gameobject.GameObject;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Shielddrop extends GameObject {
    private static final double SPEED = 50;
    public boolean shield = false;

    public Shielddrop(double x, double y) {
        super(x, y, new Image(Shielddrop.class.getResourceAsStream("/airdrop_shield.png")));
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
