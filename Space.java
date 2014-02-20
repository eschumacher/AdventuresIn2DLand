/*
 * Space.java
 *
 * Created on May 13, 2005, 8:37 AM
 */

package FinalProject;

import java.awt.Graphics;

/**
 *
 * @author  DHHS-AP
 */
public interface Space
{
    void draw(Graphics page);
    
    Space[][] move(Space[][] spaces);
    
    boolean skipNextIndex(Space[][] spaces);
    
    boolean skipNextYIndex(Space[][] spaces);
    
    boolean checkWinLevel();
    
    void setWinLevel(boolean b);
    
    Location getLocation();
    
    int getClassValue();
    
    String toString();
}
