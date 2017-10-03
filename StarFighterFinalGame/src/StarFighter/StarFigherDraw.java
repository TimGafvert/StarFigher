package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.MouseInputAdapter;
import javax.swing.*;

/**
 * Draw and control the robot game.
 *
 * @author fsjlepak
 */
public class StarFigherDraw implements GameDraw, GameControl {

    // Track mouse and keyboard
    private double mouseX, mouseY;
    private boolean left, right, up, down, space = true, upgradeHP = true, q = true,
            upgradeEP = true, upgradeSpeed = true, upgradeLaser = true, p = true, pause;
    private int mousePressedLeft = 0, mousePressedRight = 0, i;
    // Size of board
    private int width, height;
    private Mouse mouse;
    private Keys keys;
    private StarFighterGame game;
    // Track time to display messages
    private static final int DISPLAY_MILLIS = 3;
    private long timerStart = -1, introTimerStart, introTimerCurrent;

    public StarFigherDraw(int width, int height, StarFighterGame game) {
        this.game = game;
        this.width = width;
        this.height = height;
        mouse = new Mouse();
        keys = new Keys();
        game.newLevel();
        introTimerStart = System.currentTimeMillis();
    }

    public void draw(Graphics2D g, AnimationPanel panel) {

        // Clear last frame
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, panel.getWidth(), panel.getHeight());

        boolean gameOver = game.gameOver();
        if (!gameOver) {
            updateAll();
        }

        drawAll(g);

