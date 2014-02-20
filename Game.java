/*
 * Game.java
 *
 * Created on May 11, 2005, 8:53 AM
 */

//k kills
//boat level
//coins: keep old score before next level so you revert back if you die
//display score
//switches that you have to get
//hit bad guy, coin comes out
//gothic level...black background...white/glowy tiles
//ghosts

package FinalProject;

import javax.swing.*;
import java.awt.event.KeyListener;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.awt.image.ImageProducer;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.applet.AudioClip;

/**
 *
 * @author  Erik Schumacher
 */
public class Game extends JFrame implements KeyListener
{
    char[][] array;
    static Space[][] level;
    
    int currentLevel;
    
    int titleTextY1, titleTextX1, titleTextY2, titleTextX2;
    
    int mapLocation;
    int currentMapVersion;
    boolean mapChosen;
    
    boolean title;
    boolean titleLoaded;
    boolean loaded;
    boolean clearRect;
    boolean intro;
    boolean selection;
    boolean mapMode;
    boolean newGameSelected, loadGameSelected, exitSelected;
    
    String tileset;
    
    Image Title = getImageResource("Images\\BlankTitle.JPG");
    Image Creative = getImageResource("Images\\Creative.JPG");
    Image Initials = getImageResource("Images\\Initials.JPG");
    Image SelectionScreen = getImageResource("Images\\Selection.JPG");
    Image newGameBox = getImageResource("Images\\newGameBox.JPG");
    Image loadGameBox = getImageResource("Images\\loadGameBox.JPG");
    Image exitBox = getImageResource("Images\\exitBox.JPG");
    Image Map = getImageResource("Images\\Map1.JPG");
    Image mapMan = getImageResource("Images\\mapMan.PNG");
    
    
    AudioClip Music, Beep, Selected, celebration;
    
    MediaTracker mediaTracker;
    
    Constants c = new Constants();
    
    HeadBar headBar;
    
    /** Creates a new instance of Game */
    public Game()
    {
        super("Adventures in 2D Land");
        addKeyListener(this);
        setSize(1024, 740);
        setVisible(true);
        setBackground(Color.white);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        currentLevel = -1;
        tileset = "Castle";
        
        titleTextY1 = 160;
        titleTextX1 = 1000;
        titleTextY2 = 593;
        titleTextX2 = 10;
        
        mapLocation = Constants.MAP_CASTLE;
        currentMapVersion = Constants.MAP_CASTLE;
        mapChosen = false;
        
        title = false;
        titleLoaded = false;
        loaded = false;
        clearRect = false;
        intro = true;
        selection = false;
        mapMode = false;
        newGameSelected = true;
        loadGameSelected = false;
        exitSelected = false;
        
        headBar = new HeadBar(Constants.HEADBARX, Constants.HEADBARY, 0, 5);
        
        try{
            level = readLevel(Constants.PATH + "Levels\\" + currentLevel + ".lvl");
        }catch(IOException ie){System.out.println(ie.toString());}
        
        if (mediaTracker == null)
        {
            mediaTracker = new MediaTracker(this);
            mediaTracker.addImage(Title, 0);
            try{mediaTracker.waitForID(0);}
            catch (InterruptedException ie){System.err.println(ie);}
            mediaTracker.addImage(Creative, 1);
            try{mediaTracker.waitForID(1);}
            catch (InterruptedException ie){System.err.println(ie);}
            mediaTracker.addImage(SelectionScreen, 2);
            try{mediaTracker.waitForID(2);}
            catch (InterruptedException ie){System.err.println(ie);}
            mediaTracker.addImage(Initials, 3);
            try{mediaTracker.waitForID(3);}
            catch (InterruptedException ie){System.err.println(ie);}
            mediaTracker.addImage(newGameBox, 4);
            try{mediaTracker.waitForID(4);}
            catch (InterruptedException ie){System.err.println(ie);}
            mediaTracker.addImage(loadGameBox, 5);
            try{mediaTracker.waitForID(5);}
            catch (InterruptedException ie){System.err.println(ie);}
            mediaTracker.addImage(exitBox, 6);
            try{mediaTracker.waitForID(6);}
            catch (InterruptedException ie){System.err.println(ie);}
            mediaTracker.addImage(Map, 7);
            try{mediaTracker.waitForID(7);}
            catch (InterruptedException ie){System.err.println(ie);}
            mediaTracker.addImage(mapMan, 8);
            try{mediaTracker.waitForID(8);}
            catch (InterruptedException ie){System.err.println(ie);}
            
            loaded = true;
        }
        
        /*try
        {
            Music = JApplet.newAudioClip(this.getClass().getClassLoader().getResource("Music\\AdventuresIn2DLand.wav"));
        }catch (Exception e){System.out.println(e.toString());}*/
        
        try{
            Music = JApplet.newAudioClip(new File(Constants.PATH + "Music\\AdventuresIn2DLandFull.wav").toURL());
        }catch(Exception e){System.out.println(e.toString());}
        
        try{
            Beep = JApplet.newAudioClip(new File(Constants.PATH + "Music\\beep.wav").toURL());
        }catch(Exception e){System.out.println(e.toString());}
        
        try{
            Selected = JApplet.newAudioClip(new File(Constants.PATH + "Music\\selected.wav").toURL());
        }catch(Exception e){System.out.println(e.toString());}
        
        try{
            celebration = JApplet.newAudioClip(new File(Constants.PATH + "Music\\celebration.wav").toURL());
        }catch(Exception e){System.out.println(e.toString());}
        
        Music.loop();
        
        repaint();
    }
    
