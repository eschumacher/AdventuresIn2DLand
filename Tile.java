/*
 * Tile.java
 *
 * Created on May 12, 2005, 10:08 PM
 */

package FinalProject;

import java.awt.*;

/**
 *
 * @author  Erik Schumacher
 */
public class Tile implements Space
{
    private final int CLASSVALUE = Constants.TILE;
    
    private Image image;
    private Location loc;
    private Game game;
    
    /** Creates a new instance of Tile */
    public Tile(Image i, Location Loc, Game g)
    {
        image = i;
        loc = Loc;
        game = g;
    }
    
    public void draw(Graphics page)
    {
        page.drawImage(image, loc.column() * Constants.FACTOR, loc.row() * Constants.FACTOR + Constants.ADDY, game);
    }
    
    public Image getImage()
    {
        return image;
    }
    
    public Location getLocation()
    {
        return loc;
    }
    
    public void setLocation(Location Loc)
    {
        loc = Loc;
    }
    
    public String toString()
    {
        return "TILE: " + loc.toString();
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
