import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Soup
 */
public class GamePanel extends JLayeredPane implements MouseMotionListener, MouseListener {

    private Image bgImage;
    private Image peashooterImage;
    private Image freezePeashooterImage;
    private Image sunflowerImage;
    private Image peaImage;
    private Image freezePeaImage;

    private Image normalZombieImage;
    private Image coneHeadZombieImage;
    private Collider[] colliders;

    private ArrayList<ArrayList<Zombie>> laneZombies;
    private ArrayList<ArrayList<Pea>> lanePeas;
    private ArrayList<Sun> activeSuns;

    private Timer redrawTimer;
    private Timer advancerTimer;
    private Timer sunProducer;
    private Timer zombieProducer;
    private JLabel sunScoreboard;

    private GameWindow.PlantType activePlantingBrush = GameWindow.PlantType.None;

    private int mouseX, mouseY;

    private int sunScore;
    
    // Floating text overlay for game over/won
    private String overlayText = null;
    private Color overlayColor = null;
    private int overlayWobbleOffset = 0;
    private boolean overlayWobbleRight = true;
    private float overlayShinyOffset = 0.0f;
    private boolean showOverlayOK = false;

    public int getSunScore() {
        return sunScore;
    }

    public void setSunScore(int sunScore) {
        this.sunScore = sunScore;
        sunScoreboard.setText(String.valueOf(sunScore));
    }

