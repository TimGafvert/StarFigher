package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

public class StarBase extends CharacterBase {
    

    private Character target;
  
    Random rand = new Random();
    private double RATE = 4;

    public double getDiameter() {
        return 700;
    }

    public StarBase(int x0, int y0, Character target) {
        super(x0, y0);
        this.target = target;
    }

    public void die() {
        return;
    }

    public void update() {
//        if (!isAlive()) {
//            return;
//        }
//        if (flashingTimer == 0) {
//            colorSwap = false;
//        }
//        if (flashingTimer > 0) {
//            if (flashingTimer % 3 == 0) {
//                colorSwap = !colorSwap;
//            }
//            flashingTimer--;
//
//        }

        double x0 = getX();
        double y0 = getY();

        setCenter(x0, y0 + RATE);
    }

    @Override
    public Color getColor() {  
        Color starbase = new Color(90, 90,90, 255);
            return  starbase;
        
    }
    
    public void draw(Graphics2D g) {
        // Offset the corner of our circle so it's drawn with the correct 
        // center.
        double DIAM = getDiameter();
        double x1 = x - DIAM / 2;
        double y1 = y - DIAM / 2;
        // Note that even though getColor() must be defined by the subclass,
        // it can still be called by other methods implemented in the abstract
        // class.
        g.setColor(getColor());
        g.fillRect((int) 0, (int)StarFighterApp.SIZE2/2, (int) 1000, (int) 1000);

    }

    
}
