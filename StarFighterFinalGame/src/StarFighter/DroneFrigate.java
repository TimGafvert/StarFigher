package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;


public class DroneFrigate extends CharacterBase {
  
    Random rand = new Random();
    private int frame, afterBurner1X, afterBurner1Y, bulletTimer = 0,
            bulletTimerMax = 100;
    private boolean firing;
    private Character target;
    // Speed of character
    private static final double RATE = .6;
    private int brighten = 30;
    private double vecX, vecY;

    public double getDiameter() {
        return 100;
    }

    public int getLevel() {
        return 3;
    }

    public int ammotype() {
        return 2;
    }

    public DroneFrigate(int x0, int y0, Character target) {
        super(x0, y0);
        this.target = target;
        hullPoints = 10000;
    }

    public int getBulletTimer() {
        return bulletTimer;
    }

    public void resetBulletTimer() {
        bulletTimer = 0;

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
        if (getDiameter() < explodeTimer) {
            die();
        }
        if (hullPoints <= 0 && !dieing) {
            dieing = true;
            Sound.play("BigExplosion.wav");
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
        bulletTimer += 2;

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

        if (!(y0 > 100)) {
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
            setCenter(x0 + vecX * 3, y0);
        }

    }

    public void draw(Graphics2D gc) {
        // Offset the corner of our circle so it's drawn with the correct 
        // center.
        double DRAWDIAM = (getDiameter());
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
            Color color1 = new Color(0, 191, 255, 75);
            gc.setColor(color1);
            gc.fillOval((int) afterBurner1X - (int) ((2 * DRAWDIAM / 3) / 2), (int) afterBurner1Y - (int) (7 * (DRAWDIAM / 12)), (int) (2 * DRAWDIAM / 3), (int) (2 * DRAWDIAM / 3));

            if (!colorSwap) {
                brighten = 0;
            } else {
                brighten = 40;
            }
            // Note that even though getColor() must be defined by the subclass,
            // it can still be called by other methods implemented in the abstract
            // class.
            Color color2 = new Color(119 + brighten, 117 + brighten, 117 + brighten, 255);
            gc.setColor(color2);
            gc.fillOval((int) x1, (int) y1, (int) DRAWDIAM, (int) DRAWDIAM);
            gc.fillRect((int) x - 30, (int) y - 50, (int) 60, (int) 30);
            Color color3 = new Color(185 + brighten, 181 + brighten, 181 + brighten, 255);
            gc.setColor(color3);
            gc.fillOval((int) x1 + 10, (int) y1 + 10, (int) (4 * DRAWDIAM / 5), (int) (4 * DRAWDIAM / 5));

            gc.setColor(color2);
            gc.fillRect((int) x + 5, (int) y - 17, 20, 40);
            gc.setColor(Color.RED);
            gc.fillOval((int) x + 12, (int) y - 10, (int) 7, (int) 7);
            gc.fillOval((int) x + 12, (int) y + 10, (int) 7, (int) 7);
            Color inside = new Color(75 + brighten, 75 + brighten, 75 + brighten, 255);
            gc.setColor(inside);
            gc.fillRect((int) x - 20, (int) y - 10, (int) 20, (int) 20);
            Color makingDrone = new Color(150, 150, 150);
            gc.setColor(makingDrone);
            double makingDroneDIAM = .12;

//        gc.fillOval((int) x - 16, (int) y-10, 12, 12);
//        gc.fillOval((int) x - 16, (int) y + 6, 12, 12);
            gc.fillOval((int) ((x - 8) - 2 * bulletTimer * makingDroneDIAM / 3),
                    (int) ((y - 10) + 2 * bulletTimer * makingDroneDIAM / 3),
                    (int) (bulletTimer * makingDroneDIAM / 2) + 4,
                    (int) (bulletTimer * makingDroneDIAM / 2) + 4);
            Font font = new Font("Serif", Font.BOLD, 12);
            gc.setFont(font);
            int intHP = (int) hullPoints;
            int intMaxHP = (int) 10000;
            gc.setColor(Color.WHITE);
            gc.drawString(intHP + "/" + intMaxHP, (int) x-30, (int) y +50);

        }
    }

    @Override
    public Color getColor() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
