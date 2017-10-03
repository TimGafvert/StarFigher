package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;

/**
 * Implement simple robot that follows a target.
 *
 * @author fsjlepak
 */
public class CannonFrigate extends CharacterBase {
    // Who robot is following

    Random rand = new Random();
    private int frame, afterBurner1X, afterBurner1Y, bulletTimer = 0,
            bulletTimerMax = 6, ammo, reload, burst, brighten;
    private boolean firing;
    private Character target;
    private double vecX, vecY, explosionDiameter= 60;

    // Speed of character
    private static final double RATE = .6;

    public double getDiameter() {
        return 100;
    }

    public int getLevel() {
        return 3;
    }

    public int ammotype() {
        return 0;
    }

    public CannonFrigate(int x0, int y0, Character target) {
        super(x0, y0);
        this.target = target;
        hullPoints = 10000;

    }

    public int getBulletTimer() {
        return bulletTimer;
    }

    public void resetBulletTimer() {
        bulletTimer = 0;
        if (!(burst > 0) && (ammo > 0)) {
            ammo--;
        }
    }

    public int getBulletTimerMax() {
        return bulletTimerMax;
    }

    public int giveBounty() {
        return 500;
    }

    public void update() {
        if (!isAlive()) {
            return;
        }
        if (explosionDiameter < explodeTimer) {
            die();
        }
        if (getDiameter() >= explodeTimer) {
            diameter++;
        }
        if (hullPoints <= 0) {
            dieing = true;
        }

        if (explodeTimer > 0) {
            if (explodeTimer % 3 == 0) {
                explodeChange = !explodeChange;
            }
        }
        if (dieing) {
            explodeTimer++;
            return;
        }
        if (ammo == 0) {
            firing = false;
            reload++;
        }
        if (burst > 0) {
            burst--;
            bulletTimer = bulletTimerMax;
        }
        if (reload == 150) {
            reload = 0;
            ammo = 50;
            firing = true;
        }
        if (reload == 60) {
            burst = 20;
        }
        if (firing) {
            bulletTimer++;
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

        if (!(y0 > 200)) {
            setCenter(x0, y0 + RATE);
        } else if (!(y0 < 650)) {
            setCenter(x0, y0 - RATE);
        } else {
            double x1 = target.getX();
            double y1 = target.getY();
            // Compute vector of length RATE from current location to target.
            double dx = x1 - x0;
            double dy = y1 - y0;
            double len = Math.sqrt(dx * dx + dy * dy);
            vecX = RATE * dx / len;
            vecY = RATE * dy / len;
            if (dieing) {
                return;
            }
            // Update based on direction vector
            setCenter(x0, y0 + vecY * 3);
        }

    }

    public void draw(Graphics2D gc) {
        // Offset the corner of our circle so it's drawn with the correct 
        // center.
        double DRAWDIAM = (8 * getDiameter() / 10);
        double x1 = x - DRAWDIAM / 2;
        double y1 = y - DRAWDIAM / 2;

        if (frame > 1) {
            afterBurner1X = (int) x;
            afterBurner1Y = (int) y;
            frame = 0;
        } else {
            frame++;
        }
        if (dieing) {
            if (explodeChange) {
                gc.setColor(Color.RED);
            } else {
                gc.setColor(Color.YELLOW);
            }
            gc.fillOval((int) (x - explodeTimer / 2), (int) (y - explodeTimer / 2), (int) explodeTimer, (int) explodeTimer);
        } else {
            Color color1 = new Color(255, 0, 0, 75);
            gc.setColor(color1);
            gc.fillOval((int) afterBurner1X - (int) (5.5 * DRAWDIAM / 10), (int) afterBurner1Y - (int) (2 * (DRAWDIAM / 3)), (int) DRAWDIAM / 3, (int) DRAWDIAM / 2);
            gc.fillOval((int) afterBurner1X + (int) (DRAWDIAM / 4), (int) afterBurner1Y - (int) (2 * (DRAWDIAM / 3)), (int) DRAWDIAM / 3, (int) DRAWDIAM / 2);

            if (!colorSwap) {
                brighten = 0;
            } else {
                brighten = 40;
            }

//        156	102	31
//	255	153	18
            Color trim = new Color(50 + brighten, 50 + brighten, 50 + brighten, 255);
            Color thrusters = new Color(156 + brighten, 102 + brighten, 31 + brighten, 255);
            Color hull = new Color(175, 100 + brighten, 0 + brighten, 255);
            Color gun = new Color(25 + brighten, 25 + brighten, 25 + brighten, 255);
            Color mound = new Color(210 + brighten, 180 + brighten, 140 + brighten, 255);
            Graphics2D g2 = (Graphics2D) gc;
            g2.setStroke(new BasicStroke(3));
            gc.setColor(thrusters);
            gc.fillRect((int) x - 45, (int) y - 40, 25, 70);
            gc.setColor(trim);
            gc.drawRect((int) x - 45, (int) y - 40, 25, 70);
            gc.setColor(thrusters);
            gc.fillOval((int) x - 44, (int) y + 20, (int) 25, (int) 20);

            gc.fillRect((int) x + 20, (int) y - 40, 25, 70);
            gc.setColor(trim);
            gc.drawRect((int) x + 20, (int) y - 40, 25, 70);
            gc.setColor(thrusters);
            gc.fillOval((int) x + 20, (int) y + 20, (int) 25, (int) 20);

            gc.setColor(hull);
            gc.fillOval((int) x1, (int) y1, (int) DRAWDIAM, (int) DRAWDIAM);

            g2.setColor(mound);
            g2.fillOval((int) x1 + 15, (int) y1 + 15, (int) DRAWDIAM - 30, (int) DRAWDIAM - 30);

            gc.setColor(trim);
            gc.drawOval((int) x1, (int) y1, (int) DRAWDIAM, (int) DRAWDIAM);

            g2.drawOval((int) x1 + 15, (int) y1 + 15, (int) DRAWDIAM - 30, (int) DRAWDIAM - 30);

            double x2 = getX();
            double y2 = getY();
            double x3 = target.getX();
            double y3 = target.getY();
            double dx = x3 - x2;
            double dy = y3 - y2;
            double len = Math.sqrt(dx * dx + dy * dy);
            double vecX = 10 * dx / len;
            double vecY = 10 * dy / len;

            gc.setColor(gun);
            g2.setStroke(new BasicStroke(6));
            g2.drawLine((int) (getX() + vecX), (int) (getY() + vecY), (int) (getX() - vecX), (int) (getY() - vecY));
            g2.setStroke(new BasicStroke(1));

        }
    }

    @Override
    public Color getColor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
