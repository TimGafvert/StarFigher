package StarFighter;

import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Test routine for our animation/game.
 *
 */
public class StarFighterApp {

    public static final int SIZE1 = 1366;
    public static final int SIZE2 = 768;

    public static void buildGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("StarFighter");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(SIZE1, SIZE2));

        // Set up the game controls and panel
        StarFighterGame game = new StarFighterGame(SIZE1, SIZE2);
        StarFigherDraw draw = new StarFigherDraw(SIZE1, SIZE2, game);
        AnimationPanel gamePanel = new AnimationPanel(draw, draw);
        //draw.setAnimator(gamePanel);
        frame.setContentPane(gamePanel);

        //Display the window.
        frame.pack();
        frame.setVisible(true);

        // Start animation loop
        gamePanel.start();
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                buildGUI();
            }
        });
    }
}
