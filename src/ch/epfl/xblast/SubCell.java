package ch.epfl.xblast;

/**
 * An immutable sub cell.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class SubCell {
    
    // Constants related to the devision of the Game Board into Sub Cells
    public final static int COLUMNS = 240;
    public final static int ROWS = 208;
    public final static int COUNT = ROWS * COLUMNS;
    public final static int SUBDIVISION = 16;
    public final static int CENTRAL = 8;
    
    // Attributes
    private final int x, y;
    
    /**
     * @param cell
     * @return 
     */
    public static SubCell centralSubCellOf(Cell cell){
        //define the coordinates of the SubCell according to the given cell
        int x = cell.x()*SUBDIVISION+CENTRAL;
        int y = cell.y()*SUBDIVISION+CENTRAL;
        return new SubCell(x,y);
    }
    
    /**
     * Constructs a subcell with given x and y coordinates.
     * 
     * @param x coordinate
     * @param y coordinate
     */
    public SubCell(int x, int y) {
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }
    
    public int distanceToCentral(){
        int manhattanDist= Math.abs( x%16 - CENTRAL ) + Math.abs( y%16 - CENTRAL );
        return manhattanDist;
    }
    
    public boolean isCentral(){
      
        return CENTRAL==x%SUBDIVISION && CENTRAL==y%SUBDIVISION;
    }
    
    public SubCell neighbor(Direction d){
        switch (d) {
        case N:
            return new SubCell(x, y - 1);
        case S:
            return new SubCell(x, y + 1);
        case W:
            return new SubCell(x - 1, y);
        case E:
            return new SubCell(x + 1, y);
        }
        throw new Error(); //shouldn't happen
    }
    
    public Cell containingCell(){
        return new Cell(x/SUBDIVISION,y/SUBDIVISION);
    }
    
    public boolean equals(Object that){
        if (that == null) {
            return false;
        }
        if (this.getClass().equals(that.getClass())) {
            return this.x == ((SubCell) that).x() && this.y() == ((SubCell) that).y();
        }
        return false;
    }
    
    public int y(){
        return y;
    }
    
    public int x(){
        return x;
    }
    
    public String toString() {
        return "(" + x + "," + y + ")";
    }
    

}