    public void paint(Graphics page)
    {
        if (clearRect)
        {
            if (selection)
                page.fillRect(0, 0, 1024, 740);
            else
                page.clearRect(0, 0, 1024, 740);
            
            clearRect = false;
        }
        
        if (loaded)
        {
            if (intro)
                introSequence(page);
            else if (title)
                drawTitle(page);
            else if (selection)
                drawSelectionScreen(page);
            else if (mapMode)
                drawMapMode(page);
            else
            {
                updateLevel(page);
                if (!mapMode)
                {
                    headBar.clear(page);
                    headBar.draw(page);
                    drawLevel(page);
                }
                
                try{
                    Thread.sleep(Constants.PAUSE);
                }catch(InterruptedException ie){ie.printStackTrace();}
                
                repaint();
            }
        }
    }
    
    public void updateLevel(Graphics page)
    {
        boolean skipNext = false;
        boolean skipNextY = false;
        for (int r = 0; r < Constants.ROWS; r ++)
        {
            for (int c = 0; c < Constants.COLS; c ++)
            {
                if (level[r][c].getClassValue() == Constants.PLAYER && !skipNextY)
                {
                    skipNextY = level[r][c].skipNextYIndex(level);
                    
                    if (((Player) level[r][c]).isDead())
                    {
                        headBar.setLives(((Player) level[r][c]).getLives());
                        
                        if (((Player) level[r][c]).isGameOver())
                            gameOver(page);
                        else
                            dead(page);
                    }
                    else if (level[r][c].checkWinLevel())
                    {
                        level[r][c].setWinLevel(false);
                        if (currentLevel == 9)
                        {
                            Music.stop();                            
                            try{
                                Music = JApplet.newAudioClip(new File(Constants.PATH + "Music\\piano.wav").toURL());
                            }catch(Exception ex){System.out.println(ex.toString());}
                            Music.loop();
                            
                            drawMapMode(page);
                            mapMode = true;
                        }
                        else
                            WinLevel(page);
                    }
                    else if (!skipNextY)
                    {
                        headBar.setCoins(((Player) level[r][c]).getScore());
                        //setLives??
                        skipNext = level[r][c].skipNextIndex(level);
                        level = level[r][c].move(level);
                    }
                }
                else
                {
                    skipNext = level[r][c].skipNextIndex(level);

                    level = level[r][c].move(level);
                    
                    if (level[r][c].getClassValue() == Constants.TRIANGLE && ((Triangle) level[r][c]).isPlayerKilled())
                        dead(page);
                }

                if (skipNext)
                {
                    c ++;
                    skipNext = false;
                }
            }
        }
    }
    
