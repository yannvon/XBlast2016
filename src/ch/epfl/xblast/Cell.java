package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An immutable Cell.
 *  
 * @author Loic Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public final class Cell {

    // Constants defining the Game Board size
    public static final int COLUMNS = 15;
    public static final int ROWS = 13;
    public static final int COUNT = ROWS * COLUMNS;
    
    // Unmodifiable arrays containing all Cells of the Game Board
    public final static List<Cell> ROW_MAJOR_ORDER = 
            Collections.unmodifiableList(rowMajorOrder());
    public final static List<Cell> SPIRAL_ORDER = 
            Collections.unmodifiableList(spiralOrder());

    // Attributes
    private final int x, y;
    
    /**
     * Sole Cell constructor.
     * Accepts every integer as parameter, but the coordinates are then
     * normalized to fit inside the Game Board.
     * 
     * @param x
     *            x-coordinate
     * @param y
     *            y-coordinate
     */
    public Cell(int x, int y) {
        this.x = Math.floorMod(x, COLUMNS);
        this.y = Math.floorMod(y, ROWS);
    }
    
    /**
     * Getter for the x-coordinate of the Cell.
     * 
     * @return the x-coordinate
     */
    public int x() {
        return x;
    }

    /**
     * Getter for the y-coordinate of the Cell.
     * 
     * @return the y-coordinate
     */
    public int y() {
        return y;
    }
    
    /**
     * Returns the Index of the Cell following the reading order.
     * 
     * @return reading order index of Cell
     */
    public int rowMajorIndex() {
        return x() + y() * COLUMNS;
    }
    
    /**
     * Returns the neighboring cell in given Direction. Since the game board is
     * considered a torus, this Cell always exists.
     * 
     * @param dir
     *            direction of the neighbor
     * @return the neighboring Cell in given Direction
     */
    public Cell neighbor(Direction dir) {
        switch (dir) {
        case N:
            return new Cell(x(), y() - 1);
        case S:
            return new Cell(x(), y() + 1);
        case W:
            return new Cell(x() - 1, y());
        case E:
            return new Cell(x() + 1, y());
        }
        throw new Error(); // will never happen
    }
    
    /**
     * Compares an Object to a Cell.
     * 
     * @return true if the Object is equal to this Cell, false otherwise
     */
    @Override
    public boolean equals(Object that) {
        return (that instanceof Cell
                && (this.rowMajorIndex() == ((Cell) that).rowMajorIndex()));
    }
    
    /**
     * Defines how to print a Cell.
     * 
     * @return a String representation of the Cell.
     */
    @Override
    public String toString() {
        return "(" + x() + "," + y() + ")";
    }
    
    /**
     * Returns the hash value of the Cell.
     * 
     * @return integer hash value
     */
    @Override
    public int hashCode(){
       return rowMajorIndex(); 
    }

    /**
     * Constructs an array containing all Cells of the Game Board following the
     * reading order.
     * 
     * @return ordered array of Cells
     */
    private static ArrayList<Cell> rowMajorOrder() {
        ArrayList<Cell> readingOrder = new ArrayList<Cell>();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                readingOrder.add(new Cell(j, i));
            }
        }
        return readingOrder;
    }

    /**
     * Constructs an array containing all Game Board Cells following the spiral
     * order (using the COLUMNS and ROWS constants)
     * 
     * @return ordered array of Cells
     */
    private static ArrayList<Cell> spiralOrder() {
        // Declare and fill two arrays that are useful for the ordering algorithm
        ArrayList<Integer> ix = new ArrayList<>();
        ArrayList<Integer> iy = new ArrayList<>();
        for (int i = 0; i < COLUMNS; i++) {
            ix.add(i);
        }
        for (int i = 0; i < ROWS; i++) {
            iy.add(i);
        }
        
        // Declare needed variables
        boolean horizontal = true;
        ArrayList<Cell> spiral = new ArrayList<>();
        
        // Apply ordering Algorithm
        while (!ix.isEmpty() && !iy.isEmpty()) {
            ArrayList<Integer> i1 = horizontal ? ix : iy;
            ArrayList<Integer> i2 = horizontal ? iy : ix;
            int c2 = i2.remove(0);
            for (int c1 : i1) {
                spiral.add(horizontal ? new Cell(c1, c2) : new Cell(c2, c1));
            }
            Collections.reverse(i1);
            horizontal = !horizontal;
        }
        return spiral;
    }
}