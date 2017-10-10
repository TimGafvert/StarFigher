package StarFighter;

import java.awt.*;
import java.util.Random;

public class Player extends CharacterBase {

    public double dx, dy, dxOld, dyOld, HP, EP, speed = 2,
            maxEP = 100, maxHP = 100, genEP = .1, armor, laserDamage = 10;
    public int credits = 2000, missiles = 20, thrusters = 0, evasionTimer = 0;
    public boolean bankLeft, bankRight, shieldSwitch = true, shieldON, shieldMatrix, shieldPlasma, laserFiring, evading,
            missileFabricatorSwitch, missileFabricator;
    private int frame, missileParts;
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

    public void toggleShield() {
        shieldSwitch = !shieldSwitch;
    }

    public void toggleMissileFabricator() {
        missileFabricatorSwitch = !missileFabricatorSwitch;
    }

    public void upgrade(String s) {
        if (null != s) {
            switch (s) {
                case "HP":
                    maxHP += 25;
                    HP += 25;
                    armor += 0.1;
                    break;
                case "EP":
                    maxEP += 25;
                    genEP += .025;
                    break;
                case "Speed":
                    speed += .5;
                    thrusters += 1;
                    break;
                case "Laser":
                    laserDamage += 2.5;
                    break;
                case "ShieldMatrix":
                    shieldMatrix = true;
                    break;
                case "ShieldPlasma":
                    shieldPlasma = true;
                    break;
                case "MissileFabricator":
                    missileFabricator = true;
                    missileFabricatorSwitch = true;
                    break;
                default:
                    break;
            }
        }
    }

    public void interactWith(Character c, double d) {
        if (collidesWith(c)) {
            if (!evading && d > 5) {
                Random rand = new Random();
                int n = rand.nextInt(100) + 1;
                if (n < thrusters * 10) {
                    evading = true;
                    evasionTimer = 30;
                }
            }
            //check if it is a collision with a ship
            // more than 5 damage is a projectile
            //a ship on ship collision will still cause damage reguardless of evasion
            if (evading && d > 5) {

            } else {

                takeDamage(d);
            }
        }
    }

    @Override
    public void takeDamage(double d) {
        if (d < 0) {
            HP += maxHP / (100 / -d);
        } else {

            if (shieldON) {
                if (d > EP) {
                    EP = 0;
                    shieldON = false;
                    takeDamage(d - EP);
                } else {
                    EP -= d;
                }
            } else {
                double r = 0;
                if (!(d <= 0)) {
                    r = d * armor;
                }
                HP -= (d - r);
            }

            flashingTimer = 18;

            if (HP <= 0) {
                die();
            }
        }
    }

    @Override
    public double getDiameter() {
        if (shieldON) {
            return 60;
        }
        return 20;
    }

    private double getRealDiameter() {
        if (evading) {
            return 25;
        }
        return 20;
    }

    public void setDirection(double dx, double dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public double getEP() {
        return EP;
    }

    public double getLaserDamage() {
        return laserDamage;
    }

    @Override
    public void update() {
        if (!isAlive()) {
            return;
        }
        if (missileFabricatorSwitch && missileFabricator && EP > 2) {
            missileParts += 1;
            EP -= 0.05;
        }
        if (missileParts == 60) {
            missileParts = 0;
            missiles += 1;
        }
        if (!shieldSwitch) {
            shieldON = false;
        }
        if (EP > 5 && shieldSwitch) {
            shieldON = true;
        }
        evasionTimer--;
        if (evasionTimer <= 0) {
            evading = false;
        }
        if (HP > maxHP) {
            HP = maxHP;
        }
        if (EP <= 1) {
            shieldON = false;
        }
        if (laserFiring) {
            EP -= 0.1;
        }
        if (shieldON && !shieldMatrix) {
            EP -= 0.05;
        } else if (shieldON && shieldMatrix) {
            EP -= 0.025;
        }
        if (EP < maxEP) {
            EP += genEP;
        }
        if (EP > maxEP) {
            EP = maxEP;
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

    //if a bullet takes x.999 it will ignore it. we don't want bullets to die if they were evaded
    @Override
    public double getCollisionDamage() {
        double shieldDamage = 0;
        if (shieldON && shieldPlasma) {
            shieldDamage = 20;
        }
        //do some rounding to prevent Double trouble
        double newCollision = Math.round((shieldDamage + 4.999) * 1000.0) / 1000.0;
        if (evading) {
            return newCollision;
        } else {
            return collisionDamage + shieldDamage;
        }

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
            return (int) (getRealDiameter() / 2);
        } else {
            return 0;
        }
    }

    public int checkBankRight() {
        if (bankRight) {
            return (int) -(getRealDiameter() / 2);
        } else {
            return 0;
        }
    }

    public void draw(Graphics2D gc) {
        // Offset the corner of our circle so it's drawn with the correct 
        // center.
        double DIAM = getRealDiameter();
        double x1 = x - DIAM / 2;
        double y1 = y - DIAM / 2;
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

        Font font = new Font("Serif", Font.BOLD, 12);
        gc.setFont(font);

        //HULL POINTS
        Color HPcolor = new Color(50, 205, 50, 255);
        gc.setColor(HPcolor);
        gc.drawRect(25, 250 - (int) maxHP, 35, (int) maxHP);
        gc.fillRect(25, 250 - (int) HP, 35, (int) HP);
        int intHP = (int) HP;
        int intMaxHP = (int) maxHP;
        gc.setColor(Color.WHITE);
        gc.drawString(intHP + "/" + intMaxHP, 23, 260);
        gc.setColor(HPcolor);
        gc.drawString("HP", 23, 270);

        //ENERGY POINTS
        Color EPcolor = new Color(0, 191, 255, 255);
        gc.setColor(EPcolor);
        gc.drawRect(70, 250 - (int) maxEP, 35, (int) maxEP);
        gc.fillRect(70, 250 - (int) EP, 35, (int) EP);
        int intEP = (int) EP;
        int intMaxEP = (int) maxEP;
        gc.setColor(Color.WHITE);
        gc.drawString(intEP + "/" + intMaxEP, 68, 260);
        gc.setColor(EPcolor);
        gc.drawString("EP", 68, 270);

        //EVASION
        if (evading) {
            gc.setColor(Color.WHITE);
            int xInt = (int) x;
            int yInt = (int) y;
            gc.drawString("EVADE", xInt - 22, yInt + 50 - evasionTimer * 2);
        }

        Color shieldColor = new Color(70, 130, 180, 100);
        if (shieldPlasma) {
            shieldColor = new Color(180, 70, 130, 100);
        }
        gc.setColor(shieldColor);
        if (shieldON) {
            gc.fillOval((int) (x - DIAM * 1.5), (int) (y - DIAM * 1.5), (int) (DIAM * 3), (int) (DIAM * 3));
        }

        bankLeft = false;
        bankRight = false;
    }

}
