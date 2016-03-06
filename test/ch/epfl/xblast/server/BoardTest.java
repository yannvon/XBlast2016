package ch.epfl.xblast.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;

/**
 * Test Board class.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen(258857)
 *
 */
public class BoardTest {
    Block __ = Block.FREE;
    Block XX = Block.INDESTRUCTIBLE_WALL;
    Block xx = Block.DESTRUCTIBLE_WALL;

    /**
     * test the size exception for all functions
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void constructorSizeException() {
        Board a = new Board(new ArrayList<Sq<Block>>());
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void quadrantRowBuilderSizeException() {
        List<List<Block>> list = new ArrayList<List<Block>>();
        Board a = Board.ofQuadrantNWBlocksWalled(list);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void ofRowsSizeException() {
        List<List<Block>> list = new ArrayList<List<Block>>();
        Board a = Board.ofRows(list);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void ofInnerBlocksWalledSizeException() {
        List<List<Block>> list = new ArrayList<List<Block>>();
        Board a = Board.ofInnerBlocksWalled(list);
    }

    /**
     * test if a board of Rows and a board of quadrant are the same
     */
    @Test
    public void ofRowsVsOfQuadrant() {
        Board boardOfRows = Board.ofRows(Arrays.asList(
                Arrays.asList(XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX,
                        XX, XX, XX),
                Arrays.asList(XX, __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __, XX),
                Arrays.asList(XX, __, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx,
                        XX, __, XX),
                Arrays.asList(XX, __, xx, __, __, __, xx, __, xx, __, __, __,
                        xx, __, XX),
                Arrays.asList(XX, xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __,
                        XX, xx, XX),
                Arrays.asList(XX, __, xx, __, xx, __, __, __, __, __, xx, __,
                        xx, __, XX),
                Arrays.asList(XX, xx, XX, xx, XX, xx, XX, __, XX, xx, XX, xx,
                        XX, xx, XX),
                Arrays.asList(XX, __, xx, __, xx, __, __, __, __, __, xx, __,
                        xx, __, XX),
                Arrays.asList(XX, xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __,
                        XX, xx, XX),
                Arrays.asList(XX, __, xx, __, __, __, xx, __, xx, __, __, __,
                        xx, __, XX),
                Arrays.asList(XX, __, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx,
                        XX, __, XX),
                Arrays.asList(XX, __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __, XX),
                Arrays.asList(XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX,
                        XX, XX, XX)));

        Board boardOfQuadrant = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                assertEquals(boardOfQuadrant.blockAt(new Cell(j, i)),
                        boardOfRows.blockAt(new Cell(j, i)));
            }
        }

    }

    /**
     * test if a board of rows and a board of inner are the same
     */
    @Test
    public void ofRowsVsOfInner() {
        Board boardOfRows = Board.ofRows(Arrays.asList(
                Arrays.asList(XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX,
                        XX, XX, XX),
                Arrays.asList(XX, __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __, XX),
                Arrays.asList(XX, __, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx,
                        XX, __, XX),
                Arrays.asList(XX, __, xx, __, __, __, xx, __, xx, __, __, __,
                        xx, __, XX),
                Arrays.asList(XX, xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __,
                        XX, xx, XX),
                Arrays.asList(XX, __, xx, __, xx, __, __, __, __, __, xx, __,
                        xx, __, XX),
                Arrays.asList(XX, xx, XX, xx, XX, xx, XX, __, XX, xx, XX, xx,
                        XX, xx, XX),
                Arrays.asList(XX, __, xx, __, xx, __, __, __, __, __, xx, __,
                        xx, __, XX),
                Arrays.asList(XX, xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __,
                        XX, xx, XX),
                Arrays.asList(XX, __, xx, __, __, __, xx, __, xx, __, __, __,
                        xx, __, XX),
                Arrays.asList(XX, __, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx,
                        XX, __, XX),
                Arrays.asList(XX, __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __, XX),
                Arrays.asList(XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX, XX,
                        XX, XX, XX)));

        Board boardOfInner = Board.ofInnerBlocksWalled(Arrays.asList(
                Arrays.asList(__, __, __, __, __, xx, __, xx, __, __, __, __,
                        __),
                Arrays.asList(__, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx, XX,
                        __),
                Arrays.asList(__, xx, __, __, __, xx, __, xx, __, __, __, xx,
                        __),
                Arrays.asList(xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __, XX,
                        xx),
                Arrays.asList(__, xx, __, xx, __, __, __, __, __, xx, __, xx,
                        __),
                Arrays.asList(xx, XX, xx, XX, xx, XX, __, XX, xx, XX, xx, XX,
                        xx),
                Arrays.asList(__, xx, __, xx, __, __, __, __, __, xx, __, xx,
                        __),
                Arrays.asList(xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __, XX,
                        xx),
                Arrays.asList(__, xx, __, __, __, xx, __, xx, __, __, __, xx,
                        __),
                Arrays.asList(__, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx, XX,
                        __),
                Arrays.asList(__, __, __, __, __, xx, __, xx, __, __, __, __,
                        __)));
        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                assertEquals(boardOfInner.blockAt(new Cell(j, i)),
                        boardOfRows.blockAt(new Cell(j, i)));
            }
        }
    }

    /**
     * test if the references to different sequences of blocks from quadrant are
     * different
     */
    @Test
    public void CellsHaveNotTheSameReference() {

        Board boardOfQuadrant = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

        assertNotSame(boardOfQuadrant.blocksAt(new Cell(1, 1)), boardOfQuadrant
                .blocksAt(new Cell(Cell.COLUMNS - 2, Cell.ROWS - 2)));

        assertNotSame(boardOfQuadrant.blocksAt(new Cell(1, 1)),
                boardOfQuadrant.blocksAt(new Cell(Cell.COLUMNS - 2, 1)));

        assertNotSame(boardOfQuadrant.blocksAt(new Cell(1, 1)),
                boardOfQuadrant.blocksAt(new Cell(1, Cell.ROWS - 2)));

    }
}
