package StarFighter;

/*
 *
 */
import com.sun.corba.se.spi.oa.OADefault;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implement game logic for robot game, including robot creation and levels.
 *
 * @author jlepak
 */
public class StarFighterGame {

    public Timer timer;
    public ArrayList<Character> ships;
    public ArrayList<Character> stars;
    public ArrayList<Character> powerups;
    public ArrayList<Character> lineofFire;
    public ArrayList<Character> playerForces;
    public Character targetConfirmed;
    public double proximityDistance;
    public double proximityDistanceTemp;
    public Player player;
    public LaserBeam LB;
    public int level, credits, HPLevel, EPLevel, SpeedLevel, LaserLevel, startingWidth;
    boolean mousePressedLeft, mousePressedRight, laserExhausted, missileAlternate, missileToggle;
    public Laser laser;
    public double endLaserX, endLaserY, mouseX, mouseY, cursorX, cursorY, laserDamage, shipType;
    private int width, height, levelEnd = 6000;
    int i = 1, j, bossOnField, levelTimer = 0, missileTimer, missileTimerValue = 20, shipSquadCounter, shipSquadSize;
    Random rand = new Random();
    double lx;
    double ly;
    double len;
    double vecX;
    double vecY;

    StarFighterGame(int width, int height) {
        this.width = width;
        this.height = height;
        ships = new ArrayList<Character>();
        stars = new ArrayList<Character>();
        playerForces = new ArrayList<Character>();
        powerups = new ArrayList<Character>();
        lineofFire = new ArrayList<Character>();
        player = new Player(width / 2, height / 2);
        laser = new Laser();
        LB = new LaserBeam(0, 0);
        level = 0;
        HPLevel = 1;
        EPLevel = 1;
        SpeedLevel = 1;
        LaserLevel = 1;
        proximityDistance = 3000;
        proximityDistanceTemp = 3000;
        playerForces.add(player);
        missileTimer = missileTimerValue;
    }

    public void reset() {

        i = 1;
        level = 0;
        player = new Player(0, 0);
        LB = new LaserBeam(0, 0);
        bossOnField = 0;
        ships.clear();
        playerForces.clear();
        newLevel();
        int posX = width / 2;
        int posY = height / 2;
        player.setCenter(posX, posY);
        HPLevel = 1;
        EPLevel = 1;
        SpeedLevel = 1;
        LaserLevel = 1;
        playerForces.add(player);
        missileTimer = missileTimerValue;

    }

    public int bossReduction() {
        if (bossOnField > 0) {
            return 4;
        } else {
            return 1;
        }
    }

    public void newLevel() {
        level++;
        levelTimer = 0;

    }

    public void toggleShield() {
        player.shieldON = !player.shieldON;
    }

    public void toggleMissileMode() {

        missileToggle = !missileToggle;
    }

    public void spawnTier1Enemies() {

        if (shipType < (0.3)) {
            Character newShip;
            newShip = new LaserStrafer(startingWidth, 0, player);
            ships.add(newShip);

        } else if (shipType < (0.6)) {
            Character newShip;
            newShip = new ShootShip(startingWidth, 0, player);
            ships.add(newShip);

        } else if (shipType < 1) {
            Character newShip;
            newShip = new SteerCrashShip(startingWidth, 0, player);
            ships.add(newShip);

        } else {
            return;
        }
    }

    public void spawnShips() {

        if ((i >= ((100) * bossReduction()))) {

            if ((levelTimer > levelEnd && levelTimer < levelEnd + 105) && ((level == 1) || (level == 3) || (level == 5))) {
                Character newShip;
                newShip = new CannonFrigate(150, 0, player);
                ships.add(newShip);
                bossOnField += 3;
                if (level == 3 || level == 5) {
                    Character othernewShip;
                    othernewShip = new CannonFrigate(1216, 700, player);
                    ships.add(othernewShip);
                    bossOnField += 3;
                }
            }

            if ((levelTimer > levelEnd && levelTimer < levelEnd + 105) && ((level == 2) || (level == 4) || (level == 5))) {
                Character newShip;
                newShip = new DroneFrigate(700, 0, player);
                ships.add(newShip);
                bossOnField += 3;
                if (level == 4 || level == 5) {
                    Character othernewShip;
                    othernewShip = new DroneFrigate(700, 750, player);
                    ships.add(othernewShip);
                    bossOnField += 3;
                }
            }
            if (level > 5) {
                if (1 + rand.nextInt(100) <= level - 5) {
                    Character newShip;
                    newShip = new CannonFrigate(rand.nextInt(width - 50), 0, player);
                    ships.add(newShip);
                    i = 0;
                }

            }
            if (i != 0) {
                shipType = rand.nextDouble();
                startingWidth = 50 + rand.nextInt(width - 100);
                shipSquadSize = 1;
                shipSquadCounter = 0;
                spawnTier1Enemies();
            }

            i = 0;
        } else {
            i++;
        }
        if ((shipSquadCounter >= (20 - level)) && (shipSquadSize < level)) {
            shipSquadCounter = 0;
            shipSquadSize++;
            if (startingWidth <= 50) {
                startingWidth = startingWidth + 50;
            } else if (startingWidth >= 1316) {
                startingWidth = startingWidth - 50;
            } else {
                Random coin = new Random();
                int x = coin.nextInt(2);
                if (x == 0) {
                    startingWidth = startingWidth - 50;
                } else {
                    startingWidth = startingWidth + 50;
                }
            }
            spawnTier1Enemies();
        } else {
            shipSquadCounter++;
        }

        if (j == 30) {
            Character newStar;
            double starType = rand.nextDouble();
            if (starType < 1) {
                newStar = new Star(rand.nextInt(width), 0, player);
            } else {
                newStar = new Star(rand.nextInt(width), rand.nextInt(height), player);
            }
            stars.add(newStar);
            j = 0;
        } else {
            j++;
        }

    }

