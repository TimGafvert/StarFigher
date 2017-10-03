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
 *
 */
public class Bullet extends CharacterBase {
    // Who robot is following

    private Character target;
    // Speed of character
    private static final double RATE = 6;
    private int blinkCounter = 0;
    private boolean finalVecSet;
    private int frame = 0;
    double vecX, vecY;
    public double collisionDamage = 10;
    Random rand = new Random();

    public Bullet(int x0, int y0, Character target) {
        super(x0, y0);
        this.target = target;
    }

    public void interactWith(Character c, double d) {
        if (collidesWith(c)) {
            takeDamage(d);
        }
    }

    public boolean isTargetable() {
        return false;
    }

    public double getDiameter() {
        return 5;
    }

    public boolean isProjectile() {
        return true;
    }

    public void takeDamage(double d) {
        die();
    }

    @Override
    public double getCollisionDamage() {

        return collisionDamage;
    }

    public void update() {

        if (!isAlive()) {
            return;
        }
        double x0 = getX();
        double y0 = getY();
        double x1 = target.getX();
        double y1 = target.getY();


        if (blinkCounter == 1) {
            colorSwap = !colorSwap;
            blinkCounter = 0;
        } else {
            blinkCounter++;
        }
        double dx = (x1 - x0);
        double dy = (y1 - y0);
        double len = Math.sqrt(dx * dx + dy * dy);

        if (!finalVecSet) {
            dx = (x1 - x0) + rand.nextDouble() * len/2 - len/4;
            dy = (y1 - y0) + rand.nextDouble() * len/2 - len/4;
            len = Math.sqrt(dx * dx + dy * dy);
            vecX = (RATE * dx / len);
            vecY = (RATE * dy / len);
        }



        // Compute vector of length RATE from current location to target.

        // Update based on direction vector
        setCenter(x0 + vecX, y0 + vecY);
        finalVecSet = true;
    }

    @Override
    public Color getColor() {

        if (frame < 30) {
            if (colorSwap == true) {
                return Color.ORANGE;
            } else {
                return Color.darkGray;
            }
        } else {
            return Color.GRAY;
        }
    }
}
