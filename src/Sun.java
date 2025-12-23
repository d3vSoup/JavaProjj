import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Soup
 */
public class Sun extends JPanel implements MouseListener {

    private GamePanel gp;
    private Image sunImage;

    private int myX;
    private int myY;
    private int endY;

    private int destruct = 200;
    private boolean isCollected = false;
    private boolean scoreAdded = false;  // Track if score was already added
    private int targetX = 37;  // Top left sun counter position
    private int targetY = 80;
    private int fadeTimer = 0;  // Timer for fade out after reaching target
    private int fadeDuration = 30;  // Fade out over ~1-2 seconds (30 frames at 60ms = ~1.8 seconds)

    public Sun(GamePanel parent, int startX, int startY, int endY) {
        this.gp = parent;
        this.endY = endY;
        setSize(80, 80);
        setOpaque(false);
        myX = startX;
        myY = startY;
        setLocation(myX, myY);
        sunImage = new ImageIcon(this.getClass().getResource("images/sun.png")).getImage();
        addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Apply fade out effect if sun is collected and reached target
        if (fadeTimer > 0) {
            float alpha = (float)fadeTimer / fadeDuration;
            // Create a composite with transparency
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g2d.drawImage(sunImage, 0, 0, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        } else {
            g.drawImage(sunImage, 0, 0, null);
        }
    }

    public void advance() {
        if (isCollected) {
            // Animate to top left corner
            int dx = targetX - myX;
            int dy = targetY - myY;
            double distance = Math.sqrt(dx * dx + dy * dy);
            
            if (distance > 15) {
                // Move towards target with better speed
                int moveX = (int)(dx * 0.4);
                int moveY = (int)(dy * 0.4);
                // Ensure at least 1 pixel movement
                if (Math.abs(moveX) < 1 && dx != 0) moveX = dx > 0 ? 1 : -1;
                if (Math.abs(moveY) < 1 && dy != 0) moveY = dy > 0 ? 1 : -1;
                myX += moveX;
                myY += moveY;
            } else {
                // Reached target - add to score (only once) and start fade out
                if (!scoreAdded) {
                    int currentScore = gp.getSunScore();
                    gp.setSunScore(currentScore + 25);
                    System.out.println("Sun collected! Score: " + currentScore + " -> " + (currentScore + 25));
                    scoreAdded = true;
                    fadeTimer = fadeDuration;  // Start fade out
                }
                
                // Fade out over 1-2 seconds
                if (fadeTimer > 0) {
                    fadeTimer--;
                    repaint();  // Repaint to show fade effect
                    if (fadeTimer <= 0) {
                        // Fade complete - remove sun
                        gp.remove(this);
                        gp.getActiveSuns().remove(this);
                        return;
                    }
                } else {
                    // Shouldn't reach here, but safety check
                    gp.remove(this);
                    gp.getActiveSuns().remove(this);
                    return;
                }
            }
        } else if (myY < endY) {
            myY += 4;
        } else {
            destruct--;
            if (destruct < 0) {
                gp.remove(this);
                gp.getActiveSuns().remove(this);
            }
        }
        setLocation(myX, myY);
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!isCollected) {
            // Start animation to top left
            isCollected = true;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
