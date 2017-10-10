package StarFighter;

/*
 *
 */
import static StarFighter.Sound.cleanThreads;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Timer;

// Implement game logic game
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
    public LaserBeamVisual laserVisual;
    public LaserBeamCollision laserLogic;
    public int level, credits, HPLevel, EPLevel, SpeedLevel, LaserLevel, startingWidth=500, priceShieldPlasma = 3000, priceShieldMatrix = 3000, priceDynamicLauncher = 2000, priceMissileFabricator = 6000;

    boolean mousePressedLeft, mousePressedRight, laserExhausted, missileAlternate, missileAimed, laserFiring;
    public double endLaserX, endLaserY, mouseX, mouseY, cursorX, cursorY, laserDamage, shipType;
    //timers run on frames
    private final int offsetGUI =200,PLAYABLE_WIDTH, WIDTH, HEIGHT, LEVEL_COMPLETION_TIME = 3000;
    int enemySpawnTimer = 1, starSpawnTimer, bossOnField, levelTimer = 0, missileTimer, missileTimerValue = 20, shipSquadCounter, shipSquadSize;
    Random rand = new Random();
    double lx;
    double ly;
    double len;
    double vecX;
    double vecY;

    StarFighterGame(int width, int height) {
        shipType= rand.nextDouble();
        this.WIDTH = width;
        this.HEIGHT = height;
        this.PLAYABLE_WIDTH = width -offsetGUI;
        ships = new ArrayList<>();
        stars = new ArrayList<>();
        playerForces = new ArrayList<>();
        powerups = new ArrayList<>();
        lineofFire = new ArrayList<>();
        player = new Player(width / 2, height / 2);
        laserLogic = new LaserBeamCollision();
        laserVisual = new LaserBeamVisual(0, 0);
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
        enemySpawnTimer = 1;
        level = 0;
        player = new Player(0, 0);
        laserVisual = new LaserBeamVisual(0, 0);
        bossOnField = 0;
        ships.clear();
        playerForces.clear();
        newLevel();
        int posX = PLAYABLE_WIDTH / 2;
        int posY = HEIGHT / 2;
        player.setCenter(posX, posY);
        missileAimed = false;
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

        }
    }

    public void spawnShips() {

        if ((enemySpawnTimer >= ((100) * bossReduction()))) {
           

            if ((levelTimer > LEVEL_COMPLETION_TIME && levelTimer < LEVEL_COMPLETION_TIME + 105) && ((level == 1) || (level == 3) || (level == 5))) {
                Character newShip;
                newShip = new CannonFrigate(200+ offsetGUI, 0, player);
                ships.add(newShip);
                bossOnField += 3;
                if (level == 3 || level == 5) {
                    Character othernewShip;
                    othernewShip = new CannonFrigate(1216, 700, player);
                    ships.add(othernewShip);
                    bossOnField += 3;
                }
            }

            if ((levelTimer > LEVEL_COMPLETION_TIME && levelTimer < LEVEL_COMPLETION_TIME + 105) && ((level == 2) || (level == 4) || (level == 5))) {
                Character newShip;
                newShip = new DroneFrigate(750, 0, player);
                ships.add(newShip);
                bossOnField += 3;
                if (level == 4 || level == 5) {
                    Character othernewShip;
                    othernewShip = new DroneFrigate(750, 750, player);
                    ships.add(othernewShip);
                    bossOnField += 3;
                }
            }
            if (level > 5) {
                if (1 + rand.nextInt(100) <= level - 5) {
                    Character newShip;
                    newShip = new CannonFrigate((rand.nextInt(PLAYABLE_WIDTH - 50)+ offsetGUI), 0, player);
                    ships.add(newShip);
                    enemySpawnTimer = 0;
                }

            }
            if (enemySpawnTimer != 0) {
                shipType = rand.nextDouble();
                startingWidth = 50 + rand.nextInt(PLAYABLE_WIDTH - 100)+offsetGUI;
                shipSquadSize = 1;
                shipSquadCounter = 0;
                spawnTier1Enemies();
            }

            enemySpawnTimer = 0;
        } else {
            enemySpawnTimer++;
        }
        if ((shipSquadCounter >= (20 - level)) && (shipSquadSize < level)) {
            shipSquadCounter = 0;
            shipSquadSize++;
            if (startingWidth <= 200) {
                startingWidth = startingWidth + 200;
            } else if (startingWidth >= 1316) {
                startingWidth = startingWidth - 50;
            } else {
                Random coin = new Random();
                int x = coin.nextInt(2);
                if (x == 0 && startingWidth > 250) {
                    startingWidth = startingWidth - 50;
                } else {
                    startingWidth = startingWidth + 50;
                }
            }
            spawnTier1Enemies();
        } else {
            shipSquadCounter++;
        }

        if (starSpawnTimer == 30) {
            cleanThreads();
            Character newStar;
            double starType = rand.nextDouble();
            if (starType < 1) {
                newStar = new Star(rand.nextInt(PLAYABLE_WIDTH)+ offsetGUI, 0, player);
            } else {
                newStar = new Star(rand.nextInt(PLAYABLE_WIDTH)+ offsetGUI, rand.nextInt(HEIGHT), player);
            }
            stars.add(newStar);
            starSpawnTimer = 0;
        } else {
            starSpawnTimer++;
        }

    }

    public void update() {
//        Sound.cleanThreads();
        double pierceThreshold = (0.9 - (LaserLevel * 0.1));
        if (!levelComplete()) {
            spawnShips();
        }
        levelTimer++;
        if (missileTimer < missileTimerValue) {
            missileTimer++;
        }
        proximityDistance = 3000;
        targetConfirmed = player;
        stars.stream().map((s) -> {
            s.update();
            return s;
        }).filter((s) -> (s.getY() > StarFighterApp.SIZE2+200)).forEachOrdered((s) -> {
            ships.remove(s);
        });

        //Missile Launcher
        if (mousePressedRight && missileTimer == missileTimerValue && !(player.missiles <= 0)) {
            Character newMissile;
            if (!missileAimed && player.missiles > 1) {
                newMissile = new Missile((int) player.getX() - 20, (int) player.getY() + 12, (int) player.getX() - 20, (int) player.getY() - 2000);
                playerForces.add(newMissile);
                ships.add(newMissile);
                newMissile = new Missile((int) player.getX() + 20, (int) player.getY() + 12, (int) player.getX() + 20, (int) player.getY() - 2000);
                playerForces.add(newMissile);
                ships.add(newMissile);
                player.missiles = player.missiles - 2;
                missileTimer = 0;
            } else if (!missileAimed) {
                newMissile = new Missile((int) player.getX(), (int) player.getY() + 12, (int) player.getX(), (int) player.getY() - 2000);
                playerForces.add(newMissile);
                ships.add(newMissile);
                player.missiles = player.missiles - 1;
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

//         LaserBeamCollision collision!!!
        laserFiring = false;

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
            laserDamage = player.getLaserDamage();
            player.laserFiring = true;
        } else {
            player.laserFiring = false;
        }

        //Detect the closest enemy to determine laserLogic stopping point and damage target
        for (Character a : getCharacters()) {

            if (a.isTargetable() && laserFiring && laserLogic.collisionLaser(player.getX(), player.getY(), a.getX(), a.getY(),
                    endLaserX, endLaserY, (a.getDiameter()) / 2) == true) {
                //If Player's Energy is above the pierce threshold, the laserLogic will pierce

                if (player.EP / player.maxEP < pierceThreshold) {
                    proximityDistanceTemp = (a.dist(player.getX(), player.getY(), a.getX(), a.getY()));
                    if (proximityDistanceTemp < proximityDistance) {
                        targetConfirmed = a;
                        proximityDistance = proximityDistanceTemp;
                    }

                } else {
                    a.takeDamage(laserDamage);
                }
            }
        }

        //Inflict damage and pierce debris
        if (player.EP / player.maxEP < pierceThreshold) {
            if (targetConfirmed != player && !targetConfirmed.isDieing()) {
                vecX = proximityDistance * lx / len;
                vecY = proximityDistance * ly / len;
                endLaserX = (player.getX() + vecX);
                endLaserY = (player.getY() + vecY);
                targetConfirmed.takeDamage(laserDamage);
            }
        }

//        set the coordinates for the visual effect of the laser
        if (laserFiring) {
            laserVisual.LaserCoordinates(player.getX(), player.getY(), endLaserX, endLaserY);
        }

        //Iterate through all of the objects
        for (Character a : getCharacters()) {

            // Generates bullets for shooter class units
            if ((a.getBulletTimer() == a.getBulletTimerMax()) && (a.getBulletTimerMax() != 0)) {
                a.resetBulletTimer();
                Character newShip = null;
                switch (a.ammotype()) {
                    case 0: 
                        newShip = new Bullet((int) a.getX(), (int) a.getY(), player);
//                        Sound.play("src/StarFighter/Gun.wav");
                        break;
                    case 1:
                        newShip = new AccurateBullet((int) a.getX(), (int) a.getY(), player);
                        Sound.play("Gun.wav");
                        break;
                    case 2:
                        newShip = new Drone((int) (a.getX() - 10), (int) (a.getY() + 10), player);
                        break;
                    case 3:
                        newShip = new LaserBurst((int) (a.getX()), (int) (a.getY()), player);
                        break;
                    default:
                        break;
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
                ships.stream().filter((b) -> (((!(playerForces.contains(b)) && !(a.ammotype() < 0)) || ((a == player) && (b.ammotype() < 0)))
                        && !(b.isDieing()))).filter((b) -> (a.collidesWith(b))).map((b) -> {
                    a.interactWith(b, b.getCollisionDamage());
                    return b;
                }).map((b) -> {
                    //missile powerup
                    if (b.ammotype() == -2) {
                        player.missiles = player.missiles + 20;
                    }
                    return b;
                }).forEachOrdered((b) -> {
                    //missile hit insta-gib small ships
                    if (a.ammotype() == 100 && (b.getLevel() == 1)) {
                        b.die();
                    } else if (a.ammotype() == 100 && b.getLevel() == 3) {
                        b.takeDamage(2);
                    } else {
                        b.interactWith(a, a.getCollisionDamage());
                    }
                });
            }
            //Remove dead objects
            if (!a.isAlive()) {
                ships.remove(a);
                playerForces.remove(a);
                if (a.getLevel() > 2) {
                    bossOnField -= a.getLevel();

                }

            }
            //Remove out of bounds objects, with some buffer for the top area
            if ((a.getY() > StarFighterApp.SIZE2) || (a.getY() < -200) || (a.getX() > StarFighterApp.SIZE1)
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

        return levelTimer > LEVEL_COMPLETION_TIME + 180 && bossOnField == 0;
    }

    public boolean gameOver() {
        return !player.isAlive();

    }

    public int UpgradeCost(int l) {
        return (int) (1000 * Math.pow(2, (l - 1)));
    }

    public void upgradePlayer(String s) {

        if (null != s) {
            switch (s) {
                case "HP":
                    if (player.credits >= UpgradeCost(HPLevel) && HPLevel < 5) {
                        player.credits -= UpgradeCost(HPLevel);
                        HPLevel++;
                        player.upgrade("HP");
                    }
                    break;
                case "EP":
                    if (player.credits >= UpgradeCost(EPLevel) && EPLevel < 5) {
                        player.credits -= UpgradeCost(EPLevel);
                        EPLevel++;
                        player.upgrade("EP");
                    }
                    break;
                case "Speed":
                    if (player.credits >= UpgradeCost(SpeedLevel) && SpeedLevel < 5) {
                        player.credits -= UpgradeCost(SpeedLevel);
                        SpeedLevel++;
                        player.upgrade("Speed");
                    }
                    break;
                case "Laser":
                    if (player.credits >= UpgradeCost(LaserLevel) && LaserLevel < 5) {
                        player.credits -= UpgradeCost(LaserLevel);
                        LaserLevel++;
                        laserVisual.upgradeLaserColor();
                        player.upgrade("Laser");
                    }
                    break;
                case "DynamicLauncher":
                    if (player.credits >= priceDynamicLauncher && !missileAimed) {
                        player.credits -= priceDynamicLauncher;
                        missileAimed = true;
                    }
                    break;
                case "MissileFabricator":
                    if (player.credits >= priceMissileFabricator && !player.missileFabricator) {
                        player.credits -= priceMissileFabricator;
                        player.upgrade("MissileFabricator");
                    }
                    break;
                case "ShieldMatrix":
                    if (player.credits >= priceShieldMatrix && !player.shieldMatrix) {
                        player.credits -= priceShieldMatrix;
                        player.upgrade("ShieldMatrix");
                    }
                    break;
                case "ShieldPlasma":
                    if (player.credits >= priceShieldPlasma && !player.shieldPlasma) {
                        player.credits -= priceShieldPlasma;
                        player.upgrade("ShieldPlasma");
                    }
                    break;
                default:
                    break;
            }
        }

    }

    private class CharIterable implements Iterable<Character> {

        public Iterator<Character> iterator() {
            return new CharIterator();
        }
    }

    private class CharIterator implements Iterator<Character> {

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
