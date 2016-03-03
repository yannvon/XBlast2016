package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

public class BoardTest {
    Block __ = Block.FREE;
    Block XX = Block.INDESTRUCTIBLE_WALL;
    Block xx = Block.DESTRUCTIBLE_WALL;
 

    /**
     * test the size exception for all functions
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void constructorSizeException() {
        Board a= new Board(new ArrayList<Sq<Block>>());
    }
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void quadrantRowBuilderSizeException() {
        List<List<Block>> list = new ArrayList<List<Block>>();
        Board a=Board.ofQuadrantNWBlocksWalled(list);
    }
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void ofRowsSizeException(){
        List<List<Block>> list = new ArrayList<List<Block>>();
        Board a= Board.ofRows(list);
    }
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void ofInnerBlocksWalledSizeException() {
        List<List<Block>> list = new ArrayList<List<Block>>();
        Board a=Board.ofInnerBlocksWalled(list);
    }
    
  
 
    /**
     * show the different Board in the console 
     */
    @Test
    public void show(){
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
            Arrays.asList(
                    Arrays.asList(__, __, __, __, __, xx, __),
                    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                    Arrays.asList(__, xx, __, __, __, xx, __),
                    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                    Arrays.asList(__, xx, __, xx, __, __, __),
                    Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
    
  /*  Board boardOfInner= Board.ofInnerBlocksWalled( Arrays.asList(
            Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                    __, __),
            Arrays.asList( __, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx,
                    XX, __),
            Arrays.asList( __, xx, __, __, __, xx, __, xx, __, __, __,
                    xx, __),
            Arrays.asList( xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __,
                    XX, xx),
            Arrays.asList( __, xx, __, xx, __, __, __, __, __, xx, __,
                    xx, __),
            Arrays.asList( xx, XX, xx, XX, xx, XX, __, XX, xx, XX, xx,
                    XX, xx),
            Arrays.asList( __, xx, __, xx, __, __, __, __, __, xx, __,
                    xx, __),
            Arrays.asList( xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __,
                    XX, xx),
            Arrays.asList( __, xx, __, __, __, xx, __, xx, __, __, __,
                    xx, __),
            Arrays.asList( __, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx,
                    XX, __),
            Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                    __, __)
            ));
    */
        System.out.println("\n\tBoard of rows:");
        
        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                System.out
                        .print(boardOfRows.blockAt(new Cell(j, i)) + " ");
            }
            System.out.println();
        }

        System.out.println("\n\tBoard of inner blocks walled:");
        
      /*  
        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                System.out
                        .print(boardOfInner.blockAt(new Cell(j, i)) + " ");
            }
            System.out.println();
        }*/

        System.out.println("\n\tBoard of quadrant NW blocks walled:");
        
        
        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                System.out
                        .print(boardOfQuadrant.blockAt(new Cell(j, i)) + " ");
            }
            System.out.println();
        }
        
    }
    
    
    
    /**
     * test if a board of Rows and a board of quadrant are the same
     */
    @Test
    public void ofRowsVsOfQuadrant(){
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
            Arrays.asList(
                    Arrays.asList(__, __, __, __, __, xx, __),
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
    public void ofRowsVsOfInner(){
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
    
        Board boardOfInner= Board.ofInnerBlocksWalled( Arrays.asList(
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx,
                        XX, __),
                Arrays.asList( __, xx, __, __, __, xx, __, xx, __, __, __,
                        xx, __),
                Arrays.asList( xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __,
                        XX, xx),
                Arrays.asList( __, xx, __, xx, __, __, __, __, __, xx, __,
                        xx, __),
                Arrays.asList( xx, XX, xx, XX, xx, XX, __, XX, xx, XX, xx,
                        XX, xx),
                Arrays.asList( __, xx, __, xx, __, __, __, __, __, xx, __,
                        xx, __),
                Arrays.asList( xx, XX, __, XX, XX, XX, XX, XX, XX, XX, __,
                        XX, xx),
                Arrays.asList( __, xx, __, __, __, xx, __, xx, __, __, __,
                        xx, __),
                Arrays.asList( __, XX, xx, XX, xx, XX, xx, XX, xx, XX, xx,
                        XX, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __)
                ));
        for (int i = 0; i < Cell.ROWS; i++) {
            for (int j = 0; j < Cell.COLUMNS; j++) {
                assertEquals(boardOfInner.blockAt(new Cell(j, i)),
                    boardOfRows.blockAt(new Cell(j, i)));
            }
        }
    }
    
    
    /**
     * test if the references to different sequences of blocks from quadrant are different
     */
    @Test
    public void CellsHaveNotTheSameReference(){
         
        Board boardOfQuadrant = Board.ofQuadrantNWBlocksWalled(
            Arrays.asList(
                    Arrays.asList(__, __, __, __, __, xx, __),
                    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                    Arrays.asList(__, xx, __, __, __, xx, __),
                    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                    Arrays.asList(__, xx, __, xx, __, __, __),
                    Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        assertNotSame( boardOfQuadrant.blocksAt(new Cell(1,1)),boardOfQuadrant.blocksAt(new Cell(Cell.COLUMNS-2 ,Cell.ROWS-2)));
        
        assertNotSame( boardOfQuadrant.blocksAt(new Cell(1,1)),boardOfQuadrant.blocksAt(new Cell(Cell.COLUMNS-2 ,1)));

        assertNotSame( boardOfQuadrant.blocksAt(new Cell(1,1)),boardOfQuadrant.blocksAt(new Cell(1 ,Cell.ROWS-2)));
        
        
        
    }
}