    public void drawLevel(Graphics page)
    {        
        for (int r = 0; r < Constants.ROWS; r ++)
        {
            for (int c = 0; c < Constants.COLS; c ++)
            {
                if (r < Constants.ROWS - 1 && level[r + 1][c].getClassValue() == Constants.PLAYER && level[r][c].getClassValue() == Constants.AIR)
                {/*do nothing*/}
                else
                    level[r][c].draw(page);
            }
        }
    }
    
    public void WinLevel(Graphics page)
    {
        currentLevel += 1;
        
        if (currentLevel == 10)
        {
            c.setRows(34);
            c.setCols(40);
            c.setPause(60);
        }
        page.setColor(Color.black);
        page.fillRect(0, 0, 1024, 740);
        page.setColor(Color.white);
        page.setFont(new Font("BankGothic Md BT", Font.PLAIN, 48));
        page.drawString("Loading...", 400, 390);
        
        try{
            level = readLevel(Constants.PATH + "Levels\\" + currentLevel + ".lvl");
        }catch(IOException ie){System.out.println(ie.toString());}
        
        for (int r = 0; r < Constants.ROWS; r ++)
        {
            for (int c = 0; c < Constants.COLS; c ++)
            {
                if (level[r][c].getClassValue() == Constants.PLAYER)
                    ((Player) level[r][c]).setPreviousScore(((Player) level[r][c]).getScore());
            }
        }
        
        page.clearRect(0, 0, 1024, 740);

        page.setColor(Color.black);
        page.fillRect(0, 0, Constants.COLS * Constants.FACTOR, Constants.ROWS * Constants.FACTOR + Constants.ADDY);
        
        drawLevel(page);

        for (int n = 3; n > 0; n --)
        {
            for (int s = 250; s > 0; s -= 5)
            {
                page.clearRect(350, 270, 200, 176);
                 page.setFont(new Font("BankGothic Md BT", Font.PLAIN, s));
                page.drawString("" + n, 350, 440);

                try{
                    Thread.sleep(10);
                }catch(InterruptedException ie){ie.printStackTrace();}
            }

            page.clearRect(350, 270, 200, 176);

            try{
                Thread.sleep(200);
            }catch(InterruptedException ie){ie.printStackTrace();}
        }
        
        clearRect = true;
    }
    
    public void dead(Graphics page)
    {        
        page.setFont(new Font("BankGothic Md BT", Font.PLAIN, 48));
        page.setColor(Color.black);
        page.fillRect(0, 0, 1024, 740);
        page.setColor(Color.white);
        page.drawString("Loading...", 400, 390);
                
        try{
            level = readLevel(Constants.PATH + "Levels\\" + currentLevel + ".lvl");
        }catch(IOException ie){System.out.println(ie.toString());}
        
        for (int r = 0; r < Constants.ROWS; r ++)
        {
            for (int c = 0; c < Constants.COLS; c ++)
            {
                if (level[r][c].getClassValue() == Constants.PLAYER)
                    ((Player) level[r][c]).setScore(((Player) level[r][c]).getPreviousScore());
            }
        }
        
        page.setColor(Color.black);
        page.fillRect(0, 0, 1024, 740);
        page.setColor(Color.white);
        page.drawString("You died", 400, 370);
        page.drawString("Respawn in", 340, 440);
        
        for (int r = 3; r > 0; r --)
        {
            page.setColor(Color.black);
            page.fillRect(660, 380, 60, 60);
            page.setColor(Color.white);
            page.drawString("" + r, 660, 440);
            
            try{
                Thread.sleep(960);
            }catch(InterruptedException ie){ie.printStackTrace();}
        }
        
        clearRect = true;
    }
    
