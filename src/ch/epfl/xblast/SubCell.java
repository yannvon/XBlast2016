package ch.epfl.xblast;

/**
 * Represents a sub cell.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann (258857)
 *
 */
public final class SubCell {
    
    //attributes
    private final int x, y;
    //constants
    public final static int COLUMNS = 240, ROWS = 208, COUNT = ROWS * COLUMNS;
    public final static int SUBDIVISION = 16;
    /**
     * @param cell
     * @return 
     */
    public static SubCell centralSubCellOf(Cell cell){
        //define the coordinates of the subcell according to the given cell
        int x = cell.x()*16+7;
        int y = cell.y()*16+7;
        
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
        //TODO
        int manhattanDist=0;
        return manhattanDist;
    }
    
    public boolean isCentral(){
      //TODO
        return false;
    }
    
    public SubCell neighbor(Direction d){
        //TODO
        return null;
    }
    
    public Cell containingCell(){
        return new Cell(x/SUBDIVISION,y/SUBDIVISION);
    }
    
    
    

}
