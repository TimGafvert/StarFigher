package StarFighter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.swing.SwingUtilities;
import javax.swing.event.MouseInputAdapter;

public class StarFighterDraw implements GameDraw, GameControl {

    // Track mouse and keyboard
    private double mouseX, mouseY;
    private boolean left, right, up, down, space, upgradeHP, q, f,
            upgradeEP, upgradeSpeed, upgradeLaser, upgradeDynamicLauncher,
            upgradeMissileFabricator, upgradeShieldMatrix, upgradeShieldPlasma, p, pause, shopping = true;
    private int mousePressedLeft = 0, mousePressedRight = 0;

    // Size of board
    private final int WIDTH, HEIGHT;
    private final Mouse MOUSE;
    private final Keys KEYS;
    private final StarFighterGame GAME;
    // Track time
    private static final int DISPLAY_MESSAGE_TIME = 3000;
    private long timerStart;
    private final long introTimerStart;
    private long introTimerCurrent;

    public StarFighterDraw(int width, int height, StarFighterGame game) {
        this.timerStart = -1;
        this.GAME = game;
        this.WIDTH = width;
        this.HEIGHT = height;
        this.MOUSE = new Mouse();
        this.KEYS = new Keys();
        this.GAME.newLevel();
        this.introTimerStart = System.currentTimeMillis();
        this.pause = true;

    }

    @Override
    public void draw(Graphics2D g, AnimationPanel panel) {
        // Clear last frame
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        // Run the game if its not currently over
        boolean gameOver = GAME.gameOver();
        if (!gameOver) {
            updateAll();
        }

        drawAll(g);
        // Keeps track of time and displays message on level completion
        // Starts new level when conditions are met
        if (GAME.levelComplete()) {
            if (timerStart < 0) {
                timerStart = System.currentTimeMillis();
            }
            long now = System.currentTimeMillis();
            if (now - timerStart > DISPLAY_MESSAGE_TIME) {
                //syncs up GAME time with DRAW time?
                GAME.newLevel();
                shopping = true;
                pause = true;
                timerStart = -1;
            } else {
                if (GAME.level == 6) {
                    drawMessage("You Win! Play for High Score!", g);
                } else {
                    drawMessage("Level " + GAME.level + " Completed", g);
                }
            }

        }
        if (gameOver) {
            drawMessage("Game over", g);

        }
    }