    public void drawMapMode(Graphics page)
    {
        if (mapChosen)
        {
            mapChosen = false;
            mapMode = false;
            WinLevel(page);
        }
        else
        {
            if (currentLevel == 9 && currentMapVersion != Constants.MAP_FOREST)
            {
                celebration.play();
                currentMapVersion = Constants.MAP_FOREST;
                Map = getImageResource("Images\\Map2.JPG");
            }
            
            page.setColor(Color.black);
            page.fillRect(0, 0, 1024, 740);
            page.setColor(Color.white);
            page.setFont(new Font("BankGothic Md BT", Font.PLAIN, 48));
            
            page.drawImage(Map, 117, 100, this);
            if (mapLocation == Constants.MAP_CASTLE)
            {
                page.drawImage(mapMan, Constants.MAP_CASTLEX, Constants.MAP_CASTLEY, this);
                page.drawString("Escape Castle", 310, 80);
            }
            else if (mapLocation == Constants.MAP_FOREST)
            {
                page.drawImage(mapMan, Constants.MAP_FORESTX, Constants.MAP_FORESTY, this);
                page.drawString("Enter Forest", 320, 80);
            }
        }
    }
    
    public void gameOver(Graphics page)
    {
        page.setColor(Color.black);
        page.fillRect(0, 0, 1024, 740);
        page.setColor(Color.white);
        
        page.setFont(new Font("BankGothic Md BT", Font.PLAIN, 48));
        page.drawString("GAME OVER", 400, 400);
        
        try{
            Thread.sleep(2000);
        }
        catch(InterruptedException ie){
            ie.printStackTrace();
        }
        
        new Game();
    }
    