        if (game.levelComplete()) {
            i = 180;
            if (timerStart < 0) {
                timerStart = System.currentTimeMillis();
            }
            long now = System.currentTimeMillis();
            if (now - timerStart > DISPLAY_MILLIS) {
                game.newLevel();
                timerStart = -1;

            }
        }
        if (i > 0) {
            i--;
            if (game.level == 6) {
                drawMessage("You Win! Play for High Score!", g);
            } else {
                drawMessage("Level " + game.level + " Reached", g);
            }
        }
        if (gameOver) {
            drawMessage("Game over", g);

        }
    }

    private void drawIntro(Graphics2D g) {
        Font font = new Font("Serif", Font.BOLD, 25);
        String message = "Press 'P' to Pause and read intructions";
        FontMetrics metrics = g.getFontMetrics(font);
        int sHeight = metrics.getHeight();
        int sWidth = metrics.stringWidth(message);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message, (width / 2) - sWidth / 2, (height / 2) - sHeight / 2);
    }

    private void drawMessage(String message, Graphics2D g) {
        Font font = new Font("Serif", Font.BOLD, 50);
        FontMetrics metrics = g.getFontMetrics(font);
        int height = metrics.getHeight();
        int width = metrics.stringWidth(message);
        // Display centered message
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message, this.width / 2 - width / 2,
                this.height / 2 + height / 2);

    }

    private void drawCredits(String message, Graphics2D g) {
        Font font = new Font("Serif", Font.BOLD, 13);
        FontMetrics metrics = g.getFontMetrics(font);
        int height = metrics.getHeight();
        int width = metrics.stringWidth(message);
        // Display centered message
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message, 10 ,
                10 + height / 2);

    }
    private void drawMissileCount(String message, Graphics2D g) {
        Font font = new Font("Serif", Font.BOLD, 13);
        FontMetrics metrics = g.getFontMetrics(font);
        int height = metrics.getHeight();
        int width = metrics.stringWidth(message);
        // Display centered message
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message, 10 ,
                30 + height / 2);

    }

    private void drawStats(Graphics2D g) {
        Font font = new Font("Serif", Font.BOLD, 13);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString("Hull Level: " + game.HPLevel, 10, 500);
        if (game.HPLevel == 5) {
            g.drawString("Cost to Upgrade: MAX", 10, 520);
        } else {
            g.drawString("Cost to Upgrade: " + UpgradeCost(game.HPLevel), 10, 520);
        }
        g.drawString("Energy Level: " + game.EPLevel, 10, 540);
        if (game.EPLevel == 5) {
            g.drawString("Cost to Upgrade: MAX", 10, 560);
        } else {
            g.drawString("Cost to Upgrade: " + UpgradeCost(game.EPLevel), 10, 560);
        }
        g.drawString("Speed Level: " + game.SpeedLevel, 10, 580);
        if (game.SpeedLevel == 5) {
            g.drawString("Cost to Upgrade: MAX", 10, 600);
        } else {
            g.drawString("Cost to Upgrade: " + UpgradeCost(game.SpeedLevel), 10, 600);
        }
        g.drawString("Laser Level: " + game.LaserLevel, 10, 620);
        if (game.LaserLevel == 5) {
            g.drawString("Cost to Upgrade: MAX", 10, 640);
        } else {
            g.drawString("Cost to Upgrade: " + UpgradeCost(game.LaserLevel), 10, 640);
        }
    }

    private void drawPause(Graphics2D g) {
        Font font = new Font("Serif", Font.BOLD, 30);
        String message = "PAUSED";
        FontMetrics metrics = g.getFontMetrics(font);
        int sHeight = metrics.getHeight();
        int sWidth = metrics.stringWidth(message);
        g.setFont(font);
        g.setColor(Color.WHITE);
        g.drawString(message, (width / 2) - sWidth / 2, (height / 2) - sHeight / 2);
        Font font2 = new Font("Serif", Font.BOLD, 14);
        g.setFont(font2);
        g.drawString("<------------- Credits for purchasing Upgrades", 100, 20);
        g.drawString("Use the WASD keys to move around.", 150, 150);
        g.drawString("Click and hold the mouse to fire the laser.", 150, 180);
        g.drawString("Press SPACE to activate Shields; this will drain "
                + "Energy instead of HP.", 150, 210);
        g.drawString("Hull Points start at 100. + 25 MAX HP and 10% damage "
                + "reduction per level. Press '1' to Upgrade.", 150, 500);
        g.drawString("Energy starts at 100, with .5 regen. + 25 MAX EP and .1 "
                + "regen per level. Press '2' to Upgrade.", 150, 540);
        g.drawString("Speed starts at 2. + 1 Speed per level. Press '3' to Upgrade.", 150, 580);
        g.drawString("Laser Damage starts at 10. + 2.5 Damage per level. Press '4' to Upgrade.", 150, 620);

    }

    private void drawAll(Graphics2D g) {
        for (Character s : game.stars) {
            s.draw(g);
        }
        game.LB.draw(g);
        for (Character c : game.ships) {
            c.draw(g);
        }
        game.player.draw(g);
        drawCredits("Credits: " + Integer.toString(game.player.credits), g);
        drawMissileCount("Missiles: " + Integer.toString(game.player.missiles), g);
        drawStats(g);
        if (pause) {
            drawPause(g);
        }
        introTimerCurrent = System.currentTimeMillis();
        if ((introTimerCurrent - introTimerStart < 5000) && !pause) {
            drawIntro(g);
        }

    }

    private int UpgradeCost(int l) {
        return (int) (1000 * Math.pow(2, (l - 1)));
    }

    private void updateAll() {
        // Set direction for human
        double dx = 0, dy = 0;
        if (left) {
            if (game.player.getX() > 30) {
                dx = -1;
            }
        }
        if (right) {
            if (game.player.getX() < StarFighterApp.SIZE1 - 40) {
                dx = 1;
            }
        }
        if (up) {
            if (game.player.getY() > 50) {
                dy = -1;
            }
        }
        if (down) {
            if (game.player.getY() < StarFighterApp.SIZE2 - 50) {
                dy = 1;

            }

        }
        if (!space) {
            game.toggleShield();
            space = true;
        }
        
        if (!q) {
            game.toggleMissileMode();
            q = true;
        }
        if (!upgradeHP) {
            if (game.player.credits >= UpgradeCost(game.HPLevel) && game.HPLevel < 5) {
                game.player.maxHP += 20;
                game.player.credits -= UpgradeCost(game.HPLevel);
                game.player.armor += .1;
                game.HPLevel++;
                game.player.HP += 25;
            }
            upgradeHP = true;
        }
        if (!upgradeEP) {
            if (game.player.credits >= UpgradeCost(game.EPLevel) && game.EPLevel < 5) {
                game.player.maxEP += 20;
                game.player.genEP += .1;
                game.player.credits -= UpgradeCost(game.EPLevel);
                game.EPLevel++;
            }
            upgradeEP = true;
        }
        if (!upgradeSpeed) {
            if (game.player.credits >= UpgradeCost(game.SpeedLevel) && game.SpeedLevel < 5) {
                game.player.speed += 1;
                game.player.credits -= UpgradeCost(game.SpeedLevel);
                game.SpeedLevel++;
            }
            upgradeSpeed = true;
        }
        if (!upgradeLaser) {
            if (game.player.credits >= UpgradeCost(game.LaserLevel) && game.LaserLevel < 5) {
                game.LB.upgradeLaserDamage();
                game.player.credits -= UpgradeCost(game.LaserLevel);
                game.LaserLevel++;
            }
            upgradeLaser = true;
        }
        game.inputMouse(mouseX, mouseY);
        game.inputMouseAction(mousePressedLeft,mousePressedRight);
        

        if (!p) {
            pause = !pause;
            p = true;

        }
        if (pause) {
            return;
        }
        game.input(dx, dy);
        game.update();

    }

    // MouseInputAdapter provides empty default handlers for all mouse events.
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

        
        public void mousePressed(MouseEvent e) {
            
            if (SwingUtilities.isLeftMouseButton(e)) {
                mousePressedLeft = 1;
            } 
            if (SwingUtilities.isRightMouseButton(e)) {
                mousePressedRight = 1;
            }
        }
       
        public void mouseReleased(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                mousePressedLeft = 0;
            } 
            if (e.getButton() == MouseEvent.BUTTON3) {
                mousePressedRight = 0;
            }
        }
              
    }
    // Right click, left click, release Right, press Right
    

    // KeyAdapter provides empty default handlers for all key events.
    // It implements the KeyListener interface.
    private class Keys extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyChar() == 'r') {
                game.reset();
            } else {
                updateFlag(e, true);
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            updateFlag(e, false);
        }

        void updateFlag(KeyEvent e, boolean pressed) {
            if (e.getKeyCode() == KeyEvent.VK_A) {
                left = pressed;
            } else if (e.getKeyCode() == KeyEvent.VK_D) {
                right = pressed;
            } else if (e.getKeyCode() == KeyEvent.VK_W) {
                up = pressed;
            } else if (e.getKeyCode() == KeyEvent.VK_S) {
                down = pressed;
            } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                space = pressed;
            } else if (e.getKeyCode() == KeyEvent.VK_1) {
                upgradeHP = pressed;
            } else if (e.getKeyCode() == KeyEvent.VK_2) {
                upgradeEP = pressed;
            } else if (e.getKeyCode() == KeyEvent.VK_3) {
                upgradeSpeed = pressed;
            } else if (e.getKeyCode() == KeyEvent.VK_4) {
                upgradeLaser = pressed;
            } else if (e.getKeyCode() == KeyEvent.VK_P) {
                p = pressed;
            } else if (e.getKeyCode() == KeyEvent.VK_Q) {
                q = pressed;
            }
        }
    }

    public MouseListener getMouseListener() {
        return mouse;
    }

    public KeyListener getKeyListener() {
        return keys;
    }

    public MouseMotionListener getMouseMotionListener() {
        return mouse;
    }
}
