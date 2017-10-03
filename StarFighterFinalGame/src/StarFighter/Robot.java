package StarFighter;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Implement simple robot that follows a target.
 *
 * @author fsjlepak
 */
public class Robot extends CharacterBase {
    // Who robot is following

    private Character target;
    // Speed of character
    private static final double RATE = 1;

    public Robot(int x0, int y0, Character target) {
        super(x0, y0);
        this.target = target;
    }

    public void update() {
        if (!isAlive()) {
            return;
        }
        if (flashingTimer == 0)
            colorSwap = false;
        if (flashingTimer > 0) {
            if (flashingTimer%3 == 0){
                colorSwap=!colorSwap;}
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
        double vecX = RATE * dx / len;
        double vecY = RATE * dy / len;
        // Update based on direction vector
        setCenter(x0 + vecX, y0 + vecY);
    }

    @Override
    public Color getColor() {
        if (isAlive()) {
            if (!colorSwap) {
                return Color.RED;
            } else {                
                return Color.WHITE;
            }
        } else {
            return Color.GRAY;
        }
    }

    
}
