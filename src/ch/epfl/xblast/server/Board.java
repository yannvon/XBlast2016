package ch.epfl.xblast.server;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;

/**
 * The immutable class Board represents the Game Board of the xblast game.
 * 
 * @author Loic Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Board {

    // Attributes
    private List<Sq<Block>> board; // FIXME final since immutable class?

    /**
     * Constructor of a board taking a list of block sequences as param.
     * 
     * @param blocks
     *            a list of block sequences
     * @throws IllegalArgumentException
     *             if the size of the List is not correct
     */
    public Board(List<Sq<Block>> blocks) {
        if (board.size() != Cell.COUNT) {// FIXME added constant instead of 195
            throw new IllegalArgumentException();   //FIXME add message?
        }

        board = new ArrayList<>(blocks); // FIXME cant use List?

    }

    /**
     * Constructs a constant Board, a given block matrix.
     * 
     * @param rows
     *            a matrix of blocks
     * @return a constant Board
     * @throws IllegalArgumentException
     *             if the list rows is not in the right format
     */
    public static final Board ofRows(List<List<Block>> rows) {
        // TODO
        for (int i = 0; i < rows.size();) {

        }

        return null;
    }
    
    

    public static final void checkBlockMatrix(List<List<Block>> matrix,
            int rows, int columns) {
        // TODO

    }
}