    public GamePanel(JLabel sunScoreboard) {
        setSize(1000, 752);
        setLayout(null);
        addMouseMotionListener(this);
        addMouseListener(this);
        this.sunScoreboard = sunScoreboard;
        setSunScore(150);  // Start every round with 150 suns
        
        // Read level from file when panel is created to ensure correct level
        LevelData levelData = new LevelData();
        // Ensure level is correctly set
        if (LevelData.LEVEL_NUMBER == null || LevelData.LEVEL_NUMBER.isEmpty()) {
            LevelData.LEVEL_NUMBER = "1";
        }
        System.out.println("GamePanel initialized with level: " + LevelData.LEVEL_NUMBER);
        System.out.println("Level 1 zombies: " + java.util.Arrays.toString(LevelData.LEVEL_CONTENT[0]));

        bgImage = new ImageIcon(this.getClass().getResource("images/mainBG.png")).getImage();

        peashooterImage = new ImageIcon(this.getClass().getResource("images/plants/peashooter.gif")).getImage();
        freezePeashooterImage = new ImageIcon(this.getClass().getResource("images/plants/freezepeashooter.gif")).getImage();
        sunflowerImage = new ImageIcon(this.getClass().getResource("images/plants/sunflower.gif")).getImage();
        peaImage = new ImageIcon(this.getClass().getResource("images/pea.png")).getImage();
        freezePeaImage = new ImageIcon(this.getClass().getResource("images/freezepea.png")).getImage();

        normalZombieImage = new ImageIcon(this.getClass().getResource("images/zombies/zombie1.png")).getImage();
        coneHeadZombieImage = new ImageIcon(this.getClass().getResource("images/zombies/zombie2.png")).getImage();

        laneZombies = new ArrayList<>();
        laneZombies.add(new ArrayList<>()); //line 1
        laneZombies.add(new ArrayList<>()); //line 2
        laneZombies.add(new ArrayList<>()); //line 3
        laneZombies.add(new ArrayList<>()); //line 4
        laneZombies.add(new ArrayList<>()); //line 5

        lanePeas = new ArrayList<>();
        lanePeas.add(new ArrayList<>()); //line 1
        lanePeas.add(new ArrayList<>()); //line 2
        lanePeas.add(new ArrayList<>()); //line 3
        lanePeas.add(new ArrayList<>()); //line 4
        lanePeas.add(new ArrayList<>()); //line 5

        colliders = new Collider[45];
        for (int i = 0; i < 45; i++) {
            Collider a = new Collider();
            a.setLocation(44 + (i % 9) * 100, 109 + (i / 9) * 120);
            a.setAction(new PlantActionListener((i % 9), (i / 9)));
            colliders[i] = a;
            add(a, new Integer(0));
        }

        //colliders[0].setPlant(new FreezePeashooter(this,0,0));
/*
        colliders[9].setPlant(new Peashooter(this,0,1));
        laneZombies.get(1).add(new NormalZombie(this,1));*/

        activeSuns = new ArrayList<>();

        redrawTimer = new Timer(25, (ActionEvent e) -> {
            repaint();
        });
        redrawTimer.start();

        advancerTimer = new Timer(60, (ActionEvent e) -> advance());
        advancerTimer.start();

        // Sun generation rate increases with level
        int levelForSun = Integer.parseInt(LevelData.LEVEL_NUMBER);
        int sunInterval = 3000; // Default 3 seconds for level 1
        if (levelForSun == 2) {
            sunInterval = 1500; // 1.5 seconds for level 2
        } else if (levelForSun >= 3) {
            sunInterval = 1000; // 1 second for level 3+
        }
        
        sunProducer = new Timer(sunInterval, (ActionEvent e) -> {
            Random rnd = new Random();
            Sun sta = new Sun(this, rnd.nextInt(800) + 100, 0, rnd.nextInt(300) + 200);
            activeSuns.add(sta);
            add(sta, Integer.valueOf(1));
        });
        sunProducer.start();

        // Spawn rate based on level (faster spawns in higher levels)
        int level = Integer.parseInt(LevelData.LEVEL_NUMBER);
        System.out.println("Zombie spawner initialized for level: " + level);
        int spawnInterval = 3000; // Default 3 seconds for level 1 (was 7000)
        if (level == 2) {
            spawnInterval = 2000; // 2 seconds for level 2
        } else if (level >= 3) {
            spawnInterval = 1500; // 1.5 seconds for level 3+
        }
        
        zombieProducer = new Timer(spawnInterval, (ActionEvent e) -> {
            Random rnd = new Random();
            // Use static access to get current level
            int currentLevel = Integer.parseInt(LevelData.LEVEL_NUMBER);
            if (currentLevel < 1 || currentLevel > LevelData.LEVEL_CONTENT.length) {
                currentLevel = 1; // Safety check
            }
            System.out.println("Spawning zombie for level: " + currentLevel);
            String[] Level = LevelData.LEVEL_CONTENT[currentLevel - 1];
            int[][] LevelValue = LevelData.LEVEL_VALUE[currentLevel - 1];
            int l = rnd.nextInt(5);
            int t = rnd.nextInt(100);
            Zombie z = null;
            for (int i = 0; i < LevelValue.length; i++) {
                System.out.println("Checking zombie type: " + Level[i] + " for t=" + t + " in range [" + LevelValue[i][0] + ", " + LevelValue[i][1] + "]");
                if (t >= LevelValue[i][0] && t <= LevelValue[i][1]) {
                    z = Zombie.getZombie(Level[i], GamePanel.this, l);
                    System.out.println("Spawning: " + Level[i] + " at lane " + l);
                    break;  // Important: break after finding matching zombie type
                }
            }
            if (z != null) {
                // Ensure zombie starts visible (panel width is 1000, posX=1000 is off-screen)
                try {
                    z.setPosX(950); // place slightly inside right edge so it is visible
                } catch (Exception ex) {
                    // ignore
                }
                laneZombies.get(l).add(z);
            } else {
                System.out.println("Warning: No zombie spawned! Level: " + currentLevel + ", t: " + t);
            }
        });
        zombieProducer.start();

    }

