package StarFighter;




import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Provides a basic panel for drawing with an animation loop that makes
 * use of simple interfaces for animation and interaction.
 * @author jlepak
 */
public class AnimationPanel extends JPanel implements ActionListener {
    GameDraw draw;
    GameControl control;
    Timer timer;
    double frameRate = 70.0;
    
    AnimationPanel(GameDraw draw, GameControl control) {
        this.draw = draw;
        this.control = control;
        // Set input handlers based on control object
        addMouseListener(control.getMouseListener());
        addKeyListener(control.getKeyListener());
        addMouseMotionListener(control.getMouseMotionListener());
        
        // The animation loop will automatically repaint this panel,
        // so ignore other repaint requests.
        setIgnoreRepaint(true);
    }
    
    // Set framerate and update timer if it's set.
    void setFrameRate(double rate) {
        frameRate = rate;
        if (timer != null)
            timer.setDelay((int)(1000/rate));
    }
    
    double getFrameRate() {
        return frameRate;
    }
    
    // Draw a single frame of animation
    @Override protected void paintComponent(Graphics g) {
        
        // Enable antialiasing for shapes
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        // Enable antialiasing for text
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        // Use animator object to draw a single frame
        draw.draw(g2, this);
    }
    
    // Handle the timer firing
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    
    // Start animation loop
    public void start() {
        requestFocusInWindow();
        timer = new Timer((int)(1000 / frameRate), this);
        timer.start();
    }
}
