package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class LaserBurst extends CharacterBase {
   

    private Character target;
    // Speed of character
    private static final double RATE = 8;
    private int blinkCounter = 0;
    private boolean finalVecSet;
    private int frame = 0;
    double vecX, vecY;
    public double collisionDamage = 40;

    public LaserBurst(int x0, int y0, Character target) {
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

       // check for the 0.999 to know that this was an evade.
    public void takeDamage(double d) {
       if (d != 4.999 && d != 24.999) {
            die();
        }
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



        if (blinkCounter == 1) {
            colorSwap = !colorSwap;
            blinkCounter = 0;
        } else {
            blinkCounter++;
        }
        // Compute vector of length RATE from current location to target.

        // Update based on direction vector
        setCenter(x0, y0 + 10);
    }

    @Override
    public Color getColor() {


        if (colorSwap == true) {
            Color l1 = new Color(100, 255, 255, 200);
            return l1;
        } else {
            Color l2 = new Color(0, 155, 155, 200);
            return l2;
        }


    }

    public void draw(Graphics2D gc) {


        Graphics2D g2 = (Graphics2D) gc;
        g2.setColor(getColor());
        g2.setStroke(new BasicStroke(5));
        g2.drawLine((int) getX(), (int) getY(), (int) getX(), (int) getY() + 5);
        g2.setStroke(new BasicStroke(1));

    }
}
