/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package StarFighter;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author Tim
 */
public class LaserBeamCollision {

    private double t, dl, dist, dt, t1, t2, i1X, i1Y, i2X, i2Y, originX, originY, targetX, targetY, endX, endY, radius,
            lineOriginEnd, dx, dy, closePtOnLnX, closePtOnLnY,
            frontX, frontY, backX, backY;
    
    public void laserDraw(Graphics2D g) {
        g.setColor(Color.BLUE);
        g.drawLine((int)originX,(int)originY,(int)endX,(int)endY);
    }
           

        
    public boolean collisionLaser(double ox, double oy, double tx, double ty, double ex, double ey, double r) {
        this.originX = ox;
        this.originY = oy;
        this.targetX = tx;
        this.targetY = ty;
        this.endX = ex;
        this.endY = ey;
        this.radius = r;

        dx = endX - originX;
        dy = endY - originY;
        if ((dx == 0) && (dy == 0)) {
            // A and B are the same points, no way to calculate intersection
            return false;
        }

        dl = (dx * dx + dy * dy);
        t = ((targetX - originX) * dx + (targetY - originY) * dy) / dl;

// point on a line nearest to circle center
        closePtOnLnX = originX + t * dx;
        closePtOnLnY = originY + t * dy;
        
     
        dist = Math.sqrt(Math.pow((closePtOnLnX - targetX), 2) + Math.pow((closePtOnLnY - targetY), 2));



        if (dist < radius) // two possible intersection points
        {
            dt = Math.sqrt(radius * radius - dist * dist) / Math.sqrt(dl);

            // intersection point nearest to A
            t1 = t - dt;
            i1X = originX + t1 * dx;
            i1Y = originY + t1 * dy;
            if (t1 < 0 || t1 > 1) {
                // intersection point is not actually within line segment
                return false;
            }

            // intersection point farthest from A
            t2 = t + dt;
            i2X = originX + t2 * dx;
            i2Y = originY + t2 * dy;
            if (t2 < 0 || t2 > 1) {
                // intersection point is not actually within line segment
                return false;
            }
            return true;
        }
        return false;
    }
}


