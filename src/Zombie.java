import javax.swing.*;

/**
 * Created by Soup
 */
public class Zombie {

    private int health = 1000;
    private double speed = 0.5;  // Default slow speed

    private GamePanel gp;

    // posX is used for rendering/collision (integer), realPosX accumulates fractional movement
    private int posX = 1000;
    private double realPosX = 1000.0;
    private int myLane;
    private boolean isMoving = true;

    public Zombie(GamePanel parent, int lane) {
        this.gp = parent;
        myLane = lane;
        // Set speed based on level
        int level = Integer.parseInt(LevelData.LEVEL_NUMBER);
        if (level == 1) {
            speed = 0.5;  // Level 1: Very slow (normal Plants vs Zombies speed)
        } else if (level == 2) {
            speed = 1.0;  // Level 2: Normal speed
        } else if (level >= 3) {
            speed = 1.2;  // Level 3: 1.2x speed
        }
        // ensure realPosX matches integer posX
        realPosX = posX;
    }

    public void advance() {
        if (isMoving) {
            boolean isCollides = false;
            Collider collided = null;
            for (int i = myLane * 9; i < (myLane + 1) * 9; i++) {
                if (gp.getColliders()[i].assignedPlant != null && gp.getColliders()[i].isInsideCollider(posX)) {
                    isCollides = true;
                    collided = gp.getColliders()[i];
                }
            }
            if (!isCollides) {
                // Use realPosX to accumulate fractional movement so slow speeds (<1) still move over time
                if (slowInt > 0) {
                    if (slowInt % 2 == 0) {
                        realPosX -= speed;
                        posX = (int)Math.round(realPosX);
                    }
                    slowInt--;
                } else {
                    realPosX -= speed;
                    posX = (int)Math.round(realPosX);
                }
            } else {
                // Mark plant as being eaten
                if (collided.assignedPlant != null) {
                    collided.assignedPlant.setBeingEaten(true);
                collided.assignedPlant.setHealth(collided.assignedPlant.getHealth() - 10);
                if (collided.assignedPlant.getHealth() < 0) {
                    collided.removePlant();
                }
            }
            }
            // Only trigger game over if zombie is alive and reaches the left edge
            if (posX < 0 && health > 0) {
                isMoving = false;
                showGameOverDialog();
            }
        }
    }

    int slowInt = 0;

    public void slow() {
        slowInt = 1000;
    }

    public static Zombie getZombie(String type, GamePanel parent, int lane) {
        Zombie z = new Zombie(parent, lane);
        switch (type) {
            case "NormalZombie":
                z = new NormalZombie(parent, lane);
                break;
            case "ConeHeadZombie":
                z = new ConeHeadZombie(parent, lane);
                break;
        }
        return z;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public GamePanel getGp() {
        return gp;
    }

    public void setGp(GamePanel gp) {
        this.gp = gp;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
        this.realPosX = posX;
    }

    public int getMyLane() {
        return myLane;
    }

    public void setMyLane(int myLane) {
        this.myLane = myLane;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public int getSlowInt() {
        return slowInt;
    }

    public void setSlowInt(int slowInt) {
        this.slowInt = slowInt;
    }

    private void showGameOverDialog() {
        SwingUtilities.invokeLater(() -> {
            gp.showGameOverOverlay();
        });
    }
}
