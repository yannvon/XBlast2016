package ch.epfl.DONT_SUBMIT;

/*
 * Author:      Zak Cook
 * Date:        Mar 1, 2016
 */

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public class BoardZackTest {

    @Test
    public void isMyQuadrantCorrectMate() {

        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;

        Board boardOfQuadrantNW = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

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

        // Print both boards

        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                System.out
                        .print(boardOfQuadrantNW.blockAt(new Cell(j, i)) + " ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();

        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                System.out.print(boardOfRows.blockAt(new Cell(j, i)) + " ");
                assertEquals(boardOfQuadrantNW.blockAt(new Cell(j, i)),
                        boardOfRows.blockAt(new Cell(j, i)));
            }
            System.out.println();
        }

        // Assert equals for each block of both boards

        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                assertEquals(boardOfQuadrantNW.blockAt(new Cell(j, i)),
                        boardOfRows.blockAt(new Cell(j, i)));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void illegalArgumentReturnsExcetpion() {
        List<Sq<Block>> illegalList = new ArrayList<Sq<Block>>();
        new Board(illegalList);
    }
}