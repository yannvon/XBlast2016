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
    public final static int SUBDIVISION = 16;
    public final static int CENTRAL = 8;
    public final static int COLUMNS = Cell.COLUMNS * SUBDIVISION;
    public final static int ROWS = Cell.ROWS * SUBDIVISION;

    // Attributes
    private final int x, y;

    /**
     * Retrieves the central SubCell of any given Cell.
     * 
     * @param cell
     *            for which the SubCell has to be found
     * @return the SubCell that lays in the center of given Cell
     */
    public static SubCell centralSubCellOf(Cell cell) {
        // define the coordinates of the SubCell using the defined constants
        int x = cell.x() * SUBDIVISION + CENTRAL;
        int y = cell.y() * SUBDIVISION + CENTRAL;
        return new SubCell(x, y);
    }

    /**
     * Sole SubCell constructor. Accepts every integer as parameter, but the
     * coordinates are then normalized to fit inside the Game Board.
     * 
     * @param x
     *            x-coordinate
     * @param y
     *            y-coordinate
     */
    public SubCell(int x, int y) {
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }

    /**
     * Determines the Manhattan distance to closest central SubCell.
     * 
     * @return length of shortest Manhattan path to a central SubCell
     */
    public int distanceToCentral() {
        int manhattanDist = Math.abs(x % 16 - CENTRAL) + Math.abs(y % 16 - CENTRAL);
        return manhattanDist;
    }

    /**
     * Determine whether this SubCell is a central SubCell or not.
     * @return true if SubCell is central, false otherwise
     */
    public boolean isCentral() {
        return CENTRAL == x % SUBDIVISION && CENTRAL == y % SUBDIVISION;
    }

    /**
     * Returns the neighboring SubCell. Like for Cells, there always exists a
     * neighbor, due to the (conceptual) Torus shape of the Game Board.
     * 
     * @param d
     *            Direction of neighbor SubCell
     * @return new SubCell that is located in given Direction from this SubCell
     */
    public SubCell neighbor(Direction d) {
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
        throw new Error(); // will never happen
    }

    /**
     * Returns the Cell in which this SubCell is located
     * 
     * @return Cell in which SubCell is located
     */
    public Cell containingCell() {
        return new Cell(x / SUBDIVISION, y / SUBDIVISION);
    }

    @Override
    public boolean equals(Object that) {
        if (that == null) {
            return false;
        }
        if (this == that){
            return true;
        }
        if (this.getClass().equals(that.getClass())) {
            return this.x == ((SubCell) that).x() && this.y() == ((SubCell) that).y();
        }
        return false;
    }

    /**
     * Getter for the x-coordinate of the SubCell.
     * @return the x-coordinate
     */
    public int x() {
        return x;
    }
    
    /**
     * Getter for the y-coordinate of the SubCell.
     * @return the y-coordinate
     */
    public int y() {
        return y;
    }

    @Override
    public String toString() {
        return "(" + x + "," + y + ")";
    }

}
