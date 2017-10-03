package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;

/**
 * Implement simple robot that follows a target.
 *
 * @author fsjlepak
 */
public class SteerCrashShip extends CharacterBase {
    // Who robot is following

    private int frame, afterBurner1X, afterBurner1Y;
    private Character target;
    public double diameter = 20;
    // Speed of character
    private static final double RATE = 2;
    private int r = 142, g = 35, b = 35;
    private boolean bankLeft, bankRight;
    private double vecX, vecY;

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
        if (diameter<explodeTimer)
            die();
        if (hullPoints <= 0) {
            dieing = true;
        }
        if (dieing) {
            explodeTimer++;
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
        



        double x0 = getX();
        double y0 = getY();
        double x1 = target.getX();
        double y1 = target.getY();


        // Compute vector of length RATE from current location to target.
        double dx = x1 - x0;
        double dy = y1 - y0;
        double len = Math.sqrt(dx * dx + dy * dy);
        vecX = (RATE * dx / len)*2.5;
        vecY = RATE * dy / len;
        if (dieing)
            return;
        if (y0 > y1) {
            setCenter(x0, y0 + RATE * (2));
            vecX = 0;
            return;
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
        if (dieing) {
            if (explodeChange) {
                gc.setColor(Color.RED);
            } else {
                gc.setColor(Color.YELLOW);
            }
            gc.fillOval((int) (x-explodeTimer/2), (int) y, (int) explodeTimer, (int) explodeTimer);
        } else {
            Color color1 = new Color(255, 0, 0, 175);
            gc.setColor(color1);
            gc.fillOval((int) afterBurner1X - (int) (DRAWDIAM / 2), (int) afterBurner1Y - (int) ((DRAWDIAM)), (int) DRAWDIAM, (int) DRAWDIAM);

            if (vecX < -.3) {
                bankLeft = true;
            }

            if (vecX > .3) {
                bankRight = true;
            }

            int[] x = new int[3];
            int[] y = new int[3];
            int n;  // count of points
            // Make a triangle
            x[0] = (int) x1 + (int) DRAWDIAM / 2;
            x[1] = (int) x1 + (int) (DRAWDIAM * 1.5) + checkBankRight() - checkBankLeft() / 4;
            x[2] = (int) x1 - (int) (DRAWDIAM / 2) + checkBankLeft() - checkBankRight() / 4;
            y[0] = (int) y1 + (int) DRAWDIAM * 2;
            y[1] = (int) y1 - (int) DRAWDIAM / 3;
            y[2] = (int) y1 - (int) DRAWDIAM / 3;

//+ checkBankRight() - checkBankLeft() / 3
//+ checkBankLeft() - checkBankRight() / 3
            n = 3;
            Polygon myTri = new Polygon(x, y, n);  // a triangle   

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

            bankLeft = false;
            bankRight = false;
        }

    }
}
