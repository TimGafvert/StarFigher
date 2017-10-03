package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

/**
 * Implement simple robot that follows a target.
 *
 * @author fsjlepak
 */
public class PowerUp_Missiles extends CharacterBase {
    // Who robot is following

    private Character target;
    // Speed of character

    @Override
    public double getCollisionDamage() {
        return 0;
    }

    public boolean isProjectile() {
        return true;
    }
    
    public boolean isTargetable() {
        return false;
    }
    
    private double RATE = 1;
//    private double RATE = 2;

    public double getDiameter() {
        if (colorSwap) {
            return 50;
        } else {
            return 30;
        }
    }
    public int ammotype() {
      return -2;  
    }

    public PowerUp_Missiles(int x0, int y0, Character target) {
        super(x0, y0);
        this.target = target;
    }

    public void interactWith(Character c, double d) {
        if (collidesWith(c)) {
            takeDamage(d);
        }
    }

    public void takeDamage(double d) {
        die();
    }

    public void update() {
        if (!isAlive()) {
            return;
        }

        if (flashingTimer < 5) {
            flashingTimer++;
            if (flashingTimer == 5) {
                colorSwap = !colorSwap;
                flashingTimer = 0;
            }

        }

        double x0 = getX();
        double y0 = getY();

        setCenter(x0, y0 + RATE);
    }

    @Override
    public Color getColor() {        
        if (colorSwap) {
            return Color.RED;
        } else {
            return Color.WHITE;
        }
    }
    public void draw(Graphics2D g) {
        // Offset the corner of our circle so it's drawn with the correct 
        // center.
        double DIAM = getDiameter()/5;
        double x1 = x - DIAM / 2;
        double y1 = y - DIAM / 2;
        // Note that even though getColor() must be defined by the subclass,
        // it can still be called by other methods implemented in the abstract
        // class.
        g.setColor(getColor());
        g.fillOval((int) x1, (int) y1, (int) DIAM, (int) DIAM);

    }
}

