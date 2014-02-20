/*
 * Location.java
 *
 * Created on May 12, 2005, 4:57 PM
 */

package FinalProject;

/**
 *
 * @author  Erik Schumacher
 */
public class Location implements java.lang.Comparable
{
    private int row, col;
    
    /** Creates a new instance of Location */
    public Location(int Row, int Column)
    {
        row = Row;
        col = Column;
    }
    
    public int row()
    {
        return row;
    }
    
    public int column()
    {
        return col;
    }
    
    public boolean equals(Location loc)
    {
        return (loc.row() == row && loc.column() == col);
    }
    
    public int compareTo(Object o)
    {
        if (row < ((Location) o).row())
            return -1;
        else if (row == ((Location) o).row())
        {
            if (col < ((Location) o).column())
                return -1;
            else if (col > ((Location) o).column())
                return 1;
            
            return 0;
        }
        
        return 1;
    }
    
    public String toString()
    {
        return "(" + row + ", " + col + ")";
    }
}
