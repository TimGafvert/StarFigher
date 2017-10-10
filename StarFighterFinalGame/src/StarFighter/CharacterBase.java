package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Abstract class that provides a default implementation for most Character
 * methods. Subclasses must provide a method to update object state each frame,
 * and define color for drawing.
 *
 * 
 */
public abstract class CharacterBase implements Character {

    
    public double diameter = 10;
    public static final int defaultBounty = 0;
    public double hullPoints;
    private boolean alive;
    public boolean colorSwap,dieing,explodeChange;
    public int flashingTimer,explodeTimer,explodeSize;
    public double defaultCollisionDamage = 1, RATE = 2;
    // Center of circle
    public double x, y;
    public Color redExplosion, yellowExplosion;

    @Override
    public double getCollisionDamage() {
        return defaultCollisionDamage;
    }
    
    
    public boolean isDieing() {
        return dieing;
    }
    
    public boolean isProjectile() {
        return false;
    }
    
    public int giveBounty() {
        return defaultBounty;
    }

    public boolean isTargetable() {
        return true;
    }
    
    public int getLevel() {
        return 1;
    }
    
    public int ammotype() {
      return 0;  
    }
    

    public CharacterBase(int x0, int y0) {
        x = x0;
        y = y0;
        alive = true;
        hullPoints = 100;     
        redExplosion = new Color(255, 0, 0, 200);
        yellowExplosion = new Color(255, 255, 0, 200);
    }

    // Must implement in subclasses.
    public abstract void update();

    // Must implement in subclasses.
    /**
     * Called to get color used to draw character.
     *
     * @return color of character
     */
    public abstract Color getColor();

    public double getDiameter() {
        return diameter;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setCenter(double cx, double cy) {
        x = cx;
        y = cy;
    }

    public double dist(double x0, double y0, double x1, double y1) {
        double dx = x0 - x1;
        double dy = y0 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }

    public boolean collidesWith(Character c) {
        double d = dist(x, y, c.getX(), c.getY());
        double radiusSum = (getDiameter() + c.getDiameter()) / 2.0;
        return d < radiusSum;
    }

    public void interactWith(Character c, double d) {
        if (collidesWith(c)) {
            takeDamage(d);
        }
    }

    public boolean isAlive() {
        return alive;
    }

    public int getBulletTimer() {
        return 0;
    }

    public int getBulletTimerMax() {
        return 0;
    }

    public void resetBulletTimer() {
        return;
    }

    public void takeDamage(double d) {
        hullPoints -= d;
       
        if (flashingTimer < 16) {
            flashingTimer = 18;
        }
        
    }
    

    @Override
    public void die() { 
        alive = false;
    }

    public void draw(Graphics2D g) {
        // Offset the corner of our circle so it's drawn with the correct 
        // center.
        
        double DIAM = getDiameter();
        double x1 = x - DIAM / 2;
        double y1 = y - DIAM / 2;
        // Note that even though getColor() must be defined by the subclass,
        // it can still be called by other methods implemented in the abstract
        // class.
        g.setColor(getColor());
        g.fillOval((int) x1, (int) y1, (int) DIAM, (int) DIAM);

    }
}
