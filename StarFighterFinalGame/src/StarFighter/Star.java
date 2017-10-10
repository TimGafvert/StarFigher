package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;


public class Star extends CharacterBase {
 

    private Character target;
  
    Random rand = new Random();
    private double RATE = rand.nextDouble()*2+ .3;


    public double getDiameter() {
        return 7;
    }

    public Star(int x0, int y0, Character target) {
        super(x0, y0);
        this.target = target;
    }

    public void die() {
        return;
    }

    public void update() {
//        if (!isAlive()) {
//            return;
//        }
//        if (flashingTimer == 0) {
//            colorSwap = false;
//        }
//        if (flashingTimer > 0) {
//            if (flashingTimer % 3 == 0) {
//                colorSwap = !colorSwap;
//            }
//            flashingTimer--;
//
//        }

        double x0 = getX();
        double y0 = getY();

        setCenter(x0, y0 + RATE);
    }

    @Override
    public Color getColor() {
        Color dim = new Color(100, 100, 100, 255);
        Color bright = new Color(150, 150, 150, 255);
        if (RATE <= 1.3 ) {
            return dim;
        } else {
            return bright;
        }
    }

    @Override
    public int getBulletTimer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetBulletTimer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getBulletTimerMax() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
