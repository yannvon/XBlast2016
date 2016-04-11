package ch.epfl.DONT_SUBMIT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;

import org.junit.Test;
public class boardTest {
    @Test(expected = IllegalArgumentException.class)
    public void boardTooMuchElements(){
        List<Sq<Block>> l = new ArrayList<>();
        for(int i = 0; i<194; ++i){
            l.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
        }
        new Board(l);
    }
    @Test
    public void test(){
        List<Sq<Block>> l = new ArrayList<>();
        for(int i = 0; i<195; ++i){
            l.add(Sq.constant(Block.INDESTRUCTIBLE_WALL));
        }
        new Board(l);
    }
}