    private void advance() {
        // Reset all plants' beingEaten flag at start of each frame
        for (int i = 0; i < 45; i++) {
            if (colliders[i].assignedPlant != null) {
                colliders[i].assignedPlant.setBeingEaten(false);
            }
        }
        
        for (int i = 0; i < 5; i++) {
            // Process peas first (they check collision and damage zombies)
            for (int j = 0; j < lanePeas.get(i).size(); j++) {
                Pea p = lanePeas.get(i).get(j);
                p.advance();
            }

            // Remove dead zombies after peas have processed
            laneZombies.get(i).removeIf(z -> z.getHealth() <= 0);
            
            // Then advance zombies
            for (Zombie z : laneZombies.get(i)) {
                z.advance();
            }
        }

        for (int i = 0; i < activeSuns.size(); i++) {
            activeSuns.get(i).advance();
        }
        
        // Update overlay animations
        if (overlayText != null) {
            if (overlayWobbleRight) {
                overlayWobbleOffset += 2;
                if (overlayWobbleOffset >= 10) {
                    overlayWobbleRight = false;
                }
            } else {
                overlayWobbleOffset -= 2;
                if (overlayWobbleOffset <= -10) {
                    overlayWobbleRight = true;
                }
            }
            if (overlayColor == Color.GREEN) {
                // Shiny animation for game won
                overlayShinyOffset += 0.05f;
                if (overlayShinyOffset > 1.5f) {
                    overlayShinyOffset = -0.5f;
                }
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(bgImage, 0, 0, null);

        //Draw Plants
        for (int i = 0; i < 45; i++) {
            Collider c = colliders[i];
            if (c.assignedPlant != null) {
                Plant p = c.assignedPlant;
                int plantX = 60 + (i % 9) * 100;
                int plantY = 129 + (i / 9) * 120;
                
                // Draw plant with red tint if being eaten
                if (p.isBeingEaten()) {
                    // Draw red overlay
                    g.setColor(new Color(255, 0, 0, 100));
                    g.fillRect(plantX, plantY, 80, 80);
                }
                
                if (p instanceof Peashooter) {
                    g.drawImage(peashooterImage, plantX, plantY, null);
                }
                if (p instanceof FreezePeashooter) {
                    g.drawImage(freezePeashooterImage, plantX, plantY, null);
                }
                if (p instanceof Sunflower) {
                    g.drawImage(sunflowerImage, plantX, plantY, null);
                }
            }
        }

        for (int i = 0; i < 5; i++) {
            for (Zombie z : laneZombies.get(i)) {
                if (z instanceof NormalZombie) {
                    g.drawImage(normalZombieImage, z.getPosX(), 109 + (i * 120), null);
                } else if (z instanceof ConeHeadZombie) {
                    g.drawImage(coneHeadZombieImage, z.getPosX(), 109 + (i * 120), null);
                }
            }

            for (int j = 0; j < lanePeas.get(i).size(); j++) {
                Pea pea = lanePeas.get(i).get(j);
                if (pea instanceof FreezePea) {
                    g.drawImage(freezePeaImage, pea.getPosX(), 130 + (i * 120), null);
                } else {
                    g.drawImage(peaImage, pea.getPosX(), 130 + (i * 120), null);
                }
            }

        }

    // Draw progress (e.g., "Progress: 30 / 150") below the sun indicator
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    int levelForDisplay = 1;
    try { levelForDisplay = Integer.parseInt(LevelData.LEVEL_NUMBER); } catch (Exception ex) { levelForDisplay = 1; }
    int thresholdForDisplay = 150 * levelForDisplay;
    g2.setFont(new Font("Arial", Font.BOLD, 20));
    g2.setColor(Color.WHITE);
    // shadow
    g2.setColor(new Color(0,0,0,160));
    g2.drawString("Progress: " + progress + " / " + thresholdForDisplay, 30, 50);
    g2.setColor(Color.WHITE);
    g2.drawString("Progress: " + progress + " / " + thresholdForDisplay, 28, 48);

        // Draw floating overlay text for game over/won
        if (overlayText != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            Font overlayFont = new Font("Arial", Font.BOLD, 80);
            FontMetrics fm = g2d.getFontMetrics(overlayFont);
            int textWidth = fm.stringWidth(overlayText);
            int textHeight = fm.getHeight();
            
            // Draw shadow
            g2d.setFont(overlayFont);
            g2d.setColor(new Color(0, 0, 0, 150));
            g2d.drawString(overlayText, centerX - textWidth/2 + 3 + overlayWobbleOffset, centerY + 3);
            
            // Draw main text
            g2d.setColor(overlayColor);
            g2d.drawString(overlayText, centerX - textWidth/2 + overlayWobbleOffset, centerY);
            
            // Draw shiny effect for game won
            if (overlayColor == Color.GREEN) {
                int shinyX = (int)(centerX - textWidth/2 + (overlayShinyOffset * textWidth));
                int shinyWidth = textWidth / 4;
                for (int i = 0; i < 3; i++) {
                    int alpha = 150 - (i * 30);
                    if (alpha > 0) {
                        g2d.setColor(new Color(255, 255, 255, alpha));
                        int offset = shinyX - (shinyWidth / 2) + (i * 10);
                        if (offset >= centerX - textWidth/2 && offset < centerX + textWidth/2) {
                            g2d.fillRect(offset, centerY - textHeight, shinyWidth, textHeight);
                        }
                    }
                }
            }
            
            // Draw OK button
            if (showOverlayOK) {
                int buttonX = centerX - 75;
                int buttonY = centerY + 80;
                g2d.setColor(Color.BLACK);
                g2d.fillRect(buttonX, buttonY, 150, 50);
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.BOLD, 24));
                FontMetrics buttonFm = g2d.getFontMetrics();
                int okWidth = buttonFm.stringWidth("OK");
                g2d.drawString("OK", buttonX + (150 - okWidth) / 2, buttonY + 35);
            }
        }

    }

