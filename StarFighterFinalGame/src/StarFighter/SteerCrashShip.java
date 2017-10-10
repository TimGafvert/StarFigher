package StarFighter;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

public class SteerCrashShip extends CharacterBase {
    // Who robot is following

    private int frame, afterBurner1X, afterBurner1Y;
    private Character target;
    public double diameter = 20;
    // Speed of character
//    private double RATE = 2;
    private int r = 142, g = 35, b = 35;
    private boolean bankLeft, bankRight;
    private double vecX, vecY, x0, y0, x1, y1;

    public double getCollisionDamage() {
        return 5;
    }

    public void upgradeHP() {
    }

    public void upgradeEP() {
    }

    public void upgradeSpeed() {
    }

    public double getDiameter() {
        return 25;
    }

    public int giveBounty() {
        return 50;
    }

    public SteerCrashShip(int x0, int y0, Character target) {
        super(x0, y0);
        this.target = target;
        hullPoints = 200;
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
        if (dieing) {
            explodeTimer += 4;
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
        //get current position
        x0 = getX();
        y0 = getY();
        if (!dieing) {

            x1 = target.getX();
            y1 = target.getY();
            // Compute vector of length RATE from current location to target.
            double dx = x1 - x0;
            double dy = y1 - y0;
            double len = Math.sqrt(dx * dx + dy * dy);
            vecX = (RATE * dx / len) * 2.5;
            vecY = RATE * dy / len;

            if (y0 > y1) {
                setCenter(x0, y0 + RATE * (2));
                vecX = 0;
                return;
            }
        }
        // Update based on direction vector
        setCenter(x0 + vecX, y0 + RATE * (2));

    }

    @Override
    public Color getColor() {

        Color ship = new Color(r + 40, g + 40, b + 40, 255);
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

    public int checkBankLeft() {
        if (bankLeft) {
            int DRAWDIAM = (int) (8 * diameter / 10);
            return +(DRAWDIAM / 2);
        } else {
            return 0;
        }
    }

    public int checkBankRight() {
        if (bankRight) {
            int DRAWDIAM = (int) (8 * diameter / 10);
            return -(DRAWDIAM / 2);
        } else {
            return 0;
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
        gc.fillOval((int) afterBurner1X - (int) (DRAWDIAM / 2), (int) afterBurner1Y - (int) ((DRAWDIAM)), (int) DRAWDIAM, (int) DRAWDIAM);

        if (vecX < -.3) {
            bankLeft = true;
        }

        if (vecX > .3) {
            bankRight = true;
        }

        int[] xT = new int[3];
        int[] yT = new int[3];
        int n;  // count of points
        // Make a triangle

        xT[0] = (int) x1 + (int) DRAWDIAM / 2;
        xT[1] = (int) x1 + (int) (DRAWDIAM * 1.5) + checkBankRight() - checkBankLeft() / 4;
        xT[2] = (int) x1 - (int) (DRAWDIAM / 2) + checkBankLeft() - checkBankRight() / 4;
        yT[0] = (int) y1 + (int) DRAWDIAM * 2;
        yT[1] = (int) y1 - (int) DRAWDIAM / 3;
        yT[2] = (int) y1 - (int) DRAWDIAM / 3;

//+ checkBankRight() - checkBankLeft() / 3
//+ checkBankLeft() - checkBankRight() / 3
        n = 3;
        Polygon myTri = new Polygon(xT, yT, n);  // a triangle   

        if (!colorSwap) {
            Color ship = new Color(r, g, b, 255);
            gc.setColor(ship);
        } else {
            Color ship = new Color(r + 90, g + 90, b + 90, 255);
            gc.setColor(ship);
        }

        gc.fillPolygon(myTri);
        gc.setColor(getColor());

        if (bankRight) {
            gc.fillOval((int) (x1 + DRAWDIAM / 4), (int) (y1), (int) (2 * DRAWDIAM / 3), (int) (DRAWDIAM));
        } else if (bankLeft) {
            gc.fillOval((int) (x1 + DRAWDIAM / 16), (int) (y1), (int) (2 * DRAWDIAM / 3), (int) (DRAWDIAM));
        } else {
            gc.fillOval((int) x1, (int) y1, (int) DRAWDIAM, (int) DRAWDIAM);
        }
        if (dieing) {
            if (explodeChange) {
                gc.setColor(redExplosion);
            } else {
                gc.setColor(yellowExplosion);
            }
            gc.fillOval((int) (x - explodeTimer / 2), (int) y - explodeTimer / 2, (int) explodeTimer, (int) explodeTimer);
        }
        bankLeft = false;
        bankRight = false;

    }

}
