package StarFighter;

/*
 * 
 */


import java.awt.event.KeyListener;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Defines methods required to control our game.
 * @author jlepak
 */
public interface GameControl {
    MouseListener getMouseListener();
    KeyListener getKeyListener();
    MouseMotionListener getMouseMotionListener();
}
