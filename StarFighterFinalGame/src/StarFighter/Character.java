package StarFighter;

import java.awt.Graphics2D;

/**
 * Interface for general game character.
 *
 * 
 */
public interface Character {
    
    public boolean isDieing();

    /**
     * Called once per frame to update state of character.
     */
    void update();

    public int getBulletTimer();

    public int getBulletTimerMax();

    public void resetBulletTimer();
    
    public double getCollisionDamage();
    
    public boolean isProjectile();
    
    public int giveBounty();
    
    public boolean isTargetable();
    
    public int getLevel();
    
    public int ammotype();
    
    public double dist(double a, double b, double c, double d);
  

    /**
     * X-coordinate of center.
     *
     * @return x-coordinate value
     */
    double getX();

    /**
     * Y-coordinate of center.
     *
     * @return y-coordinate value
     */
    double getY();

    /**
     * Determine if character collides with another.
     *
     * @param c other character
     * @return true if characters have collided
     */
    boolean collidesWith(Character c);

    void interactWith(Character c,double d);

    /**
     * Determine if character is still alive.
     *
     * @return true if character is alive
     */
    boolean isAlive();

    /**
     * Set character state to dead.
     */
    void takeDamage(double d);

    void die();

    /**
     * Draw character on given graphics context.
     */
    void draw(Graphics2D g);

    /**
     * Size of character.
     *
     * @return diameter of circle
     */
    double getDiameter();
    
     
    
}