    public void introSequence(Graphics page)
    {
        BufferedImage buffer = null, black = null;
        float alpha_value = 0;
        java.awt.Graphics2D offScreen = null;
        double a_value = .05, a_value2 = .13; //.05 / .13 on home computer
        
        for (float a = 1; a > 0; a -= a_value)
        {
            alpha_value = a;

            if (buffer == null)
            {
                buffer = new BufferedImage(1024, 740, BufferedImage.TYPE_INT_ARGB);
                offScreen = buffer.createGraphics();
                black = new BufferedImage(1024, 740, BufferedImage.TYPE_INT_RGB);
                java.awt.Graphics2D graph = black.createGraphics();
                graph.setColor(Color.black);
                graph.fillRect(0, 0, 1024, 740);
            }

            offScreen.drawImage(Creative, 117, 100, this);
            offScreen.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha_value));
            offScreen.drawImage(black, 0, 0,this);
            page.drawImage(buffer, 0, 0, this);
        }

        try{
            Thread.sleep(800);
        }
        catch(InterruptedException ie){
            ie.printStackTrace();
        }

        for (float a = 0; a < 1.0; a += a_value)
        {
            alpha_value = a;

            offScreen.drawImage(Creative, 117, 100, this);
            offScreen.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha_value));
            offScreen.drawImage(black, 0, 0, this);
            page.drawImage(buffer, 0, 0, this);
        }
        page.fillRect(0, 0, 1024, 740);
        
        try{
            Thread.sleep(200);
        }
        catch(InterruptedException ie){
            ie.printStackTrace();
        }
        
        for (float a = 1; a > 0; a -= a_value) //.05 on home computer
        {
            alpha_value = a;

            offScreen.drawImage(Initials, 117, 100, this);
            offScreen.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha_value));
            offScreen.drawImage(black, 0, 0,this);
            page.drawImage(buffer, 0, 0, this);
        }

        try{
            Thread.sleep(600);
        }
        catch(InterruptedException ie){
            ie.printStackTrace();
        }

        for (float a = 0; a < 1.0; a += a_value)
        {
            alpha_value = a;

            offScreen.drawImage(Initials, 117, 100, this);
            offScreen.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha_value));
            offScreen.drawImage(black, 0, 0, this);
            page.drawImage(buffer, 0, 0, this);
        }
        page.fillRect(0, 0, 1024, 740);
        
        try{
            Thread.sleep(10);
        }
        catch(InterruptedException ie){
            ie.printStackTrace();
        }
        
        for (float a = 1; a > 0; a -= a_value2)
        {
            alpha_value = a;

            offScreen.drawImage(Title, 0, 0, this);
            offScreen.setComposite(java.awt.AlphaComposite.getInstance(java.awt.AlphaComposite.SRC_OVER, alpha_value));
            offScreen.drawImage(black, 0, 0, this);
            page.drawImage(buffer, 0, 0, this);
        }
        
        page.clearRect(0, 0, 1024, 740);
        drawTitle(page);
        
        intro = false;
        title = true;
    }
    
    public void drawSelectionScreen(Graphics page)
    {
        page.drawImage(SelectionScreen, 117, 100, this);
        page.setFont(new Font("Courier New", Font.BOLD, 28));

        if (newGameSelected)
        {
            page.setColor(Color.blue);
            page.drawString("New Game", 288, 264);
            page.setColor(Color.black);
            page.drawString("Load Game", 280, 364);
            page.drawString("Exit Game", 280, 464);

            page.drawLine(290, 265, 560, 265);
            page.drawImage(newGameBox, 560, 184, this);
        }
        else if (loadGameSelected)
        {
            page.drawString("New Game", 288, 264);
            page.setColor(Color.blue);
            page.drawString("Load Game", 280, 364);
            page.setColor(Color.black);
            page.drawString("Exit Game", 280, 464);

            page.drawLine(282, 365, 560, 365);
            page.drawImage(loadGameBox, 560, 284, this);
        }
        else if (exitSelected)
        {
            page.drawString("New Game", 288, 264);
            page.drawString("Load Game", 280, 364);
            page.setColor(Color.blue);
            page.drawString("Exit Game", 280, 464);
            page.setColor(Color.black);

            page.drawLine(282, 465, 560, 465);
            page.drawImage(exitBox, 560, 354, this);
        }
    }
    
    public void drawTitle(Graphics page)
    {
        page.drawImage(Title, 0, 0, this);
        
        page.setFont(new Font("BernhardMod BT", Font.PLAIN, 48));
        page.drawString("ADVENTURES IN", titleTextX1, titleTextY1);
        
        page.setFont(new Font("BankGothic Md BT", Font.PLAIN, 48));
        page.drawString("2D LAND", titleTextX2, titleTextY2);
        
        if (!titleLoaded)
        {
            titleTextX1 -= 20;
            if (titleTextX1 <= 280)
                titleLoaded = true;
            
            if (titleTextX2 < 370)
                titleTextX2 += 10;
            
            repaint();
        }
        else
        {
            page.setFont(new Font("BankGothic Md BT", Font.PLAIN, 36));
            page.drawString("-Press Enter-", 355, 653);
        }
    }
        
    public void keyPressed(java.awt.event.KeyEvent e)
    {
        if (title)
        {
            if (e.getKeyCode() == 10)
            {
                Selected.play();
                
                title = false;
                clearRect = true;
                selection = true;
                repaint();
            }
        }
        else if (selection)
        {
            if (newGameSelected)
            {
                if (e.getKeyCode() == 10)
                {
                    Music.stop();
                    Selected.play();
                    try{
                        Music = JApplet.newAudioClip(new File(Constants.PATH + "Music\\piano.wav").toURL());
                    }catch(Exception ex){System.out.println(ex.toString());}
                    Music.loop();
                    
                    selection = false;
                    mapMode = true;
                    clearRect = true;
                    repaint();
                }
                else if (e.getKeyCode() == 38)
                {
                    Beep.play();
                    
                    newGameSelected = false;
                    exitSelected = true;
                    repaint();
                }
                else if (e.getKeyCode() == 40)
                {
                    Beep.play();
                    
                    newGameSelected = false;
                    loadGameSelected = true;
                    repaint();
                }
            }
            else if (loadGameSelected)
            {
                if (e.getKeyCode() == 38)
                {
                    Beep.play();
                    
                    loadGameSelected = false;
                    newGameSelected = true;
                    repaint();
                }
                else if (e.getKeyCode() == 40)
                {
                    Beep.play();
                    
                    loadGameSelected = false;
                    exitSelected = true;
                    repaint();
                }
            }
            else if (exitSelected)
            {
                if (e.getKeyCode() == 10)
                    System.exit(0);
                else if (e.getKeyCode() == 38)
                {
                    Beep.play();
                    
                    exitSelected = false;
                    loadGameSelected = true;
                    repaint();
                }
                else if (e.getKeyCode() == 40)
                {
                    Beep.play();
                    
                    exitSelected = false;
                    newGameSelected = true;
                    repaint();
                }
            }
        }
        else if (mapMode)
        {
            if (e.getKeyCode() == 37 || e.getKeyCode() == 40)
            {
                if (mapLocation == Constants.MAP_CASTLE && currentLevel >= 9)
                {
                    mapLocation = Constants.MAP_FOREST;
                    repaint();
                }
            }
            else if (e.getKeyCode() == 38 || e.getKeyCode() == 39)
            {
                if (mapLocation == Constants.MAP_FOREST)
                {
                    mapLocation = Constants.MAP_CASTLE;
                    repaint();
                }
            }
            else if (e.getKeyCode() == 10)
            {
                if (mapLocation == Constants.MAP_CASTLE)
                {
                    tileset = "Castle";
                    currentLevel = -1; //-1 for when you add1 in winlevel
                    Music.stop();
                    Selected.play();
                    
                    try{
                        Music = JApplet.newAudioClip(new File(Constants.PATH + "Music\\evil.wav").toURL());
                    }catch(Exception ex){System.out.println(ex.toString());}
                    
                    Music.loop();
                }
                else if (mapLocation == Constants.MAP_FOREST)
                {
                    currentLevel = 9; //-1 for when you add1 in winlevel
                    tileset = "Forest";
                    Music.stop();
                    Selected.play();
                    
                    try{
                        Music = JApplet.newAudioClip(new File(Constants.PATH + "Music\\ghostlike.wav").toURL());
                    }catch(Exception ex){System.out.println(ex.toString());}
                    
                    Music.loop();
                }
                
                mapChosen = true;
                
                repaint();
            }
                
        }
        else
        {
            if (e.getKeyCode() == 75)
            {
                for (int r = 0; r < Constants.ROWS; r ++)
                {
                    for (int c = 0; c < Constants.COLS; c ++)
                    {
                        if (level[r][c].getClassValue() == Constants.PLAYER)
                            ((Player) level[r][c]).kill();
                    }
                }
            }
            
            if (e.getKeyCode() == 37)
            {
                for (int r = 0; r < Constants.ROWS; r ++)
                {
                    for (int c = 0; c < Constants.COLS; c ++)
                    {
                        if (level[r][c].getClassValue() == Constants.PLAYER)
                            ((Player) level[r][c]).setMoveLeft(true);
                    }
                }
            }
            
            if (e.getKeyCode() == 39)
            {
                for (int r = 0; r < Constants.ROWS; r ++)
                {
                    for (int c = 0; c < Constants.COLS; c ++)
                    {
                        if (level[r][c].getClassValue() == Constants.PLAYER)
                            ((Player) level[r][c]).setMoveRight(true);
                    }
                }
            }
            
            if (e.getKeyCode() == 38)
            {
                for (int r = 0; r < Constants.ROWS; r ++)
                {
                    for (int c = 0; c < Constants.COLS; c ++)
                    {
                        if (level[r][c].getClassValue() == Constants.PLAYER)
                            ((Player) level[r][c]).setMoveUp(true);
                    }
                }
            }
        }
    }
    
    public void keyReleased(java.awt.event.KeyEvent e)
    {
        if (e.getKeyCode() == 38)
        {
            for (int r = 0; r < Constants.ROWS; r ++)
            {
                for (int c = 0; c < Constants.COLS; c ++)
                {
                    if (level[r][c].getClassValue() == Constants.PLAYER)
                    {
                        ((Player) level[r][c]).setMoveUp(false);
                        ((Player) level[r][c]).setJumpDown(true);
                        ((Player) level[r][c]).setJumpUp(false);
                    }
                }
            }
        }
        
        if (e.getKeyCode() == 37)
        {
            for (int r = 0; r < Constants.ROWS; r ++)
            {
                for (int c = 0; c < Constants.COLS; c ++)
                {
                    if (level[r][c].getClassValue() == Constants.PLAYER)
                        ((Player) level[r][c]).setMoveLeft(false);
                }
            }
        }
        
        if (e.getKeyCode() == 39)
        {
            for (int r = 0; r < Constants.ROWS; r ++)
            {
                for (int c = 0; c < Constants.COLS; c ++)
                {
                    if (level[r][c].getClassValue() == Constants.PLAYER)
                        ((Player) level[r][c]).setMoveRight(false);
                }
            }
        }
    }
    
    public void keyTyped(java.awt.event.KeyEvent e){}
    
    public static void main(String[] args)
    {
        new Game();
    }
    
    
    
    /*------------------------------------------------------------------------
     * returns array of all spaces in the level
     *----------------------------------------------------------------------*/
    private Space[][] readLevel(String level) throws IOException
    {
        BufferedReader in = new BufferedReader(new FileReader(new File(level)));
        array = new char[Constants.ROWS][];

        String buffer;
        int line = 0;
        while ((buffer = in.readLine()) != null)
            array[line++] = buffer.toCharArray();

        in.close();
        
        Space[][] spaces = new Space[Constants.ROWS][Constants.COLS];
        
        for (int r = 0; r < Constants.ROWS; r ++)
        {
            for (int c = 0; c < Constants.COLS; c ++)
            {
                System.out.print(array[r][c]);                
                
                if (array[r][c] == 'o')
                    spaces[r][c] = new Air(new Location(r, c), this);
                else if (array[r][c] == 'x')
                    spaces[r][c] = new Tile(getImageResource("Images\\" + tileset + "\\Tile.JPG"), new Location(r, c), this);
                else if (array[r][c] == 'c')
                    spaces[r][c] = new Coin(getImageResource("Images\\Coin.JPG"), new Location(r, c), this);
                else if (array[r][c] == 'w')
                    spaces[r][c] = new Warp(getImageResource("Images\\Warp.JPG"), false, new Location(r, c), this);
                else if (array[r][c] == 't')
                    spaces[r][c] = new Triangle(getImageResource("Images\\Triangles.JPG"), new Location(r, c), this);
                else if (array[r][c] == 'r')
                    spaces[r][c] = new Triangle(getImageResource("Images\\Rock.JPG"), new Location(r, c), this);
                else if (array[r][c] == 'f')
                    spaces[r][c] = new Warp(getImageResource("Images\\FinalWarp.JPG"), true, new Location(r, c), this);
                else if (array[r][c] == 'p')
                    spaces[r][c] = new Player(getImageResource("Images\\Player.JPG"), new Location(r, c), this);
                else if (array[r][c] == '1' || array[r][c] == '2' || array[r][c] == '3' || array[r][c] == '4' || array[r][c] == '5')
                    spaces[r][c] = new Warp(getImageResource("Images\\Warp.JPG"), false, new Location(r, c), this, Integer.parseInt("" + array[r][c]));
                else if (array[r][c] == 'T')
                    spaces[r][c] = new Tree(getImageResource("Images\\" + tileset + "\\Tree.JPG"), new Location(r, c), this);
            }
            System.out.println("");
        }
        
        return spaces;
    }
   
    public static Image getImageResource(String name) //throws java.io.IOException 
    {
        return getImageResource(Game.class, name);
    }
   
    public static Image getImageResource(Class base, String name ) //throws java.io.IOException 
    {
      if (name != null) {
         Image result = null;

         // A must, else won't pick the path properly within the jar!!
         // For file system forward or backward slash is not an issue
         name = name.replace('\\', '/');


         // For loading from file system using getResource via base class
         URL imageURL = base.getResource( name );


         // For loading from jar starting from root path using getResource via base class
         if (imageURL == null) {
            imageURL = base.getResource("/" + name );
         }


         // For loading from file system using getSystemResource
         if (imageURL == null) {
            imageURL = ClassLoader.getSystemResource( name );
         }


         // For loading from jar starting from root path using getSystemResource
         if (imageURL == null) {
            imageURL = ClassLoader.getSystemResource("/" + name );
         }


         // Found the image url? If so, create the image and return it...
         if ( imageURL != null ) {
            Toolkit tk = Toolkit.getDefaultToolkit();
            try{
            result = tk.createImage( (ImageProducer) imageURL.getContent() );
            }catch(IOException i){System.out.println(i);}
         }

         return result;

      }
      else {

         return null;
      }
    }
}
