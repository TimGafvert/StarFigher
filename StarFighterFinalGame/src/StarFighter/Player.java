package StarFighter;

import java.awt.*;

public class Player extends CharacterBase {

    public double dx, dy, dxOld, dyOld, HP, EP, speed = 2,
            maxEP = 100, maxHP = 100, genEP = .5, armor;
    public int credits = 00000, missiles = 20;
    public boolean bankLeft, bankRight, shieldON, laserFiring;
    private int frame;
    private int afterBurner1X = 0, afterBurner2X = 0, afterBurner3X = 0;
    private int afterBurner1Y = 0, afterBurner2Y = 0, afterBurner3Y = 0;
    private int r = 0, g = 0, b = 205;
    private int r2 = 139, g2 = 139, b2 = 131;
    public double collisionDamage = 5;

    public Player(int x, int y) {
        super(x, y);
        dx = 0;
        dy = 0;
        HP = maxHP;
        EP = maxEP;
    }

    public void takeDamage(double d) {
        if (d < 0) {
            HP += maxHP / (100 / -d);
        } else {

            if (shieldON) {
                if (d > EP) {
                    EP = 0;
                    shieldON = false;
                    takeDamage(d - EP);
                }
                EP -= d;
            } else {
                double r = 0;
                if (!(d <= 0)) {
                    r = d * armor;
                }
                HP -= (d - r);
            }

            if (flashingTimer < 16) {
                flashingTimer = 18;
            }
            if (HP <= 0) {
                die();
            }
        }
    }

    public double getDiameter() {
        return 20;
    }

    public void setDirection(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double getEP() {
        return EP;
    }

    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }
        if (HP > maxHP) {
            HP = maxHP;
        }
        if (EP <= 1) {
            shieldON = false;
        }
        if (!(EP >= maxEP) && (!shieldON) && (!laserFiring)) {
            EP += genEP;
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
        if (dx == 0 && dy == 0) {
            return;
        }

        double len = Math.sqrt(dx * dx + dy * dy);
        double vecX = (speed * dx / len) * 1.5;
        double vecY = speed * dy / len;
        // Update based on direction vector
        double x0 = getX();
        double y0 = getY();
        dxOld = x0;
        dyOld = y0;
        setCenter(x0 + vecX, y0 + vecY);
    }

    public double getCollisionDamage() {

        return collisionDamage;
    }

    @Override
    public Color getColor() {
        Color ship = new Color(r, g, b, 255);
        Color ship2 = new Color(r + 90, g + 90, b + 50, 255);

        if (isAlive()) {
            if (!colorSwap) {
                return ship;
            } else { 
                return ship2;
            }
        } else {
            return Color.RED;
        }
    }

    public int checkBankLeft() {
        if (bankLeft) {
            return (int) (getDiameter() / 2);
        } else {
            return 0;
        }
    }

    public int checkBankRight() {
        if (bankRight) {
            return (int) -(getDiameter() / 2);
        } else {
            return 0;
        }
    }

    public void draw(Graphics2D gc) {
        // Offset the corner of our circle so it's drawn with the correct 
        // center.
        double DIAM = getDiameter();
        double x1 = x - DIAM / 2;
        double y1 = y - DIAM / 2;

//        afterBurner3X = afterBurner2X;
//            afterBurner3Y = afterBurner2Y;
//            afterBurner2X = afterBurner1X;
//            afterBurner2Y = afterBurner1Y;
//            afterBurner1X = (int) dxOld;
//            afterBurner1Y = (int) dyOld;
//
        if (frame > 1) {
            afterBurner3X = afterBurner2X;
            afterBurner3Y = afterBurner2Y;
            afterBurner2X = afterBurner1X;
            afterBurner2Y = afterBurner1Y;
            afterBurner1X = (int) x;
            afterBurner1Y = (int) y;
            frame = 0;
        } else {
            frame++;
        }
        Color color1 = new Color(255, 0, 0, 175);
        gc.setColor(color1);
        gc.fillOval(afterBurner1X - (int) (DIAM / 2), afterBurner1Y - (int) (DIAM / 3), (int) DIAM, (int) DIAM);
        if (!(dy == 1)) {
            gc.fillOval(afterBurner2X - (int) (DIAM / 2.8), afterBurner2Y - (int) (DIAM / 10), (int) (3 * DIAM / 4), (int) (3 * DIAM / 4));
            gc.fillOval(afterBurner3X - (int) (DIAM / 4), afterBurner3Y, (int) (DIAM / 2), (int) (DIAM / 2));

        } else {
            afterBurner3X = (int) x;
            afterBurner3Y = (int) y;
            afterBurner2X = (int) x;
            afterBurner2Y = (int) y;
        }
        if (dx == -1) {
            bankLeft = true;
        }
        if (dx == 1) {
            bankRight = true;
        }
        int[] xt = new int[3];
        int[] yt = new int[3];
        int n;  // count of points
        // Make a triangle

        xt[0] = (int) x1 + (int) DIAM / 2;
        xt[1] = (int) x1 + (int) (7 * DIAM / 4) + checkBankRight() - checkBankLeft() / 3;
        xt[2] = (int) x1 - (int) (3 * DIAM / 4) + checkBankLeft() - checkBankRight() / 3;
        yt[0] = (int) y1 - (int) DIAM / 4;
        yt[1] = (int) y1 + (int) DIAM;
        yt[2] = (int) y1 + (int) DIAM;
        n = 3;
        Polygon myTri = new Polygon(xt, yt, n);
        gc.setColor(Color.LIGHT_GRAY);
        if (!colorSwap) {
            Color ship = new Color(r2, g2, b2, 255);
            gc.setColor(ship);
        } else {
            Color ship = new Color(r2 + 90, g2 + 90, b2 + 50, 255);
            gc.setColor(ship);
        }
        gc.fillPolygon(myTri);

        gc.setColor(getColor());
        if (bankRight) {
            gc.fillOval((int) (x1 + DIAM / 4), (int) (y1), (int) (2 * DIAM / 3), (int) (DIAM));
        } else if (bankLeft) {
            gc.fillOval((int) (x1 + DIAM / 16), (int) (y1), (int) (2 * DIAM / 3), (int) (DIAM));
        } else {
            gc.fillOval((int) x1, (int) y1, (int) DIAM, (int) DIAM);
        }

        Color HPcolor = new Color(50, 205, 50, 255);
        gc.setColor(HPcolor);
        gc.drawRect(25, 250 - (int) maxHP, 20, (int) maxHP);
        gc.fillRect(25, 250 - (int) HP, 20, (int) HP);

        Color EPcolor = new Color(0, 191, 255, 255);
        gc.setColor(EPcolor);
        gc.drawRect(50, 250 - (int) maxEP, 20, (int) maxEP);
        gc.fillRect(50, 250 - (int) EP, 20, (int) EP);

        Color shieldColor = new Color(70, 130, 180, 100);
        gc.setColor(shieldColor);
        if (shieldON) {
            gc.fillOval((int) (x - 30), (int) (y - 30), (int) (60), (int) (60));
        }

        bankLeft = false;
        bankRight = false;
    }
}
