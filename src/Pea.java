import java.awt.*;

/**
 * Created by Soup
 */
public class Pea {

    private int posX;
    protected GamePanel gp;
    private int myLane;

    public Pea(GamePanel parent, int lane, int startX) {
        this.gp = parent;
        this.myLane = lane;
        posX = startX;
    }

    public void advance() {
        Rectangle pRect = new Rectangle(posX, 130 + myLane * 120, 28, 28);
        // Use iterator to safely remove zombies while iterating
        java.util.Iterator<Zombie> zombieIterator = gp.getLaneZombies().get(myLane).iterator();
        while (zombieIterator.hasNext()) {
            Zombie z = zombieIterator.next();
            // Check collision - zombie rectangle should be based on actual zombie position
            Rectangle zRect = new Rectangle(z.getPosX(), 109 + myLane * 120, 400, 120);
            if (pRect.intersects(zRect)) {
                int oldHealth = z.getHealth();
                z.setHealth(z.getHealth() - 300);
                System.out.println("Pea hit zombie! Health: " + oldHealth + " -> " + z.getHealth());
                if (z.getHealth() <= 0) {
                    System.out.println("ZOMBIE DIED");
                    zombieIterator.remove();
                    GamePanel.setProgress(10);
                }
                gp.getLanePeas().get(myLane).remove(this);
                return; // Exit after hitting one zombie
            }
        }
        /*if(posX > 2000){
            gp.lanePeas.get(myLane).remove(this);
        }*/
        posX += 15;
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getMyLane() {
        return myLane;
    }

    public void setMyLane(int myLane) {
        this.myLane = myLane;
    }
}
