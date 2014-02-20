/*
 * Warp.java
 *
 * Created on May 12, 2005, 10:31 AM
 */

package FinalProject;

import java.awt.*;

/**
 *
 * @author  Erik Schumacher
 */
public class Warp implements Space
{
    private final int CLASSVALUE = Constants.WARP;
    
    private Image image;
    private boolean endWarp, targetWarp;
    private int target;
    private Location loc;
    private Game game;
    
    /** Creates a new instance of Warp */
    public Warp(Image i, boolean EndWarp, Location Loc, Game g)
    {
        image = i;
        endWarp = EndWarp;
        loc = Loc;
        game = g;
        
        targetWarp = false;
    }
    
    public Warp(Image i, boolean EndWarp, Location Loc, Game g, int Target)
    {
        image = i;
        endWarp = EndWarp;
        loc = Loc;
        game = g;
        
        targetWarp = true;
        target = Target;
    }
    
    public void draw(Graphics page)
    {
        page.drawImage(image, loc.column() * Constants.FACTOR, loc.row() * Constants.FACTOR + Constants.ADDY, game);
    }
    
    public boolean isEndWarp()
    {
        return endWarp;
    }
    
    public boolean isTargetWarp()
    {
        return targetWarp;
    }
    
    public int getTarget()
    {
        return target;
    }
    
    public void setTarget(int Target)
    {
        target = Target;
    }
    
    public Location getLocation()
    {
        return loc;
    }
    
    public String toString()
    {
        return "WARP: " + loc.toString();
    }
    
    public int getClassValue()
    {
        return CLASSVALUE;
    }
    
    public Space[][] move(Space[][] spaces){return spaces;}
    
    public boolean skipNextIndex(Space[][] spaces){return false;}
    
    public boolean skipNextYIndex(Space[][] spaces){return false;}
    
    public boolean checkWinLevel(){return false;}
    
    public void setWinLevel(boolean b){}
    
}
