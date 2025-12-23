/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.logging.Logger;

/**
 * @author Soup
 */
public class LevelData {

    public static String LEVEL_NUMBER = "1";
    public static String[][] LEVEL_CONTENT = {
        {"NormalZombie"},  // Level 1: Only NormalZombie
        {"NormalZombie", "ConeHeadZombie"},  // Level 2: Both types
        {"NormalZombie", "ConeHeadZombie"}  // Level 3: Both types
    };
    public static int[][][] LEVEL_VALUE = {
        {{0, 99}},  // Level 1: 100% NormalZombie
        {{0, 49}, {50, 99}},  // Level 2: 50% Normal, 50% ConeHead
        {{0, 49}, {50, 99}}  // Level 3: 50% Normal, 50% ConeHead
    };

    public LevelData() {
        try {
            File f = new File("LEVEL_CONTENT.vbhv");

            if (!f.exists()) {
                BufferedWriter bwr = new BufferedWriter(new FileWriter(f));
                bwr.write("1");
                bwr.close();
                LEVEL_NUMBER = "1";
            } else {
                BufferedReader br = new BufferedReader(new FileReader(f));
                LEVEL_NUMBER = br.readLine();
                br.close();
            }
        } catch (Exception ex) {


        }
    }

    public static void write(String lvl) {
        File f = new File("LEVEL_CONTENT.vbhv");
        try {
            BufferedWriter bwr = new BufferedWriter(new FileWriter(f));
            bwr.write(lvl);
            bwr.close();
            LEVEL_NUMBER = lvl;
        } catch (IOException ex) {
            Logger.getLogger(LevelData.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

    }
}
