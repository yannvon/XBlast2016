package ch.epfl.xblast;

/**
 * An immutable subdivision of Cell.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class SubCell {

    // Constants related to the devision of the Game Board into Sub Cells
    private static final int SUBDIVISION = 16;
    private static final int CENTRAL = SUBDIVISION / 2;
    private static final int SUBCOLUMNS = Cell.COLUMNS * SUBDIVISION;
    private static final int SUBROWS = Cell.ROWS * SUBDIVISION;

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
        this.x = Math.floorMod(x, SUBCOLUMNS);
        this.y = Math.floorMod(y, SUBROWS);
    }

    /**
     * Determines the Manhattan distance to closest central SubCell.
     * 
     * @return length of shortest Manhattan path to a central SubCell
     */
    public int distanceToCentral() {
        return Math.abs(x() % SUBDIVISION - CENTRAL) + Math.abs(y() % SUBDIVISION - CENTRAL);
    }

    /**
     * Determine whether this SubCell is a central SubCell or not.
     * @return true if SubCell is central, false otherwise
     */
    public boolean isCentral() {
        return distanceToCentral() == 0;
    }

    /**
     * Returns the neighboring SubCell. Like for Cells, there always exists a
     * neighbor, due to the (conceptual) Torus shape of the game board.
     * 
     * @param d
     *            Direction of neighbor SubCell
     * @return new SubCell that is located in given Direction from this SubCell
     * @throws Error
     *             if the argument is not one of the 4 direction
     */
    public SubCell neighbor(Direction d) {
        switch (d) {
        case N:
            return new SubCell(x(), y() - 1);
        case S:
            return new SubCell(x(), y() + 1);
        case W:
            return new SubCell(x() - 1, y());
        case E:
            return new SubCell(x() + 1, y());
        }
        throw new Error(); // will never happen
    }

    /**
     * Returns the Cell in which this SubCell is located
     * 
     * @return Cell in which SubCell is located
     */
    public Cell containingCell() {
        return new Cell(x() / SUBDIVISION, y() / SUBDIVISION);
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
    /**
     * Compares an Object to a SubCell.
     * 
     * @return true if given Object is equal to this SubCell, false otherwise
     */
    public boolean equals(Object that) {
        return (that instanceof SubCell && x() == ((SubCell) that).x()
                && y() == ((SubCell) that).y());
//        if (that == null) {                   //FIXME
//            return false;
//        }
//        if (this == that){
//            return true;
//        }
//        if (getClass() == that.getClass()) {
//            return x == ((SubCell) that).x()
//                    && y == ((SubCell) that).y();
//        }
//        return false;
    }

    @Override
    /**
     * Defines how to represent a SubCell.
     * 
     * @return a String representation of the SubCell.
     */
    public String toString() {
        return "(" + x() + "," + y() + ")";
    }
    
    @Override
    /**
     * Returns the hash value of the SubCell.
     * 
     * @return integer hash value
     */
    public int hashCode(){
        return x() + y() * SUBCOLUMNS;
    }
}
