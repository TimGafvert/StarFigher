package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;


public class Missile extends CharacterBase {
   

    private int frameRear, frameBurn, afterBurner1X, afterBurner1Y, rearOfMissileX, rearOfMissileY;

    // Speed of character
    private static final double RATE = 8;
    private int r = 150, g = 150, b = 150, mx, my;
    private boolean bankLeft, bankRight, running;
    private double vecX, vecY, explosionDiameter = 120, collisionDamage = 1, diameter =20;
    private boolean finalVecSet;

    public double getCollisionDamage() {
        return collisionDamage;
    }

    public boolean isProjectile() {
        return true;
    }

    public double getDiameter() {
        return diameter;
    }

    public Missile(int x0, int y0, int mx, int my) {
        super(x0, y0);
        this.mx = mx;
        this.my = my;
        hullPoints = 1;
        rearOfMissileX = (int) x;
        rearOfMissileY = (int) y;
        afterBurner1X = (int) x;
        afterBurner1Y = (int) y;
        Sound.play("MissileWhoosh.wav");
    }

    public int ammotype() {
        return 100;
    }

    public boolean isTargetable() {
        return false;
    }

    public void update() {
        if (!isAlive()) {
            return;
        }
        if (explosionDiameter < explodeTimer) {
            die();
        }

        if (hullPoints <= 0 && !dieing) {
            dieing = true;
            Sound.play("MissileExplosion.wav");
        }
        if (dieing) {
            explodeTimer = explodeTimer + 6;
            if (getDiameter() >= explodeTimer) {
                diameter = diameter + 6;
                collisionDamage = collisionDamage + 1;
            }
        }

        if (explodeTimer > 0) {
            if (explodeTimer % 9 == 0) {
                explodeChange = !explodeChange;
            }
        }
        if (flashingTimer == 0) {
            colorSwap = false;
        }
        if (flashingTimer > 0) {
            if (flashingTimer % 3 == 0) {
                colorSwap = !colorSwap;
            }
            flashingTimer--;

        }

        double x0 = getX();
        double y0 = getY();

        // Compute vector of length RATE from current location to target.
        if (!finalVecSet) {
            double dx = mx - x0;
            double dy = my - y0;
            double len = Math.sqrt(dx * dx + dy * dy);
            vecX = RATE * dx / len;
            vecY = RATE * dy / len;
        }

        // Compute vector of length RATE from current location to target.
        // Update based on direction vector
        if (dieing) {
            return;
        }
        setCenter(x0 + vecX, y0 + vecY);
        finalVecSet = true;

    }

    @Override
    public Color getColor() {

        Color ship = new Color(r, g, b, 255);
        if (isAlive()) {
            if (!colorSwap) {
                return ship;
            } else {
                return Color.WHITE;
            }
        } else {
            return Color.GRAY;
        }
    }

    public void draw(Graphics2D gc) {
        // Offset the corner of our circle so it's drawn with the correct 
        // center.
        double DRAWDIAM = (8 * 10 / 10);
        double x1 = x - DRAWDIAM / 2;
        double y1 = y - DRAWDIAM / 2;

        if (frameRear == 1) {
            rearOfMissileX = (int) x;
            rearOfMissileY = (int) y;
            frameRear = 0;
        } else {
            frameRear++;
        }

        if (frameBurn > 2) {
            afterBurner1X = (int) x;
            afterBurner1Y = (int) y;
            frameBurn = 0;
        } else {
            frameBurn++;
        }

        if (dieing) {
            if (explodeChange) {
                gc.setColor(Color.RED);
            } else {
                gc.setColor(Color.YELLOW);
            }
            gc.fillOval((int) (x - explodeTimer / 2), (int) (y - explodeTimer / 2), (int) explodeTimer, (int) explodeTimer);
        } else {
            Color color1 = new Color(255, 0, 0, 175);
            gc.setColor(color1);
            gc.fillOval((int) (afterBurner1X - (DRAWDIAM / 2) + 1), (int) (afterBurner1Y - (DRAWDIAM / 2) + 2), (int) (DRAWDIAM), (int) (DRAWDIAM));
//            gc.setColor(getColor());
//            gc.fillOval((int) x1, (int) y1, (int) DRAWDIAM, (int) DRAWDIAM);
//            gc.fillOval((int) rearOfMissileX - (int) (DRAWDIAM / 2), (int) rearOfMissileY - (int) ((DRAWDIAM) / 2), (int) (DRAWDIAM  ), (int) (DRAWDIAM));
            Graphics2D g3 = (Graphics2D) gc;
            g3.setColor(getColor());
            g3.setStroke(new BasicStroke(5));
            g3.drawLine((int) rearOfMissileX, (int) rearOfMissileY, (int) x, (int) y);
            g3.setStroke(new BasicStroke(1));

        }
    }
}
