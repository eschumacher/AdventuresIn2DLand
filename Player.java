/*
 * Player.java
 *
 * Created on May 12, 2005, 9:45 PM
 */

package FinalProject;

import java.awt.*;
import java.applet.AudioClip;
import java.io.*;
import javax.swing.JApplet;
import java.util.Random;

/**
 *
 * @author  Erik
 */
public class Player implements Space
{
    private final int CLASSVALUE = Constants.PLAYER;
    
    private Image image;
    private int direction;
    private boolean dead;
    private Location loc;
    private Game game;
    
    static int score = 0, previousScore = 0, lives = 5;
    
    private static Random gen = new Random();
    
    private static boolean moveUp = false, moveLeft = false, moveRight = false;
    
    private static boolean winLevel = false, gameover = false;
    
    private static int jumpCount = 0;
    private static boolean jumpUp = false;
    private static boolean jumpDown = false;
    private static boolean skipNextY = false;
    
    static AudioClip land = null, jump = null, die = null, alright = null, whoa = null, haha = null, wohoo = null, bling = null, yeah = null;
    
    /** Creates a new instance of Player */
    public Player(Image i, Location Loc, Game g)
    {
        image = i;
        loc = Loc;
        direction = 4;
        game = g;
        dead = false;
        
        if (land == null)
        {
            try{
                land = JApplet.newAudioClip(new File(Constants.PATH + "Music\\landing.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (jump == null)
        {
            try{
                jump = JApplet.newAudioClip(new File(Constants.PATH + "Music\\jump.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (die == null)
        {
            try{
                die = JApplet.newAudioClip(new File(Constants.PATH + "Music\\die.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (alright == null)
        {
            try{
                alright = JApplet.newAudioClip(new File(Constants.PATH + "Music\\alright.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (whoa == null)
        {
            try{
                whoa = JApplet.newAudioClip(new File(Constants.PATH + "Music\\whoa.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (haha == null)
        {
            try{
                haha = JApplet.newAudioClip(new File(Constants.PATH + "Music\\haha.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (wohoo == null)
        {
            try{
                wohoo = JApplet.newAudioClip(new File(Constants.PATH + "Music\\wohoo.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (yeah == null)
        {
            try{
                yeah = JApplet.newAudioClip(new File(Constants.PATH + "Music\\yeah.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (bling == null)
        {
            try{
                bling = JApplet.newAudioClip(new File(Constants.PATH + "Music\\blingbling.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
    }
    
    public Player(Image i, Location Loc, Game g, int Direction)
    {
        image = i;
        loc = Loc;
        direction = Direction;
        game = g;
        dead = false;
        
        if (land == null)
        {
            try{
                land = JApplet.newAudioClip(new File(Constants.PATH + "Music\\landing.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (jump == null)
        {
            try{
                jump = JApplet.newAudioClip(new File(Constants.PATH + "Music\\jump.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (die == null)
        {
            try{
                die = JApplet.newAudioClip(new File(Constants.PATH + "Music\\die.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (alright == null)
        {
            try{
                alright = JApplet.newAudioClip(new File(Constants.PATH + "Music\\alright.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
        
        if (whoa == null)
        {
            try{
                whoa = JApplet.newAudioClip(new File(Constants.PATH + "Music\\whoa.wav").toURL());
            }catch(Exception e){System.out.println(e.toString());}
        }
    }
    
    public void draw(Graphics page)
    {
        page.drawImage(image, loc.column() * Constants.FACTOR, loc.row() * Constants.FACTOR + Constants.ADDY - Constants.FACTOR, loc.column() * Constants.FACTOR + Constants.FACTOR, loc.row() * Constants.FACTOR + Constants.ADDY + Constants.FACTOR, (direction - 1) * 20 + 1, 1, (direction - 1) * 20 + 20, 40, game);
    }
    
    public Space[][] move(Space[][] spaces)
    {
        return move(spaces, 0);
    }
    
    public Space[][] move(Space[][] spaces, int keycode)
    {
        if (!dead)
        {
            //Make this work now~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
            int futureX = 0, futureY = 0;
            boolean turned_around = false;

            if (moveUp && !jumpDown)
            {
                if (jumpCount == 0 && !jumpUp)
                    jump.play();
                
                jumpCount++;
                jumpUp = true;

                if (direction < Constants.LEFTJUMP)
                    direction = Constants.LEFTJUMP;
                else if (direction > Constants.LEFTJUMP || direction < Constants.RIGHTJUMP)
                    direction = Constants.RIGHTJUMP;

                if (jumpCount > Constants.JUMPMAX)
                {
                    jumpUp = false;
                    jumpDown = true;
                    jumpCount = 0;
                }

                if (!tileAbove(spaces) && jumpUp)
                    futureY -= 1;
                else if (tileAbove(spaces))
                {
                    jumpUp = false;
                    jumpDown = true;
                    jumpCount = 0;
                    
                    if (triangleAboveTile(spaces))
                    {
                        ((Triangle) spaces[loc.row() - 3][loc.column()]).kill();
                        
                        if (loc.row() - 4 >= 0 && spaces[loc.row() - 4][loc.column()].getClassValue() == Constants.AIR)
                            spaces[loc.row() - 4][loc.column()] = new Coin(Game.getImageResource("Images\\Coin.JPG"), new Location(loc.row() - 4, loc.column()), game);
                    }
                }
            }
            else if (!tileBelow(spaces))
            {
                if (!jumpDown)
                    jumpDown = true;

                if (direction < Constants.LEFTJUMP)
                    direction = Constants.LEFTJUMP;
                else if (direction > Constants.LEFTJUMP || direction < Constants.RIGHTJUMP)
                    direction = Constants.RIGHTJUMP;

                futureY += 1;
                skipNextY = true;
            }

            if (tileBelow(spaces))
            {
                if (jumpDown && !moveUp)
                    land.play();
                
                jumpDown = false;
                jumpCount = 0;

                if (direction == Constants.LEFTJUMP)
                    direction = Constants.LEFT;
                else if (direction == Constants.RIGHTJUMP)
                    direction = Constants.RIGHT;
            }

            if (moveLeft)
            {
                if (direction == Constants.LEFT)
                    direction = Constants.LEFT2;
                else if (direction == Constants.LEFT2)
                    direction = Constants.LEFT;
                else if (direction == Constants.RIGHTJUMP)
                    direction = Constants.LEFTJUMP;
                else if (direction == Constants.RIGHT || direction == Constants.RIGHT2)
                {
                    direction = Constants.LEFT;
                    turned_around = true;
                }

                if (canMoveForward(spaces, loc.column() + futureX, loc.row() + futureY) && !turned_around)
                    futureX -= 1;
            }

            if (moveRight)
            {
                if (direction == Constants.RIGHT)
                    direction = Constants.RIGHT2;
                else if (direction == Constants.RIGHT2)
                    direction = Constants.RIGHT;
                else if (direction == Constants.LEFTJUMP)
                    direction = Constants.RIGHTJUMP;
                else if (direction == Constants.LEFT || direction == Constants.LEFT2)
                {
                    direction = Constants.RIGHT;
                    turned_around = true;
                }

                if (canMoveForward(spaces, loc.column() + futureX, loc.row() + futureY) && !turned_around)
                    futureX += 1;
            }
            
            if (spaces[loc.row() + futureY][loc.column() + futureX].getClassValue() == Constants.COIN || (loc.row() + futureY - 1 > 0 && spaces[loc.row() + futureY - 1][loc.column() + futureX].getClassValue() == Constants.COIN))
            {
                int tmp = gen.nextInt(4);
                if (tmp == 0)
                    yeah.play();
                else if (tmp == 1)
                    wohoo.play();
                else if (tmp == 2)
                    haha.play();
                else
                    bling.play();
                
                score += 1;
                
                if (spaces[loc.row() + futureY - 1][loc.column() + futureX].getClassValue() == Constants.COIN)
                    spaces[loc.row() + futureY - 1][loc.column() + futureX] = new Air(new Location(loc.row() + futureY - 1, loc.column() + futureX), game);
            }
            
            if (spaces[loc.row() + futureY][loc.column() + futureX].getClassValue() == Constants.TRIANGLE)
            {
                if (!((Triangle) spaces[loc.row() + futureY][loc.column() + futureX]).isDead())
                    kill();
                else
                {
                    futureY -= 1;
                    spaces[loc.row()][loc.column()] = new Air(new Location(loc.row(), loc.column()), game);
                    spaces[loc.row() + futureY][loc.column() + futureX] = new Player(image, new Location(loc.row() + futureY, loc.column() + futureX), game, direction);
                }
            }
            else if (loc.row() + futureY > 0 && spaces[loc.row() + futureY - 1][loc.column() + futureX].getClassValue() == Constants.TRIANGLE && !((Triangle) spaces[loc.row() + futureY][loc.column() + futureX]).isDead())
                kill();
            else if (spaces[loc.row() + futureY][loc.column() + futureX].getClassValue() == Constants.WARP)
                warp(spaces, spaces[loc.row() + futureY][loc.column() + futureX].getLocation());
            else if (loc.row() + futureY > 0 && spaces[loc.row() + futureY - 1][loc.column() + futureX].getClassValue() == Constants.WARP)
                warp(spaces, spaces[loc.row() + futureY - 1][loc.column() + futureX].getLocation());
            else
            {
                spaces[loc.row()][loc.column()] = new Air(new Location(loc.row(), loc.column()), game);
                spaces[loc.row() + futureY][loc.column() + futureX] = new Player(image, new Location(loc.row() + futureY, loc.column() + futureX), game, direction);
            }
        }
        
        return spaces;
    }
    
    public boolean canMoveForward(Space[][] spaces, int x, int y)
    {
        int addX = 1;
        
        if (direction <= Constants.LEFTJUMP)
            addX = -1;
        
        if (x + addX > Constants.COLS - 1 || x + addX < 0)
            return false;
        else if (spaces[y][x + addX].getClassValue() == Constants.TILE || spaces[y][x + addX].getClassValue() == Constants.TRIANGLE)
            return false;
        
        if (y > 0)
        {
            if (spaces[y - 1][x + addX].getClassValue() == Constants.TILE || spaces[y][x + addX].getClassValue() == Constants.TRIANGLE)
                return false;
        }
        
        return true;
    }
    
    public boolean tileBelow(Space[][] spaces)
    {
        return (spaces[loc.row() + 1][loc.column()].getClassValue() == Constants.TILE || (spaces[loc.row() + 1][loc.column()].getClassValue() == Constants.TRIANGLE && ((Triangle) spaces[loc.row() + 1][loc.column()]).isDead()));
    }
    
    public boolean tileAbove(Space[][] spaces)
    {
        if (loc.row() <= 1)
            return true;
        
        if (spaces[loc.row() - 2][loc.column()].getClassValue() == Constants.TILE || spaces[loc.row() - 1][loc.column()].getClassValue() == Constants.TILE)
            return true;
        
        return false;
    }
    
    public boolean triangleAboveTile(Space[][] spaces)
    {
        if (loc.row() <= 2)
            return false;
        
        if (spaces[loc.row() - 3][loc.column()].getClassValue() == Constants.TRIANGLE)
            return true;
        
        return false;
    }
    
    public void kill()
    {
        if (!dead)
        {
            die.play();
            dead = true;
            if (lives <= 0)
                gameover = true;
            else
                lives -= 1;
        }
    }
    
    public boolean isGameOver()
    {
        return gameover;
    }
    
    public void setGameOver(boolean b)
    {
        gameover = b;
    }
    
    public int getLives()
    {
        return lives;
    }
    
    public void setLives(int Lives)
    {
        lives = Lives;
    }
    
    public boolean isDead()
    {
        return dead;
    }
    
    private void warp(Space[][] spaces, Location warpLoc)
    {
        if (!((Warp) spaces[warpLoc.row()][warpLoc.column()]).isEndWarp())
        {
            if (((Warp) spaces[warpLoc.row()][warpLoc.column()]).isTargetWarp())
            {
                for (int r = 0; r < Constants.ROWS; r ++)
                {
                    for (int c = 0; c < Constants.COLS; c ++)
                    {
                        if (spaces[r][c].getClassValue() == Constants.WARP && ((Warp) spaces[r][c]).isTargetWarp() && ((Warp) spaces[r][c]).getTarget() == ((Warp) spaces[warpLoc.row()][warpLoc.column()]).getTarget() && !spaces[r][c].getLocation().equals(warpLoc))
                        {
                            if (c < 19 && (spaces[r][c + 1].getClassValue() == Constants.AIR || spaces[r][c + 1].getClassValue() == Constants.COIN))
                            {
                                whoa.play();

                                moveLeft = false;
                                moveRight = false;
                                moveUp = false;

                                direction = Constants.RIGHT;

                                spaces[r][c + 1] = new Player(image, spaces[r][c + 1].getLocation(), game, direction);
                                spaces[loc.row()][loc.column()] = new Air(loc, game);
                                r = Constants.ROWS;
                                c = Constants.COLS;
                            }else if (c > 0 && (spaces[r][c - 1].getClassValue() == Constants.AIR || spaces[r][c - 1].getClassValue() == Constants.COIN))
                            {
                                whoa.play();

                                moveLeft = false;
                                moveRight = false;
                                moveUp = false;

                                direction = Constants.LEFT;

                                spaces[r][c - 1] = new Player(image, spaces[r][c - 1].getLocation(), game, direction);
                                spaces[loc.row()][loc.column()] = new Air(loc, game);
                                r = Constants.ROWS;
                                c = Constants.COLS;
                            }
                        }
                    }
                }
            }
            else
            {
                for (int r = 0; r < Constants.ROWS; r ++)
                {
                    for (int c = 0; c < Constants.COLS; c ++)
                    {
                        if (spaces[r][c].getClassValue() == Constants.WARP && !spaces[r][c].getLocation().equals(warpLoc) && !((Warp)spaces[r][c]).isEndWarp() && !((Warp) spaces[r][c]).isTargetWarp())
                        {
                            if (spaces[r][c].getLocation().column() < 5 && (spaces[r][c + 1].getClassValue() == Constants.AIR || spaces[r][c + 1].getClassValue() == Constants.COIN))
                            {
                                whoa.play();

                                moveLeft = false;
                                moveRight = false;
                                moveUp = false;

                                direction = Constants.RIGHT;

                                spaces[r][c + 1] = new Player(image, spaces[r][c + 1].getLocation(), game, direction);
                                spaces[loc.row()][loc.column()] = new Air(loc, game);
                                r = Constants.ROWS;
                                c = Constants.COLS;
                            }
                            else if (spaces[r][c].getLocation().column() >= 5 && (spaces[r][c - 1].getClassValue() == Constants.AIR || spaces[r][c - 1].getClassValue() == Constants.COIN))
                            {
                                whoa.play();

                                moveLeft = false;
                                moveRight = false;
                                moveUp = false;

                                direction = Constants.LEFT;

                                spaces[r][c - 1] = new Player(image, spaces[r][c - 1].getLocation(), game, direction);
                                spaces[loc.row()][loc.column()] = new Air(loc, game);
                                r = Constants.ROWS;
                                c = Constants.COLS;
                            }
                        }
                    }
                }
            }
        }
        else
            winLevel();
    }
    
    public int getScore()
    {
        return score;
    }
    
    public void setScore(int s)
    {
        score = s;
    }
    
    public int getPreviousScore()
    {
        return previousScore;
    }
    
    public void setPreviousScore(int prev_score)
    {
        previousScore = prev_score;
    }
    
    public void resetScore()
    {
        score = previousScore;
    }
    
    public void winLevel()
    {
        alright.play();
        winLevel = true;
    }
    
    public boolean checkWinLevel()
    {
        return winLevel;
    }
    
    public void setWinLevel(boolean b)
    {
        winLevel = b;
    }
    
    public void setMoveLeft(boolean b)
    {
        moveLeft = b;
    }
    
    public void setMoveRight(boolean b)
    {
        moveRight = b;
    }
    
    public void setMoveUp(boolean b)
    {
        moveUp = b;
    }
    
    public void setJumpUp(boolean b)
    {
        jumpUp = b;
    }
    
    public void setJumpDown(boolean b)
    {
        jumpDown = b;
        jumpCount = 0;
    }
    
    public boolean isJumping()
    {
        return jumpUp;
    }
    
    public boolean isFalling()
    {
        return jumpDown;
    }
    
    public Location getLocation()
    {
        return loc;
    }
    
    public void setLocation(Location Loc)
    {
        loc = Loc;
    }
    
    public Image getImage()
    {
        return image;
    }
    
    public String toString()
    {
        return "PLAYER: " + loc.toString();
    }
    
    public int getClassValue()
    {
        return CLASSVALUE;
    }
    
    public boolean skipNextIndex(Space[][] spaces)
    {
        return (((direction == 4 || direction == 5) && canMoveForward(spaces, loc.column(), loc.row())) || (direction == Constants.RIGHTJUMP && (jumpUp || jumpDown) && moveRight));
    }
    
    public boolean skipNextYIndex(Space[][] spaces)
    {
        if (skipNextY)
        {
            skipNextY = false;
            return true;
        }
        
        return false;
    }
}
