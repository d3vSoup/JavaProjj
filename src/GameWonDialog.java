import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Custom Game Won Dialog with wobbling green text and shiny animation
 */
public class GameWonDialog extends JDialog {
    private JLabel gameWonLabel;
    private Timer wobbleTimer;
    private Timer shinyTimer;
    private int wobbleOffset = 0;
    private boolean wobbleRight = true;
    private float shinyOffset = 0.0f;

    public GameWonDialog(JFrame parent) {
        super(parent, true);
        setUndecorated(true);
        setSize(600, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 0, 0, 240));

        // Create wobbling GAME WON label
        gameWonLabel = new JLabel("GAME WON", JLabel.CENTER);
        gameWonLabel.setFont(new Font("Arial", Font.BOLD, 80));
        gameWonLabel.setForeground(Color.GREEN);
        gameWonLabel.setOpaque(false);

        // Add shadow effect
        JLabel shadowLabel = new JLabel("GAME WON", JLabel.CENTER);
        shadowLabel.setFont(new Font("Arial", Font.BOLD, 80));
        shadowLabel.setForeground(new Color(0, 0, 0, 150));
        shadowLabel.setBounds(3, 3, 600, 120);

        JPanel labelPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw shiny gradient effect over the label area
                if (gameWonLabel != null && gameWonLabel.getX() >= 0) {
                    int x = gameWonLabel.getX();
                    int y = gameWonLabel.getY();
                    int width = gameWonLabel.getWidth();
                    int height = gameWonLabel.getHeight();
                    
                    // Create shiny effect using simple gradient
                    int shinyX = (int)(x + (shinyOffset * width));
                    int shinyWidth = width / 4;
                    
                    // Draw multiple overlapping gradients for shiny effect
                    for (int i = 0; i < 3; i++) {
                        int alpha = 150 - (i * 30);
                        if (alpha > 0) {
                            g2d.setColor(new Color(255, 255, 255, alpha));
                            int offset = shinyX - (shinyWidth / 2) + (i * 10);
                            if (offset >= x && offset < x + width) {
                                g2d.fillRect(offset, y, shinyWidth, height);
                            }
                        }
                    }
                }
            }
        };
        labelPanel.setOpaque(false);
        labelPanel.add(shadowLabel);
        labelPanel.add(gameWonLabel);
        gameWonLabel.setBounds(0, 0, 600, 120);
        shadowLabel.setBounds(3, 3, 600, 120);

        // OK button
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Arial", Font.BOLD, 24));
        okButton.setPreferredSize(new Dimension(150, 50));
        okButton.setBackground(Color.BLACK);
        okButton.setForeground(Color.WHITE);
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                wobbleTimer.stop();
                shinyTimer.stop();
                dispose();
                System.exit(0);
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(okButton);

        add(labelPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Wobble animation
        wobbleTimer = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (wobbleRight) {
                    wobbleOffset += 2;
                    if (wobbleOffset >= 10) {
                        wobbleRight = false;
                    }
                } else {
                    wobbleOffset -= 2;
                    if (wobbleOffset <= -10) {
                        wobbleRight = true;
                    }
                }
                gameWonLabel.setBounds(wobbleOffset, 0, 600, 120);
                shadowLabel.setBounds(3 + wobbleOffset, 3, 600, 120);
                labelPanel.repaint();
            }
        });
        wobbleTimer.start();

        // Shiny animation
        shinyTimer = new Timer(30, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                shinyOffset += 0.05f;
                if (shinyOffset > 1.5f) {
                    shinyOffset = -0.5f;
                }
                labelPanel.repaint();
            }
        });
        shinyTimer.start();
    }
}

