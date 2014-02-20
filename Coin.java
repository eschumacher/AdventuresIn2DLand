/*
 * Coin.java
 *
 * Created on June 6, 2005, 9:59 PM
 */

package FinalProject;

import java.awt.*;
import java.util.Random;

/**
 *
 * @author  Erik Schumacher
 */
public class Coin implements Space
{
    private final int CLASSVALUE = Constants.COIN;
    
    private Image image;
    private Location loc;
    private Game game;
    
    private int direction;
    private boolean up;
    
    private static Random gen = new Random();
    
    /** Creates a new instance of Tile */
    public Coin(Image i, Location Loc, Game g)
    {
        image = i;
        loc = Loc;
        game = g;
        
        if (gen.nextInt(2) == 1)
            up = true;
        else
            up = false;
        
        direction = gen.nextInt(5) + 1;
    }
    
    public void draw(Graphics page)
    {
        page.drawImage(image, loc.column() * Constants.FACTOR, loc.row() * Constants.FACTOR + Constants.ADDY, loc.column() * Constants.FACTOR + Constants.FACTOR, loc.row() * Constants.FACTOR + Constants.ADDY + Constants.FACTOR, (direction - 1) * 20 + 1, 1, (direction - 1) * 20 + 20, 20, game);
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
    
    public Space[][] move(Space[][] spaces)
    {
        if (up)
        {
            if (direction == 5)
            {
                up = false;
                direction -= 1;
            }
            else
                direction += 1;
        }
        else
        {
            if (direction == 1)
            {
                up = true;
                direction += 1;
            }
            else
                direction -= 1;
        }        
        
        return spaces;
    }
    
    public boolean skipNextIndex(Space[][] spaces){return false;}
    
    public boolean skipNextYIndex(Space[][] spaces){return false;}
    
    public boolean checkWinLevel(){return false;}
    
    public void setWinLevel(boolean b){}
    
}