    public void update() {
        spawnShips();
        levelTimer++;
        if (missileTimer < missileTimerValue) {
            missileTimer++;
        }
        proximityDistance = 3000;
        targetConfirmed = player;
        for (Character s : stars) {
            s.update();
            if (s.getY() > StarFighterApp.SIZE2) {
                ships.remove(s);
            }
        }

        //Missile Launcher
        if (mousePressedRight && missileTimer == missileTimerValue && !(player.missiles == 0)) {
            Character newMissile = null;
            if (!missileToggle) {
                newMissile = new Missile((int) player.getX() - 20, (int) player.getY() + 12, (int) player.getX() - 20, (int) player.getY() - 2000);
                playerForces.add(newMissile);
                ships.add(newMissile);
                newMissile = new Missile((int) player.getX() + 20, (int) player.getY() + 12, (int) player.getX() + 20, (int) player.getY() - 2000);
                playerForces.add(newMissile);
                ships.add(newMissile);
                player.missiles = player.missiles - 2;
                missileTimer = 0;
            } else {
                if (missileAlternate) {
                    newMissile = new Missile((int) player.getX() - 20, (int) player.getY() + 12, (int) mouseX, (int) mouseY);
                    playerForces.add(newMissile);
                    ships.add(newMissile);
                    player.missiles = player.missiles - 1;
                } else {
                    newMissile = new Missile((int) player.getX() + 20, (int) player.getY() + 12, (int) mouseX, (int) mouseY);
                    playerForces.add(newMissile);
                    ships.add(newMissile);
                    player.missiles = player.missiles - 1;
                }
                missileTimer = missileTimerValue / 2;
                missileAlternate = !missileAlternate;
            }

        }

//         Laser collision!!!
        boolean laserFiring = false;

        if (player.getEP() < 1) {
            laserExhausted = true;
        } else if (player.getEP() > 5) {
            laserExhausted = false;
        }

        if (mousePressedLeft && (player.getEP() > 1) && !laserExhausted) {
            lx = mouseX - player.getX();
            ly = mouseY - player.getY();
            len = Math.sqrt(lx * lx + ly * ly);
            vecX = 2000 * lx / len;
            vecY = 2000 * ly / len;
            endLaserX = (player.getX() + vecX);
            endLaserY = (player.getY() + vecY);
            laserFiring = true;
            laserDamage = LB.getLaserDamage();
            player.EP -= .3;
            player.laserFiring = true;
        } else {
            player.laserFiring = false;
        }

        //Detect the closest enemy to determine laser stopping point and damage target
        for (Character a : getCharacters()) {

            if (a.isTargetable() && laserFiring && laser.collisionLaser(player.getX(), player.getY(), a.getX(), a.getY(),
                    endLaserX, endLaserY, (a.getDiameter()) / 2) == true) {
                //If Player's Energy is above 40%, the laser will pierce
                if (player.EP / player.maxEP < 0.4) {
                    proximityDistanceTemp = (a.dist(player.getX(), player.getY(), a.getX(), a.getY()));
                    if (proximityDistanceTemp < proximityDistance) {
                        targetConfirmed = a;
                        proximityDistance = proximityDistanceTemp;
                    }

                } 
                else {
                    a.takeDamage(laserDamage);
                }
            }
        }

        //Inflict damage and pierce debris
        if (player.EP / player.maxEP < 0.4) {
            if (targetConfirmed != player && !targetConfirmed.isDieing()) {
                vecX = proximityDistance * lx / len;
                vecY = proximityDistance * ly / len;
                endLaserX = (player.getX() + vecX);
                endLaserY = (player.getY() + vecY);
                targetConfirmed.takeDamage(laserDamage);
            }
        }

        //Paint the laser
        if (laserFiring) {
            LB.LaserCoordinates(player.getX(), player.getY(), endLaserX, endLaserY);
        }

        //Iterate through all of the objects
        for (Character a : getCharacters()) {


            // Generates bullets for shooter class units
            if ((a.getBulletTimer() == a.getBulletTimerMax()) && (a.getBulletTimerMax() != 0)) {
                a.resetBulletTimer();
                Character newShip = null;
                if (a.ammotype() == 0) {
                    newShip = new Bullet((int) a.getX(), (int) a.getY(), player);
                } else if (a.ammotype() == 1) {
                    newShip = new AccurateBullet((int) a.getX(), (int) a.getY(), player);
                } else if (a.ammotype() == 2) {
                    newShip = new Drone((int) (a.getX() - 10), (int) (a.getY() + 10), player);
                } else if (a.ammotype() == 3) {
                    newShip = new LaserBurst((int) (a.getX()), (int) (a.getY()), player);
                }

                ships.add(newShip);
            }
            if (!a.isAlive() && !a.isProjectile()) {
                player.credits += a.giveBounty();

                if (rand.nextDouble() < .1) {
                    Character newPowerUp = new PowerUp_HP25((int) a.getX(), (int) a.getY(), player);
                    playerForces.add(newPowerUp);
                    ships.add(newPowerUp);
                }

                if (rand.nextDouble() < .025) {
                    Character newPowerUp = new PowerUp_Missiles((int) a.getX(), (int) a.getY(), player);
                    playerForces.add(newPowerUp);
                    ships.add(newPowerUp);
                }

            }
            //check for colliding objects 
            //the boolean asks that enemies dont touch powerups and that only players do
            //debris won't trigger collision
            if (playerForces.contains(a)) {
                for (Character b : ships) {
                    if (((!(playerForces.contains(b)) && !(a.ammotype() < 0)) || ((a == player) && (b.ammotype() < 0)))
                            && !(b.isDieing())) {
                        if (a.collidesWith(b)) {
                            a.interactWith(b, b.getCollisionDamage());
                            //missile powerup
                            if (b.ammotype() == -2) {
                                player.missiles = player.missiles + 20;
                            }
                            //missile hits insta-gib small ships
                            if (a.ammotype() == 100 && (b.getLevel() == 1)) {
                                b.die();
                            } else if (a.ammotype() == 100 && (b.getLevel() == 3)) {
                                b.takeDamage(2);
                            } else {
                                b.interactWith(a, a.getCollisionDamage());
                            }

                        }
                    }
                }
            }

            if (!a.isAlive()) {
                ships.remove(a);
                playerForces.remove(a);
                if (a.getLevel() > 2) {
                    bossOnField -= a.getLevel();

                }

            }

            if ((a.getY() > StarFighterApp.SIZE2) || (a.getY() < 0) || (a.getX() > StarFighterApp.SIZE1)
                    || (a.getX() < 0)) {

                if (a.getLevel() != 0) {
                    stars.remove(a);
                }
                ships.remove(a);
                playerForces.remove(a);
            }

            a.update();
        }

    }

