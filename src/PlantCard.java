import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * Created by Soup
 */
public class PlantCard extends JPanel implements MouseListener, MouseMotionListener {

    private Image img;
    private ActionListener al;
    private boolean isDragging = false;
    private int dragOffsetX, dragOffsetY;
    private int originalX, originalY;

    public PlantCard(Image img) {
        setSize(64, 90);
        this.img = img;
        addMouseListener(this);
        addMouseMotionListener(this);
        setOpaque(false);
    }

    public void setAction(ActionListener al) {
        this.al = al;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(img, 0, 0, null);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Click still works for selecting plant
        if (!isDragging && al != null) {
            al.actionPerformed(new ActionEvent(this, ActionEvent.RESERVED_ID_MAX + 1, ""));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        isDragging = true;
        originalX = getX();
        originalY = getY();
        dragOffsetX = e.getX();
        dragOffsetY = e.getY();
        setCursor(new Cursor(Cursor.MOVE_CURSOR));
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (isDragging) {
            isDragging = false;
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            
            // Check if dropped on game panel (for drag and drop placement)
            Component parent = getParent();
            if (parent instanceof JLayeredPane) {
                // Find GamePanel in the layered pane
                for (Component comp : ((JLayeredPane) parent).getComponents()) {
                    if (comp instanceof GamePanel) {
                        GamePanel gp = (GamePanel) comp;
                        // Convert point to GamePanel coordinates
                        Point gpPoint = SwingUtilities.convertPoint(this, e.getPoint(), gp);
                        // Check if point is over a collider
                        for (int i = 0; i < 45; i++) {
                            Collider c = gp.getColliders()[i];
                            if (gpPoint.x >= c.getX() && gpPoint.x < c.getX() + c.getWidth() &&
                                gpPoint.y >= c.getY() && gpPoint.y < c.getY() + c.getHeight()) {
                                // First set the active planting brush
        if (al != null) {
            al.actionPerformed(new ActionEvent(this, ActionEvent.RESERVED_ID_MAX + 1, ""));
                                }
                                // Then immediately place the plant
                                ActionListener action = c.getAction();
                                if (action != null) {
                                    action.actionPerformed(new ActionEvent(c, ActionEvent.RESERVED_ID_MAX + 1, ""));
                                }
                                break;
                            }
                        }
                        break;
                    }
                }
            }
            
            // Snap back to original position
            setLocation(originalX, originalY);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (isDragging) {
            Point parentPoint = SwingUtilities.convertPoint(this, e.getPoint(), getParent());
            setLocation(parentPoint.x - dragOffsetX, parentPoint.y - dragOffsetY);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }
}
