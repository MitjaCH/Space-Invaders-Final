package ch.bbcag.bbcspaceinvader.common;

import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class KeyEventHandler implements EventHandler<KeyEvent> {
    private final KeyControllable keyControllable;

    public KeyEventHandler(KeyControllable keyControllable) {
        this.keyControllable = keyControllable;
    }

    @Override
    public void handle(KeyEvent event) {
        KeyCode code = event.getCode();

        switch (code) {
            case LEFT:
                keyControllable.setLeftKeyPressed(true);
                break;
            case RIGHT:
                keyControllable.setRightKeyPressed(true);
                break;
            case SPACE:
                keyControllable.setSpaceBarPressed(true);
                break;
            case X:
                keyControllable.setXKeyPressed(true);
            default:
        }
    }
}
