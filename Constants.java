/*
 * Constants.java
 *
 * Created on May 13, 2005, 8:52 AM
 */

package FinalProject;

import java.awt.Font;

/**
 *
 * @author  DHHS-AP
 */
public class Constants
{
    static final int AIR = 0;
    static final int PLAYER = 1;
    static final int TILE = 2;
    static final int TRIANGLE = 3;
    static final int WARP = 4;
    static final int COIN = 5;
    
    static final int LEFT = 1;
    static final int LEFT2 = 2;
    static final int LEFTJUMP = 3;
    static final int RIGHT = 4;
    static final int RIGHT2 = 5;
    static final int RIGHTJUMP = 6;
    
    static final int JUMPMAX = 4;
    
    static final int MAP_CASTLE = 1;
    static final int MAP_FOREST = 2;
    
    static final int MAP_CASTLEX = 430;
    static final int MAP_CASTLEY = 170;
    static final int MAP_FORESTX = 160;
    static final int MAP_FORESTY = 275;
    
    static int ROWS = 20;
    static int COLS = 20;
    
    static final int FACTOR = 20;
    static final int ADDY = 53; //30
    
    static int PAUSE = 100;
    
    static int SCORE = 0;
    
    static final int HEADBARX = 5;
    static final int HEADBARY = 52;
    
    static Font FONT = new Font("Garamond", Font.PLAIN, 48); //new Font("BankGothic Md BT", Font.PLAIN, 48);
    
    
    static final String PATH = "E:\\Code\\OldStuff\\sampledir\\FinalProject\\";
    
    public Constants()
    {
        //added so that changes can be made to ROWS / COLS during runtime
    }
    
    public void setPause(int p)
    {
        PAUSE = p;
    }
    
    public void setRows(int r)
    {
        ROWS = r;
        System.out.println("" + ROWS);
    }
    
    public void setCols(int c)
    {
        COLS = c;
        System.out.println("" + COLS);
    }
    
    public void setScore(int s)
    {
        SCORE = s;
    }
    
    public void addScore()
    {
        SCORE += 1;
    }
}
