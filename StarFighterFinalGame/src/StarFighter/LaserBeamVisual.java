/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StarFighter;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 *
 * @author Tim
 */
public class LaserBeamVisual extends CharacterBase {
    private int dx,dy,level = 1;    
    private boolean firing;
    private double ox,oy,ex,ey;
    public LaserBeamVisual(int x, int y) {
        super(x, y);
        dx = 0;
        dy = 0;
    }

    @Override
    public void update() {
    }
    
    public void LaserCoordinates(double ox,double oy, double ex, double ey){
        this.ox = ox;
        this.oy = oy;
        this.ex = ex;
        this.ey = ey;
        firing = true;
    }
    
    
    public void upgradeLaserColor() {
        level += 1;
    }
//186 85 211      148 0 211      30 144 255      0 245 255       255 20 147
    @Override
    public Color getColor() {
        Color color1 = new Color(30,144,255,255);        
        Color color2 = new Color(148,0,211,255);
        Color color3 = new Color(186,85,211,255);
        Color color4 = new Color(0,245,255,255);
        Color color5 = new Color(255,20,147,255);        
        
        if (level == 1)            
            return color1;
        if (level == 2)
            return color2;
        if(level == 3)
            return color3;
        if(level == 4)
            return color4;
        else
            return color5;
    }
    public void draw(Graphics2D g) {      
        if (!firing)
            return;
        g.setColor(getColor());
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(3));
        g2.drawLine((int) ox, (int) oy, (int) ex, (int) ey);
        g2.setStroke(new BasicStroke(1));
        firing = false;
    }

    
}