    private void drawMessage(String message, Graphics2D g) {
        Font font = new Font("Serif", Font.BOLD, 50);
        FontMetrics metrics = g.getFontMetrics(font);
        int sHeight = metrics.getHeight();
        int sWidth = metrics.stringWidth(message);
        // Display centered message
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message, this.WIDTH / 2 - sWidth / 2,
                this.HEIGHT / 2 + sHeight / 2);

    }

    private void drawCredits(String message, Graphics2D g) {
        Font font = new Font("Serif", Font.BOLD, 20);
        FontMetrics metrics = g.getFontMetrics(font);
        int sHeight = metrics.getHeight();
        int sWidth = metrics.stringWidth(message);
        // Display centered message
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message, 20,
                10 + sHeight / 2);

    }

    private double getEnergyDrain() {
        double energyDrain = 0;
        if (GAME.laserFiring) {
            energyDrain += 0.1;
        }
        if (GAME.player.shieldON && !GAME.player.shieldMatrix) {
            energyDrain += 0.05;
        }
        if (GAME.player.shieldON && GAME.player.shieldMatrix) {
            energyDrain += 0.025;
        }
        if (GAME.player.missileFabricatorSwitch) {
            energyDrain += 0.05;
        }
        return energyDrain;
    }

    private void drawStats(Graphics2D g) {
        //draw GUI
        g.setColor(Color.darkGray);
        g.fillRect(0, 0, 200, HEIGHT);
        g.setColor(Color.WHITE);
        g.drawRect(0, 0, 200, HEIGHT);
        Font font = new Font("Serif", Font.BOLD, 16);
        g.setFont(font);
        Color mainText = Color.WHITE;

        g.setColor(Color.YELLOW);
        g.drawString("Missiles: " + GAME.player.missiles, 10, 300);
        g.setColor(mainText);
        if (GAME.player.missileFabricator) {
            if (GAME.player.missileFabricatorSwitch) {
                g.drawString("Fabricator(F): ACTIVE", 10, 315);
                g.drawString("Energy Drain: 3.0", 10, 330);
            } else {
                g.drawString("Fabricator(F): INACTIVE", 10, 315);
                g.drawString("Energy Drain: 0.0", 10, 330);
            }
        }

        g.setColor(Color.GREEN);
        g.drawString("Hull Level: " + GAME.HPLevel, 10, 350);
        g.setColor(mainText);
        g.drawString((GAME.HPLevel * 25 + 75) + " Max Hull Points", 10, 365);
        g.drawString(((GAME.HPLevel - 1) * 10) + "% Damage Reduction", 10, 380);

        g.setColor(Color.CYAN);
        g.drawString("Reactor Level: " + GAME.EPLevel, 10, 400);
        g.setColor(mainText);
        g.drawString((GAME.EPLevel * 25 + 75) + " Max Energy Points", 10, 415);
        g.drawString(String.format("%.1f", (GAME.player.genEP * 60)) + " Energy Produced", 10, 430);
        g.drawString(String.format("%.1f", getEnergyDrain() * 60) + " Energy Drained", 10, 445);

        g.setColor(Color.RED);
        g.drawString("Thrusters Level: " + GAME.SpeedLevel, 10, 465);
        g.setColor(mainText);
        g.drawString((GAME.SpeedLevel * 0.5 + 1.5) + " km/s", 10, 480);
        g.drawString(((GAME.SpeedLevel - 1) * 10) + "% Evasion", 10, 495);

        g.setColor(Color.MAGENTA);
        g.drawString("Laser Level: " + GAME.LaserLevel, 10, 515);
        g.setColor(mainText);
        g.drawString((String.format("%.1f", (GAME.LaserLevel * 2.5 + 7.5)) + " Damage"), 10, 530);
        g.drawString("Pierces at EP +" + (90 - (GAME.LaserLevel * 10)) + "%", 10, 545);
        if (GAME.laserFiring) {
            g.drawString("Energy Drain: 6.0", 10, 560);
        } else {
            g.drawString("Energy Drain: 0.0", 10, 560);
        }

        g.setColor(Color.cyan);
        if (GAME.player.shieldON) {
            g.drawString("Shield(Q): ACTIVE", 10, 580);
            g.setColor(mainText);
            if (GAME.player.shieldMatrix) {
                g.drawString("Energy Drain 1.5", 10, 595);
            } else {
                g.drawString("Energy Drain 3.0", 10, 595);
            }
        } else {
            g.drawString("Shield(Q): INACTIVE", 10, 580);
            g.setColor(mainText);
            g.drawString("Energy Drain 0.0", 10, 595);
        }

    }

    private void drawShop(Graphics2D g) {
        Font font = new Font("Serif", Font.BOLD, 30);
        String message = "BUY UPGRADES AND ACCESSORIES";
        FontMetrics metrics = g.getFontMetrics(font);
        int sWidth = metrics.stringWidth(message);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message, (WIDTH / 2) - sWidth / 2, 25);

        Font font2 = new Font("Serif", Font.BOLD, 20);
        g.setFont(font2);
        Color mainText = Color.WHITE;

        g.setColor(Color.GREEN);
        if (GAME.HPLevel < 5) {
            g.drawString("(1)    Hull Level: " + GAME.HPLevel + ".             *Press -1- to Upgrade Hull for " + GAME.UpgradeCost(GAME.HPLevel) + " Credits.*", 300, 100);
        } else {
            g.drawString("(x)    Hull Level: 5 (MAXED)", 300, 100);
        }
        g.setColor(Color.CYAN);
        if (GAME.EPLevel < 5) {
            g.drawString("(2)    Reactor Level: " + GAME.EPLevel + ".       *Press -2- to Upgrade Reactor for " + GAME.UpgradeCost(GAME.EPLevel) + " Credits.*", 300, 125);
        } else {
            g.drawString("(x)    Reactor Level: 5 (MAXED)", 300, 125);
        }
        g.setColor(Color.RED);
        if (GAME.SpeedLevel < 5) {
            g.drawString("(3)    Thrusters Level: " + GAME.SpeedLevel + ".    *Press -3- to Upgrade Thrusters for " + GAME.UpgradeCost(GAME.SpeedLevel) + " Credits.*", 300, 150);
        } else {
            g.drawString("(x)    Thrusters Level: 5 (MAXED)", 300, 150);
        }
        g.setColor(Color.MAGENTA);
        if (GAME.LaserLevel < 5) {
            g.drawString("(4)    Laser Level: " + GAME.LaserLevel + ".           *Press -4- to Upgrade Laser for " + GAME.UpgradeCost(GAME.LaserLevel) + " Credits.*", 300, 175);
        } else {
            g.drawString("(x)    Laser Level: 5 (MAXED)", 300, 175);
        }
        g.setColor(Color.YELLOW);
        if (!GAME.missileAimed) {
            g.drawString("(5)    Dynamic Launcher: Allows targeting of missiles.      *Press -5- to Purchase for 2000 Credits.*", 300, 200);
        } else {
            g.drawString("(x)    Dynamic Launcher: (PURCHASED)", 300, 200);
        }
        g.setColor(Color.YELLOW);
        if (!GAME.player.missileFabricator) {
            g.drawString("(6)    Missile Fabricator: Generates missiles over time.    *Press -6- to Purchase for 6000 Credits.*", 300, 225);
        } else {
            g.drawString("(x)    Missile Fabricator: (PURCHASED)", 300, 225);
        }
        g.setColor(Color.BLUE);
        if (!GAME.player.shieldMatrix) {
            g.drawString("(7)    Shield Matrix Stabilizer: Reduced energy drain.   *Press -7- to Purchase for 3000 Credits.*", 300, 250);
        } else {
            g.drawString("(x)    Shield Matrix Stabilizer: (PURCHASED)", 300, 250);
        }
        g.setColor(Color.BLUE);
        if (!GAME.player.shieldPlasma) {
            g.drawString("(8)    Plasma Shielding: Shield burns enemy ships.    *Press -8- to Purchase for 3000 Credits.*", 300, 275);
        } else {
            g.drawString("(x)    Plasma Shielding: (PURCHASED)", 300, 275);
        }

    }

    private void drawPause(Graphics2D g) {
        Font font = new Font("Serif", Font.BOLD, 30);
        String message = "PAUSED (P)";
        FontMetrics metrics = g.getFontMetrics(font);
        int sHeight = metrics.getHeight();
        int sWidth = metrics.stringWidth(message);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message, (WIDTH / 2) - sWidth / 2, (HEIGHT / 2) - sHeight / 2);
        String message2 = "WASD keys to move. Left click fires Laser.";
        String message3 = "Right Click Fires Missiles. Q toggles Shield. P to Pause.";

        sHeight = metrics.getHeight();
        sWidth = metrics.stringWidth(message2);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message2, (WIDTH / 2) - sWidth / 2, (HEIGHT / 2) - (sHeight / 2) + 320);
        sWidth = metrics.stringWidth(message3);
        g.drawString(message3, (WIDTH / 2) - sWidth / 2, ((HEIGHT / 2) - sHeight / 2) + 350);

    }

    private void drawAll(Graphics2D g) {

        GAME.stars.forEach((s) -> {
            s.draw(g);
        });
        GAME.laserVisual.draw(g);
        GAME.ships.forEach((c) -> {
            c.draw(g);
        });
        
        
        drawStats(g);
        GAME.player.draw(g);
        drawCredits("Credits: " + Integer.toString(GAME.player.credits), g);
        if (pause) {
            drawPause(g);
        }
        if (shopping) {
            drawShop(g);
        }

    }

    private void updateAll() {
        // Set direction for human
        double dx = 0, dy = 0;
        if (left) {
            if (GAME.player.getX() > 30 + 205) {
                dx = -1;
            }
        }
        if (right) {
            if (GAME.player.getX() < StarFighterApp.SIZE1 - 40) {
                dx = 1;
            }
        }
        if (up) {
            if (GAME.player.getY() > 50) {
                dy = -1;
            }
        }
        if (down) {
            if (GAME.player.getY() < StarFighterApp.SIZE2 - 50) {
                dy = 1;

            }

        }
        if (q) {
            GAME.player.toggleShield();
            q = false;
        }
        if (f) {
            GAME.player.toggleMissileFabricator();
            f = false;
        }

        if (upgradeHP && shopping) {
            GAME.upgradePlayer("HP");
            upgradeHP = false;
        }
        if (upgradeEP && shopping) {
            GAME.upgradePlayer("EP");
            upgradeEP = false;
        }
        if (upgradeSpeed && shopping) {
            GAME.upgradePlayer("Speed");
            upgradeSpeed = false;
        }
        if (upgradeLaser && shopping) {
            GAME.upgradePlayer("Laser");
            upgradeLaser = false;
        }
        if (upgradeDynamicLauncher && shopping) {
            GAME.upgradePlayer("DynamicLauncher");
            upgradeDynamicLauncher = false;
        }
        if (upgradeMissileFabricator && shopping) {
            GAME.upgradePlayer("MissileFabricator");
            upgradeMissileFabricator = false;
        }
        if (upgradeShieldMatrix && shopping) {
            GAME.upgradePlayer("ShieldMatrix");
            upgradeShieldMatrix = false;
        }
        if (upgradeShieldPlasma && shopping) {
            GAME.upgradePlayer("ShieldPlasma");
            upgradeShieldPlasma = false;
        }

        GAME.inputMouse(mouseX, mouseY);
        GAME.inputMouseAction(mousePressedLeft, mousePressedRight);

        if (p) {
            pause = !pause;
            p = false;
            shopping = false;

        }
        if (pause) {
            return;
        }
        GAME.input(dx, dy);
        GAME.update();

    }

    // MouseInputAdapter provides empty default handlers for all MOUSE events.
    // It implements the MouseMotionListener and MouseListener interfaces.
    private class Mouse extends MouseInputAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }

        @Override
        public void mousePressed(MouseEvent e) {

            if (SwingUtilities.isLeftMouseButton(e)) {
                mousePressedLeft = 1;
            }
            if (SwingUtilities.isRightMouseButton(e)) {
                mousePressedRight = 1;
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                mousePressedLeft = 0;
            }
            if (e.getButton() == MouseEvent.BUTTON3) {
                mousePressedRight = 0;
            }
        }

    }

    // KeyAdapter provides empty default handlers for all key events.
    // It implements the KeyListener interface.
    private class Keys extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'r') {
                GAME.reset();
            } else {
                updateFlag(e, true);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            updateFlag(e, false);

        }

        void updateFlag(KeyEvent e, boolean pressed) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_A:
                    left = pressed;
                    break;
                case KeyEvent.VK_D:
                    right = pressed;
                    break;
                case KeyEvent.VK_W:
                    up = pressed;
                    break;
                case KeyEvent.VK_S:
                    down = pressed;
                    break;
                case KeyEvent.VK_SPACE:
                    space = pressed;
                    break;
                case KeyEvent.VK_1:
                    upgradeHP = pressed;
                    break;
                case KeyEvent.VK_2:
                    upgradeEP = pressed;
                    break;
                case KeyEvent.VK_3:
                    upgradeSpeed = pressed;
                    break;
                case KeyEvent.VK_4:
                    upgradeLaser = pressed;
                    break;
                case KeyEvent.VK_5:
                    upgradeDynamicLauncher = pressed;
                    break;
                case KeyEvent.VK_6:
                    upgradeMissileFabricator = pressed;
                    break;
                case KeyEvent.VK_7:
                    upgradeShieldMatrix = pressed;
                    break;
                case KeyEvent.VK_8:
                    upgradeShieldPlasma = pressed;
                    break;
                case KeyEvent.VK_P:
                    p = pressed;
                    break;
                case KeyEvent.VK_Q:
                    q = pressed;
                    break;
                case KeyEvent.VK_F:
                    f = pressed;
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public MouseListener getMouseListener() {
        return MOUSE;
    }

    @Override
    public KeyListener getKeyListener() {
        return KEYS;
    }

    @Override
    public MouseMotionListener getMouseMotionListener() {
        return MOUSE;
    }
}

