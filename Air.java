/*
 * Air.java
 *
 * Created on May 12, 2005, 10:36 PM
 */

package FinalProject;

/**
 *
 * @author  Erik
 */
public class Air implements Space
{
    private final int CLASSVALUE = Constants.AIR;
    
    private Location loc;
    private Game game;
    
    /** Creates a new instance of Air */
    public Air(Location Loc, Game g)
    {
        loc = Loc;
        game = g;
    }
    
    public Location getLocation()
    {
        return loc;
    }
    
    public String toString()
    {
        return "AIR: " + loc.toString();
    }
    
    public void draw(java.awt.Graphics page)
    {
        page.clearRect(loc.column() * Constants.FACTOR, loc.row() * Constants.FACTOR + Constants.ADDY, Constants.FACTOR, Constants.FACTOR);
    }
    
    public int getClassValue()
    {
        return CLASSVALUE;
    }
    
    public Space[][] move(Space[][] spaces){return spaces;}
    
    public boolean skipNextIndex(Space[][] spaces){return false;}
    
    public boolean skipNextYIndex(Space[][] spaces) {return false;}
    
    public boolean checkWinLevel(){return false;}
    
    public void setWinLevel(boolean b){}
    
}
