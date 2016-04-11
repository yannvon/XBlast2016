package ch.epfl.DONT_SUBMIT;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.NoSuchElementException;

import org.junit.Test;

import ch.epfl.xblast.server.Bomb;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.Cell;
import ch.epfl.cs108.Sq;
import ch.epfl.xblast.PlayerID;

public class BombTest {
    
    @Test(expected = NullPointerException.class)
    public void testIfOwnerIDIsNull(){
        Bomb b = new Bomb(null,new Cell(1,2),Sq.constant(2),2);
    }
    
    @Test(expected = NullPointerException.class)
    public void testIfPositionIsNull(){
        Bomb b = new Bomb(PlayerID.PLAYER_1,null,Sq.constant(2),2);
    }
    
    @Test(expected = NullPointerException.class)
    public void testIfSqIsNull(){
        Bomb b = new Bomb(PlayerID.PLAYER_1,new Cell(1,2),null,2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testIfRangeIsNegative(){
        Bomb b = new Bomb(PlayerID.PLAYER_1,new Cell(1,2),Sq.constant(2),-2);
    }
    
    @Test
    public void fuseSequenceEndsIsCorrectUntil1(){
        Bomb b = new Bomb(PlayerID.PLAYER_1,new Cell(1,1),5,2);
        System.out.print(b.fuseLengths().head().intValue());
        System.out.print(b.fuseLengths().tail().head().intValue());
        System.out.print(b.fuseLengths().tail().tail().head().intValue());
        System.out.print(b.fuseLengths().tail().tail().tail().head().intValue());
        assertEquals(1,b.fuseLengths().tail().tail().tail().tail().head().intValue());
    }
    
    @Test(expected = NoSuchElementException.class)
    public void fuseSequenceEndsAt1(){
        Bomb b = new Bomb(PlayerID.PLAYER_1,new Cell(1,1),5,2);
        b.fuseLengths().tail().tail().tail().tail().tail().head();
    }
    
    
    
}