    private class PlantActionListener implements ActionListener {

        int x, y;

        public PlantActionListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (activePlantingBrush == GameWindow.PlantType.Sunflower) {
                if (getSunScore() >= 50) {
                    colliders[x + y * 9].setPlant(new Sunflower(GamePanel.this, x, y));
                    setSunScore(getSunScore() - 50);
                }
            }
            if (activePlantingBrush == GameWindow.PlantType.Peashooter) {
                if (getSunScore() >= 100) {
                    colliders[x + y * 9].setPlant(new Peashooter(GamePanel.this, x, y));
                    setSunScore(getSunScore() - 100);
                }
            }

            if (activePlantingBrush == GameWindow.PlantType.FreezePeashooter) {
                if (getSunScore() >= 175) {
                    colliders[x + y * 9].setPlant(new FreezePeashooter(GamePanel.this, x, y));
                    setSunScore(getSunScore() - 175);
                }
            }
            activePlantingBrush = GameWindow.PlantType.None;
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // Check if clicking OK button on overlay
        if (overlayText != null && showOverlayOK) {
            int centerX = getWidth() / 2;
            int centerY = getHeight() / 2;
            int buttonX = centerX - 75;
            int buttonY = centerY + 80;
            int x = e.getX();
            int y = e.getY();
            System.out.println("Click detected at: (" + x + ", " + y + "), Button area: (" + buttonX + ", " + buttonY + ") to (" + (buttonX + 150) + ", " + (buttonY + 50) + ")");
            if (x >= buttonX && x < buttonX + 150 && y >= buttonY && y < buttonY + 50) {
                // OK button clicked - quit game
                System.out.println("OK button clicked - quitting game");
                overlayText = null;
                showOverlayOK = false;
                repaint();
                System.exit(0);
            }
            // Return so overlay click doesn't trigger plant placement
            return;
        }
        // Handle drag and drop placement
        if (activePlantingBrush != GameWindow.PlantType.None && overlayText == null) {
            int x = e.getX();
            int y = e.getY();
            // Check if click is on a collider
            for (int i = 0; i < 45; i++) {
                Collider c = colliders[i];
                if (x >= c.getX() && x < c.getX() + c.getWidth() &&
                    y >= c.getY() && y < c.getY() + c.getHeight()) {
                    // Trigger the plant action
                    ActionListener action = c.getAction();
                    if (action != null) {
                        action.actionPerformed(new ActionEvent(c, ActionEvent.RESERVED_ID_MAX + 1, ""));
                    }
                    break;
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    static int progress = 0;
    private static boolean levelTransitioning = false;

    public static void setProgress(int num) {
        if (levelTransitioning) {
            return;
        }
        progress = progress + num;
        int level = 1;
        try { level = Integer.parseInt(LevelData.LEVEL_NUMBER); } catch (Exception ex) { level = 1; }
        int threshold = 150 * level;
        System.out.println("Progress: " + progress + "/" + threshold);
        if (progress >= threshold) {
            String currentLevel = LevelData.LEVEL_NUMBER;
            System.out.println("Level completion triggered. Current level: " + currentLevel);
            levelTransitioning = true;
            if ("1".equals(currentLevel)) {
                JOptionPane.showMessageDialog(null, "LEVEL 1 CROSSED!\nLevel 2 starting now...");
                GameWindow.gw.dispose();
                LevelData.LEVEL_NUMBER = "2";
                LevelData.write("2");
                progress = 0;
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                GameWindow.gw = new GameWindow();
                levelTransitioning = false;
            } else if ("2".equals(currentLevel)) {
                JOptionPane.showMessageDialog(null, "LEVEL 2 CROSSED!\nLevel 3 starting now...");
                GameWindow.gw.dispose();
                LevelData.LEVEL_NUMBER = "3";
                LevelData.write("3");
                progress = 0;
                try { Thread.sleep(100); } catch (InterruptedException e) { e.printStackTrace(); }
                GameWindow.gw = new GameWindow();
                levelTransitioning = false;
            } else if ("3".equals(currentLevel)) {
                // Only show GAME WON overlay after all rounds
                if (GameWindow.currentGamePanel != null) {
                    GameWindow.currentGamePanel.showGameWonOverlay();
                }
                levelTransitioning = false;
            } else {
                System.out.println("Unexpected level: " + currentLevel);
                JOptionPane.showMessageDialog(null, "LEVEL " + currentLevel + " COMPLETED!");
                System.exit(0);
                levelTransitioning = false;
            }
        }
    }

    public GameWindow.PlantType getActivePlantingBrush() {
        return activePlantingBrush;
    }

    public void setActivePlantingBrush(GameWindow.PlantType activePlantingBrush) {
        this.activePlantingBrush = activePlantingBrush;
    }

    public ArrayList<ArrayList<Zombie>> getLaneZombies() {
        return laneZombies;
    }

    public void setLaneZombies(ArrayList<ArrayList<Zombie>> laneZombies) {
        this.laneZombies = laneZombies;
    }

    public ArrayList<ArrayList<Pea>> getLanePeas() {
        return lanePeas;
    }

    public void setLanePeas(ArrayList<ArrayList<Pea>> lanePeas) {
        this.lanePeas = lanePeas;
    }

    public ArrayList<Sun> getActiveSuns() {
        return activeSuns;
    }

    public void setActiveSuns(ArrayList<Sun> activeSuns) {
        this.activeSuns = activeSuns;
    }

    public Collider[] getColliders() {
        return colliders;
    }

    public void setColliders(Collider[] colliders) {
        this.colliders = colliders;
    }
    
    public void showGameOverOverlay() {
        overlayText = "GAME OVER";
        overlayColor = Color.RED;
        showOverlayOK = true;
        overlayWobbleOffset = 0;
        overlayWobbleRight = true;
        // Stop game timers
        if (redrawTimer != null) redrawTimer.stop();
        if (advancerTimer != null) advancerTimer.stop();
        if (sunProducer != null) sunProducer.stop();
        if (zombieProducer != null) zombieProducer.stop();
        repaint();
    }
    
    public void showGameWonOverlay() {
        overlayText = "GAME WON";
        overlayColor = Color.GREEN;
        showOverlayOK = true;
        overlayWobbleOffset = 0;
        overlayWobbleRight = true;
        overlayShinyOffset = 0.0f;
        // Stop game timers
        if (redrawTimer != null) redrawTimer.stop();
        if (advancerTimer != null) advancerTimer.stop();
        if (sunProducer != null) sunProducer.stop();
        if (zombieProducer != null) zombieProducer.stop();
        repaint();
    }
}