//GRAVEYARD BELOW
//private void drawPause(Graphics2D g) {
//        Font font = new Font("Serif", Font.BOLD, 30);
//        String message = "PAUSED";
//        FontMetrics metrics = g.getFontMetrics(font);
//        int sHeight = metrics.getHeight();
//        int sWidth = metrics.stringWidth(message);
//        g.setFont(font);
//        g.setColor(Color.WHITE);
//        g.drawString(message, (WIDTH / 2) - sWidth / 2, (HEIGHT / 2) - sHeight / 2);
//        Font font2 = new Font("Serif", Font.BOLD, 14);
//        g.setFont(font2);
//        g.drawString("<------------- Credits for purchasing Upgrades", 100, 20);
//        g.drawString("Use the WASD KEYS to move around.", 150, 150);
//        g.drawString("Click and hold the MOUSE to fire the laserLogic.", 150, 180);
//        g.drawString("Press SPACE to activate Shields; this will drain "
//                + "Energy instead of HP.", 150, 210);
//        g.drawString("Hull Points start at 100. + 25 MAX HP and 10% damage "
//                + "reduction per level. Press '1' to Upgrade.", 150, 500);
//        g.drawString("Energy starts at 100, with .5 regen. + 25 MAX EP and .1 "
//                + "regen per level. Press '2' to Upgrade.", 150, 540);
//        g.drawString("Speed starts at 2. + 1 Speed per level. Press '3' to Upgrade.", 150, 580);
//        g.drawString("Laser Damage starts at 10. + 2.5 Damage per level. Press '4' to Upgrade.", 150, 620);
//
//    }
