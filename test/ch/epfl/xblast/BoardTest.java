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
    
    @Test
    public void constructor(){
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
       
        
        
        
        Board boardOfQuadrantNW = Board.ofQuadrantNWBlocksWalled(
                Arrays.asList(
                        Arrays.asList(__, __, __, __, __, xx, __),
                        Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                        Arrays.asList(__, xx, __, __, __, xx, __),
                        Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                        Arrays.asList(__, xx, __, xx, __, __, __),
                        Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        Board boardOfInnerBlocksWalled= Board.ofInnerBlocksWalled( Arrays.asList(
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __),
                Arrays.asList( __, __, __, __, __, xx, __, xx, __, __, __,
                        __, __)
                
                ));
        

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
    }
    
    

}
