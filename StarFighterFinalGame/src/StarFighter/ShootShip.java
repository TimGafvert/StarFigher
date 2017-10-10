package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.Random;

public class ShootShip extends CharacterBase {

    Random rand = new Random();
    private int frame, afterBurner1X, afterBurner1Y, bulletTimer = rand.nextInt(60),
            bulletTimerMax = 60;
    private Character target;
    double diameter = 20;
    // Speed of character
    private double RATE = 4;
    private int r = 89, g = 119, b = 119;

    public double getDiameter() {
        return 25;
    }

    public ShootShip(int x0, int y0, Character target) {
        super(x0, y0);
        this.target = target;
        hullPoints = 75;
    }

    public int getBulletTimer() {
        return bulletTimer;
    }

    public void resetBulletTimer() {
        bulletTimer = 0;
    }

    public int ammotype() {
        return 1;
    }

    public int getBulletTimerMax() {
        return bulletTimerMax;
    }

    public int giveBounty() {
        return 50;
    }

    public void update() {
        if (!isAlive()) {
            return;
        }
        if (diameter * 2 < explodeTimer) {
            die();
        }
        if (hullPoints <= 0 && !dieing) {
            dieing = true;
            explodeTimer = 5;
            Sound.play("EnemyShipDeath.wav");
        }

        if (explodeTimer > 0) {
            if (explodeTimer % 3 == 0) {
                explodeChange = !explodeChange;
            }
        }

        double x0 = getX();
        double y0 = getY();

        setCenter(x0, y0 + RATE);
        if (dieing) {
            explodeTimer += 4;
            return;
        }
        bulletTimer++;
        if (flashingTimer == 0) {
            colorSwap = false;
        }
        if (flashingTimer > 0) {
            if (flashingTimer % 3 == 0) {
                colorSwap = !colorSwap;
            }
            flashingTimer--;

        }

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
        double DRAWDIAM = (8 * diameter / 10);
        double x1 = x - DRAWDIAM / 2;
        double y1 = y - DRAWDIAM / 2;

        if (frame > 1) {
            afterBurner1X = (int) x;
            afterBurner1Y = (int) y;
            frame = 0;
        } else {
            frame++;
        }

        Color color1 = new Color(255, 0, 0, 175);
        gc.setColor(color1);
        gc.fillOval((int) afterBurner1X - (int) (DRAWDIAM / 2), (int) afterBurner1Y - (int) (2 * (DRAWDIAM / 3)), (int) DRAWDIAM, (int) DRAWDIAM);
        int[] xT = new int[3];
        int[] yT = new int[3];
        int n;  // count of points
        // Make a triangle
        xT[0] = (int) x1 + (int) DRAWDIAM / 2;
        xT[1] = (int) x1 + (int) (DRAWDIAM * 1.5);
        xT[2] = (int) x1 - (int) DRAWDIAM / 2;
        yT[0] = (int) y1 + (int) DRAWDIAM * 2;
        yT[1] = (int) y1 - (int) DRAWDIAM / 3;
        yT[2] = (int) y1 - (int) DRAWDIAM / 3;

//        xT[0] = (int) x1 - 42 + 50;
//        xT[1] = (int) x1 - 42 + 69;
//        xT[2] = (int) x1 - 42 + 31;
//        yT[0] = (int) y1 - 55 + 85;
//        yT[1] = (int) y1 - 55 + 50;
//        yT[2] = (int) y1 - 55 + 50;
        n = 3;
        Polygon myTri = new Polygon(xT, yT, n);  // a triangle   

        if (!colorSwap) {
            Color ship = new Color(r - 40, g - 40, b - 40, 255);
            gc.setColor(ship);
        } else {
            Color ship = new Color(r + 50, g + 50, b + 50, 255);
            gc.setColor(ship);
        }

        gc.fillPolygon(myTri);

        // Note that even though getColor() must be defined by the subclass,
        // it can still be called by other methods implemented in the abstract
        // class.
        gc.setColor(getColor());
        gc.fillOval((int) x1, (int) y1, (int) DRAWDIAM, (int) DRAWDIAM);
        if (dieing) {
            if (explodeChange) {
                gc.setColor(redExplosion);
            } else {
                gc.setColor(yellowExplosion);
            }
            gc.fillOval((int) (x - explodeTimer / 2), (int) y - explodeTimer / 2, (int) explodeTimer, (int) explodeTimer);
        }

    }
}
