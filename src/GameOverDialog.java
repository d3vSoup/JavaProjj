import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Custom Game Over Dialog with wobbling red bold text
 */
public class GameOverDialog extends JDialog {
    private JLabel gameOverLabel;
    private Timer wobbleTimer;
    private int wobbleOffset = 0;
    private boolean wobbleRight = true;

    public GameOverDialog(JFrame parent) {
        super(parent, true);
        setUndecorated(true);
        setSize(600, 350);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(0, 0, 0, 240));

        // Create wobbling GAME OVER label with large red text
        gameOverLabel = new JLabel("GAME OVER", JLabel.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 80));
        gameOverLabel.setForeground(Color.RED);
        gameOverLabel.setOpaque(false);

        // Add shadow effect
        JLabel shadowLabel = new JLabel("GAME OVER", JLabel.CENTER);
        shadowLabel.setFont(new Font("Arial", Font.BOLD, 80));
        shadowLabel.setForeground(new Color(0, 0, 0, 150));
        shadowLabel.setBounds(3, 3, 600, 120);

        JPanel labelPanel = new JPanel(null);
        labelPanel.setOpaque(false);
        labelPanel.add(shadowLabel);
        labelPanel.add(gameOverLabel);
        gameOverLabel.setBounds(0, 0, 600, 120);
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
                gameOverLabel.setBounds(wobbleOffset, 0, 600, 120);
                shadowLabel.setBounds(3 + wobbleOffset, 3, 600, 120);
                labelPanel.repaint();
            }
        });
        wobbleTimer.start();
    }
}

