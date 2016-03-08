package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;

/**
 * The immutable class Board represents the Game Board of the xblast game.
 * 
 * @author Loic Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Board {

    // Attributes
    private final List<Sq<Block>> board;

    /**
     * Constructor of a board taking a list of block sequences as param.
     * 
     * @param blocks
     *            a list of block sequences
     * @throws IllegalArgumentException
     *             if the size of the List is not correct
     */
    public Board(List<Sq<Block>> blocks) {
        if (blocks.size() != Cell.COUNT) {
            throw new IllegalArgumentException("The amount of Blocks doesn't match the expected value of " + Cell.COUNT );
        }

        board = new ArrayList<>(blocks);

    }

    /**
     * Constructs a constant Board, given block matrix.
     * 
     * @param rows
     *            a matrix of blocks
     * @return a constant Board
     * @throws IllegalArgumentException
     *             if the list rows is not in the right format
     */
    public static Board ofRows(List<List<Block>> rows) {
        // check matrix
        checkBlockMatrix(rows, Cell.ROWS, Cell.COLUMNS);

        // add a constant sequence of given block to a temporary ArrayList
        ArrayList<Sq<Block>> tempBoard = new ArrayList<>();

        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                tempBoard.add(Sq.constant(rows.get(i).get(j)));
            }
        }

        // return the new Board
        return new Board(tempBoard);

    }

    /**
     * Constructs a constant, walled Board, given the matrix of the inner
     * Blocks.
     * 
     * @param innerBlocks
     *            a matrix of Blocks without the outer ring
     * @return a constant and walled Board
     * @throws IllegalArgumentException
     *             if the list innerBlocks is not in the right format
     */
    public static Board ofInnerBlocksWalled(List<List<Block>> innerBlocks) {
        // check matrix
        checkBlockMatrix(innerBlocks, Cell.ROWS - 2, Cell.COLUMNS - 2);

        List<List<Block>> walledBlocks = new ArrayList<>();
        
        // add walls and copy the blocks of given matrix into new walled matrix
        for (int i = 0; i < Cell.ROWS-2; ++i) {
            walledBlocks.add( new ArrayList<>());
            walledBlocks.get(i).add(Block.INDESTRUCTIBLE_WALL);
            walledBlocks.get(i).addAll(innerBlocks.get(i));
            walledBlocks.get(i).add(Block.INDESTRUCTIBLE_WALL);
        }
        
        // add the first and last row of Wall-Blocks
        List<Block> walledRow = Collections.nCopies(Cell.COLUMNS, Block.INDESTRUCTIBLE_WALL);
        walledBlocks.add(0, walledRow); //costly operation, chosen for the sake of readability
        walledBlocks.add(walledRow);

        // call ofRows method to construct Board from walledBlocks matrix
        return ofRows(walledBlocks);
        
    }

    /**
     * Constructs a constant, symmetric and walled Board, given the matrix of
     * the inner Blocks of the North-West quadrant.
     * 
     * @param quadrantNWBlocks
     *            a matrix of the inner Blocks of the N-W quadrant
     * @return a constant, walled and symmetric Board
     * @throws IllegalArgumentException
     *             if the list quadrantNWBlocks is not in the right format
     */
    public static Board ofQuadrantNWBlocksWalled(List<List<Block>> quadrantNWBlocks) {
        
        // the expected matrix dimensions
        int rows = (Cell.ROWS - 1)/2;
        int cols = (Cell.COLUMNS - 1)/2;
        
        // check matrix
        checkBlockMatrix(quadrantNWBlocks, rows, cols);
       
        // temporary block matrix
        List<List<Block>> finalMatrix = new ArrayList<>();
        
        // mirror every row to get entire rows of the upper half board
        for(int i = 0; i < rows; i++){
            finalMatrix.add(Lists.mirrored(quadrantNWBlocks.get(i)));
        }
        
        // mirror the upper half board to get entire inner Board
        finalMatrix = Lists.mirrored(finalMatrix);

        // call ofInnerBlocksWalled to add walls and construct board
        return ofInnerBlocksWalled(finalMatrix);
    }
    
    
    /**
     * Returns the block sequence of a given cell
     * 
     * @param c
     *            Cell for which you want the block sequence
     * @return the block sequence of a specific cell
     */
    public Sq<Block> blocksAt(Cell c){
        return board.get(c.rowMajorIndex());
    }
    
    /**
     * Returns the current block of a cell.
     * 
     * @param c
     *            Cell for which block sequence head is searched
     * @return the block of a specific cell
     */
    public Block blockAt(Cell c){
        return blocksAt(c).head();
    }
    
    

    /**
     * Checks if a given matrix of blocks has the desired size.
     * 
     * @param matrix
     *            a list of lists of blocks
     * @param rows
     *            the amount of rows that the matrix should have
     * @param columns
     *            the amount of columns that the matrix should have
     * @throws IllegalArgumentException
     *             if the matrix doesn't have the right size
     */
    public static final void checkBlockMatrix(List<List<Block>> matrix,
            int rows, int columns) {

        int matrixRows = matrix.size();

        // 1) check if the amount of rows is correct
        if (matrixRows != rows) {
            throw new IllegalArgumentException(
                    "The amount of rows does not match desired value");
        }
        // 2) check if the amount of blocks in each column is correct
        else {
            for (int i = 0; i < matrixRows; i++) {
                if (matrix.get(i).size() != columns) {
                    throw new IllegalArgumentException(
                            "The amount of elements of row " + i
                                    + " does not match the desired value");
                }
            }
        }
    }
}
