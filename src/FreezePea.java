import java.awt.*;

/**
 * Created by Soup
 */
public class FreezePea extends Pea {

    public FreezePea(GamePanel parent, int lane, int startX) {
        super(parent, lane, startX);
    }

    @Override
    public void advance() {
        Rectangle pRect = new Rectangle(getPosX(), 130 + getMyLane() * 120, 28, 28);
        // Use iterator to safely remove zombies while iterating
        java.util.Iterator<Zombie> zombieIterator = gp.getLaneZombies().get(getMyLane()).iterator();
        while (zombieIterator.hasNext()) {
            Zombie z = zombieIterator.next();
            Rectangle zRect = new Rectangle(z.getPosX(), 109 + getMyLane() * 120, 400, 120);
            if (pRect.intersects(zRect)) {
                int oldHealth = z.getHealth();
                z.setHealth(z.getHealth() - 300);
                z.slow();
                System.out.println("FreezePea hit zombie! Health: " + oldHealth + " -> " + z.getHealth());
                if (z.getHealth() <= 0) {
                    System.out.println("ZOMBIE DIE");
                    GamePanel.setProgress(10);
                    zombieIterator.remove();
                }
                gp.getLanePeas().get(getMyLane()).remove(this);
                return; // Exit after hitting one zombie
            }
        }
        /*if(posX > 2000){
            gp.lanePeas.get(myLane).remove(this);
        }*/
        // Move forward smoothly
        int currentPos = getPosX();
        setPosX(currentPos + 15);
    }

}
