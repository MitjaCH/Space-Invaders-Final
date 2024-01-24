package ch.bbcag.bbcspaceinvader.gameobject.objects;

import ch.bbcag.bbcspaceinvader.enums.EnumDirections;
import javafx.geometry.BoundingBox;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.List;

import ch.bbcag.bbcspaceinvader.gameobject.GameObject;
import ch.bbcag.bbcspaceinvader.common.Const;
import ch.bbcag.bbcspaceinvader.gameobject.objects.Lootdrop;

public class Alienship extends GameObject {

    private static final double SPEED = Const.SPEED;
    private static final double HORIZONTAL_BORDER_RIGHT = 690;
    private static final double HORIZONTAL_BORDER_LEFT = 10;
    private EnumDirections currentDirection = EnumDirections.RIGHT;
    private List<Bomb> bombs;
    private List<Lootdrop> lootdrops;
    private List<Lootdropitem> lootdropsitem;

    public Alienship(double x, double y, List<Bomb> bombs, List<Lootdrop> lootdrops, List<Lootdropitem> lootdropsitem) {
        super(x, y, new Image(Alienship.class.getResourceAsStream("/alienship.png")));
        this.bombs = bombs;
        this.lootdrops = lootdrops;
        this.lootdropsitem = lootdropsitem;
    }

    @Override
    public void update(double deltaTimeInSec) {
        if (isBatteryFull()) {
            throwBomb();
        }
        if (isBatteryFull2()) {
            dropLootdrop();
        }
        if (isBatteryFull3()){
            dropLootdropitem();
        }

        changeDirectionIfRequired();
        moveInCurrentDirection(deltaTimeInSec);
    }

    private void throwBomb() {
        bombs.add(new Bomb(getX() + getImage().getWidth() / 2, getY() + getImage().getHeight()));
    }
    public void dropLootdrop() {
        lootdrops.add(new Lootdrop(getX() + getImage().getWidth() / 2, getY() + getImage().getHeight()));
    }
    public void dropLootdropitem() {
        lootdropsitem.add(new Lootdropitem(getX() + getImage().getWidth() / 2, getY() + getImage().getHeight()));
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(getImage(), getX(), getY());
    }

    private void moveInCurrentDirection(double deltaTimeInSec) {
        double distanceToMove = deltaTimeInSec * SPEED;

        if (currentDirection == EnumDirections.LEFT)
            setX(getX() - distanceToMove);
        else
            setX(getX() + distanceToMove);
    }

    private void changeDirectionIfRequired() {
        if (getX() > HORIZONTAL_BORDER_RIGHT)
            currentDirection = EnumDirections.LEFT;

        if (getX() < HORIZONTAL_BORDER_LEFT)
            currentDirection = EnumDirections.RIGHT;
    }

    public BoundingBox getBoundingBox() {
        return new BoundingBox(getX(), getY(), getImage().getWidth() - 10, getImage().getHeight() - 10);
    }

    private boolean isBatteryFull() {
        return Math.random() < 0.005;
    }
    private boolean isBatteryFull2(){
        return Math.random() < 0.00052;
    }

    private boolean isBatteryFull3(){
        return Math.random() < 0.00015;
    }

}
