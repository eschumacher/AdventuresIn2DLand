/*
 * Triangle.java
 *
 * Created on May 12, 2005, 10:24 AM
 */

package FinalProject;

import java.awt.*;
import java.applet.AudioClip;
import java.io.*;
import javax.swing.JApplet;
/**
 *
 * @author  DHHS-AP
 */
public class Triangle implements Space
{
    private final int CLASSVALUE = Constants.TRIANGLE;
    
    private Image image;
    private boolean dead;
    private int direction;  // 1 = left1, 2 = left2, 3 = right1, 4 = right2, -1 = dead
    private Location loc;
    private Game game;
    private boolean playerKilled;
    
    static AudioClip die = null;
    
    /** Creates a new instance of Triangle */
    public Triangle(Image i, Location Loc, Game g)
    {
        image = i;
        dead = false;
        direction = 1;
        loc = Loc;
        game = g;
        playerKilled = false;
        
        if (die == null)
        {
            try{
                die = JApplet.newAudioClip(new File(Constants.PATH + "Music\\ouch.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
    }
    
    public Triangle(Image i, Location Loc, Game g, int Direction)
    {
        image = i;
        dead = false;
        direction = Direction;
        loc = Loc;
        game = g;
        playerKilled = false;
        
        if (die == null)
        {
            try{
                die = JApplet.newAudioClip(new File(Constants.PATH + "Music\\ouch.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
    }
    
    public void draw(Graphics page)
    {
        page.drawImage(image, loc.column() * Constants.FACTOR, loc.row() * Constants.FACTOR + Constants.ADDY, loc.column() * Constants.FACTOR + Constants.FACTOR, loc.row() * Constants.FACTOR + Constants.ADDY + Constants.FACTOR, (direction - 1) * 20 + 1, 1, (direction - 1) * 20 + 20, 20, game);
    }
    
    public Space[][] tryToKillPlayer(Space[][] spaces)
    {
        if ((direction == 1 || direction == 2) && canMoveForward(spaces) && !noFloor())
        {
            if (spaces[loc.row()][loc.column() - 1].getClassValue() == Constants.PLAYER)
            {
                ((Player) spaces[loc.row()][loc.column() - 1]).kill();
                playerKilled = true;
            }
        }
        else if ((direction == 3 || direction == 4) && canMoveForward(spaces) && !noFloor())
        {
            if (spaces[loc.row()][loc.column() + 1].getClassValue() == Constants.PLAYER)
            {
                ((Player) spaces[loc.row()][loc.column() + 1]).kill();
                playerKilled = true;
            }
        }
        
        return spaces;
    }
    
    public Space[][] move(Space[][] spaces)
    {
        if (!dead)
        {
            tryToKillPlayer(spaces);
            
            if (playerKilled)
                return spaces;
            
            if (!canMoveForward(spaces) || noFloor())
            {
                if (direction == 1 || direction == 2)
                    direction = 3;
                else
                    direction = 1;
                
                spaces[loc.row()][loc.column()] = new Triangle(image, new Location(loc.row(), loc.column()), game, direction);
            }
            else
            {
                if (direction % 2 == 1)
                    direction ++;
                else
                    direction --;
                
                if (direction == 1 || direction == 2)
                    spaces[loc.row()][loc.column() - 1] = new Triangle(image, new Location(loc.row(), loc.column() - 1), game, direction);
                else
                    spaces[loc.row()][loc.column() + 1] = new Triangle(image, new Location(loc.row(), loc.column() + 1), game, direction);
                
                spaces[loc.row()][loc.column()] = new Air(new Location(loc.row(), loc.column()), game);
            }
        }
        return spaces;
    }
    
    public boolean isPlayerKilled()
    {
        return playerKilled;
    }
    
    public boolean noFloor()
    {
        int addX = 1;

        if (direction == 1 || direction == 2)
            addX = -1;

        if (Game.level[loc.row() + 1][loc.column() + addX].getClassValue() == Constants.TILE)
            return false;
        
        return true;
    }
    
    public boolean canMoveForward(Space[][] spaces)
    {
        if (!dead)
        {
            int addX = 1;
            
            if (direction == 1 || direction == 2)
                addX = -1;
            
            if (loc.column() + addX > Constants.COLS - 1 || loc.column() + addX < 0)
                return false;
            else if (spaces[loc.row()][loc.column() + addX].getClassValue() >= Constants.TILE && spaces[loc.row()][loc.column() + addX].getClassValue() <= Constants.WARP)
                return false;
            
            return true;
        }
        
        return false;
    }
    
    public boolean skipNextIndex(Space[][] spaces)
    {
        return ((direction == 3 || direction == 4) && canMoveForward(spaces) && !noFloor());
    }
    
    public Location getLocation()
    {
        return loc;
    }
    
    public void setLocation(Location Loc)
    {
        loc = Loc;
    }
    
    public boolean isDead()
    {
        return dead;
    }
    
    public void kill()
    {
        if (!dead)
        {
            die.play();
            direction = 5;
            dead = true;
        }
    }
    
    public String toString()
    {
        return "TRIANGLE: " + loc.toString();
    }
    
    public int getClassValue()
    {
        return CLASSVALUE;
    }    
    
    public boolean skipNextYIndex(Space[][] spaces){return false;}
    
    public boolean checkWinLevel(){return false;}
    
    public void setWinLevel(boolean b){}
    
}
