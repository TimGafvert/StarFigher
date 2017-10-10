package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class Drone extends CharacterBase {

    private int frame, afterBurner1X, afterBurner1Y;
    private Character target;
    // Speed of character
    private static final double RATE = 3;
    private int r = 150, g = 150, b = 150, ammo, launchTimer;
    private boolean bankLeft, bankRight, running, firing, launched;
    private double vecX, vecY;

    public double getCollisionDamage() {
        return 1;
    }

    public int getLevel() {
        return 0;
    }

    public void upgradeHP() {
    }

    public void upgradeEP() {
    }

    public void upgradeSpeed() {
    }

    public double getDiameter() {
        return 15;
    }

    public int giveBounty() {
        return 0;
    }

    public Drone(int x0, int y0, Character target) {
        super(x0, y0);
        this.target = target;
        hullPoints = 10;
    }

    public boolean isTargetable() {
        return launched;
    }

    public void update() {
        if (!isAlive()) {
            return;
        }
        if (getDiameter() < explodeTimer) {
            Sound.play("EnemyShipDeath.wav");
            die();
        }
        
        if (hullPoints <= 0) {
            dieing = true;
        }
        if (dieing) {
            explodeTimer+=2;
        }

        if (explodeTimer > 0) {
            if (explodeTimer % 3 == 0) {
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

        if (launchTimer < 30) {
            launchTimer++;
        } else {
            launched = true;
        }

        double x0 = getX();
        double y0 = getY();
        double x1 = target.getX();
        double y1 = target.getY();

        // Compute vector of length RATE from current location to target.
        double dx = (x1 - x0);
        double dy = (y1 - y0);
        double len = Math.sqrt(dx * dx + dy * dy);

        if (!((y1 - y0) < 0)) {
            if (x0 > x1) {
                dx = ((x1 + (y1 - y0)) - x0);
            }
            if (x0 < x1) {
                dx = ((x1 - (y1 - y0)) - x0);
            }
            len = Math.sqrt(dx * dx + dy * dy);
        }

        double vecX = RATE * dx / (len);
        double vecY = RATE * dy / (len);

        if (len < 180 && !running) {
            firing = true;
        }
        if (len > 180) {
            firing = false;
        }

        if (len < 75 || ammo == 0) {
            running = true;
            firing = false;
            ammo = 50;
        }
        if (running == true) {
            vecX = -vecX;
            vecY = -vecY;
        }
        if (len > 300) {
            running = false;

        }
        // Update based on direction vector
        setCenter(x0 + vecX, y0 + vecY);
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
            gc.fillOval((int) (x - explodeTimer / 2), (int) y, (int) explodeTimer, (int) explodeTimer);
        } else {

            if (firing) {

                gc.setColor(Color.RED);
                Graphics2D g2 = (Graphics2D) gc;
                g2.setStroke(new BasicStroke(1));
                g2.drawLine((int) getX(), (int) getY(), (int) target.getX(), (int) target.getY());
                if ((int) getX() != (int) afterBurner1X) {
                    target.takeDamage(.1);
                    ammo--;
                }
            }

            Color color1 = new Color(255, 0, 0, 175);
            gc.setColor(color1);
            gc.fillOval((int) afterBurner1X - (int) (DRAWDIAM / 4), (int) afterBurner1Y - (int) ((DRAWDIAM) / 4), (int) (DRAWDIAM / 2), (int) (DRAWDIAM) / 2);
            gc.setColor(getColor());
            gc.fillOval((int) x1, (int) y1, (int) DRAWDIAM, (int) DRAWDIAM);
        }
    }
}
