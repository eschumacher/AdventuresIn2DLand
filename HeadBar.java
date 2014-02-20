/*
 * HeadBar.java
 *
 * Created on June 7, 2005, 4:59 PM
 */

package FinalProject;

import java.awt.*;

/**
 *
 * @author  Erik
 */
public class HeadBar
{
    private int x, y, coins, lives;
    private Font f;
    
    /** Creates a new instance of HeadBar */
    public HeadBar(int X, int Y, int Coins, int Lives)
    {
        x = X;
        y = Y;
        coins = Coins;
        lives = Lives;
        f = new Font("BankGothic Md BT", Font.PLAIN, 22);
    }
    
    public void draw(Graphics page)
    {
        page.setFont(f);
        page.drawString("Lives: " + lives, x, y);
        page.drawString("Coins: " + coins, Constants.COLS * 20 - 115, y);
    }
    
    public void clear(Graphics page)
    {
        page.clearRect(80, 0, 180, 52);
        page.clearRect(Constants.COLS * 20 - 60, 0, 300, 52);
    }
    
    public void setCoins(int Coins)
    {
        coins = Coins;
    }
    
    public void setLives(int Lives)
    {
        lives = Lives;
    }
}