    public void input(double dx, double dy) {
        player.setDirection(dx, dy);
    }

    public void inputMouse(double mx, double my) {
        mouseX = mx;
        mouseY = my;
    }

    public void inputMouseAction(int mcl, int mcr) {
        if (mcl == 1) {
            mousePressedLeft = true;
        }
        if (mcl == 0) {
            mousePressedLeft = false;
        }
        if (mcr == 1) {
            mousePressedRight = true;
        }
        if (mcr == 0) {
            mousePressedRight = false;
        }
    }

    public boolean levelComplete() {

        if (levelTimer > levelEnd + 180 && bossOnField == 0) {

            return true;
        } else {
            return false;
        }
    }

    public boolean gameOver() {
        return !player.isAlive();

    }

    private class CharIterable implements Iterable<Character> {

        public Iterator<Character> iterator() {
            return new CharIterator();
        }
    }

    private class CharIterator implements Iterator<Character> {

        // Track index within robot array; if -1, indicate that 
        // human should be next.
        int index = -1;

        public boolean hasNext() {
            return index < ships.size();
        }

        public Character next() {
            Character output;
            if (index < 0) {
                output = player;
            } else {
                output = ships.get(index);
            }
            index++;
            return output;
        }

        public void remove() {
        }
    }

    public Iterable<Character> getCharacters() {
        return new CharIterable();
    }
}
