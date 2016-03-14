package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

public class GameStateTest {

    //Constant used in Test
    private final Block __ = Block.FREE;
    private final Block XX = Block.INDESTRUCTIBLE_WALL;
    private final Block xx = Block.DESTRUCTIBLE_WALL;
    
    private final Board board = Board.ofQuadrantNWBlocksWalled(
            Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                    Arrays.asList(__, xx, __, __, __, xx, __),
                    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                    Arrays.asList(__, xx, __, xx, __, __, __),
                    Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
    
    private final List<Player> players= Arrays.asList(
            new Player(PlayerID.PLAYER_1,3,new Cell(1,1),3,3),
            new Player(PlayerID.PLAYER_2,3,new Cell(1,1),3,3),
            new Player(PlayerID.PLAYER_3,3,new Cell(1,1),3,3),
            new Player(PlayerID.PLAYER_4,3,new Cell(1,1),3,3)
            );
    
    
    
    
    @Test
    public void initialGameTest() {
        
        GameState a = new GameState(0,board,players,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        GameState b = new GameState(board,players);
        
        assertEquals(a.ticks(),b.ticks());
        assertEquals(a.remainingTime(),b.remainingTime(),1e-9);
        assertEquals(120,a.remainingTime(),1e-9);
        assertEquals(4,a.alivePlayers().size());
        assertFalse(a.isGameOver());
        assertFalse(b.isGameOver());
        assertEquals(Optional.empty(),a.winner());
        assertEquals(Optional.empty(),b.winner());
        
    }
    
    public void timeOutTest(){
        
    }

}
