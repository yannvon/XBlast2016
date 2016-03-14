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
    
    @Test
    public void timeOutTest(){
        GameState game = new GameState(Ticks.TOTAL_TICKS,board,players,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        assertTrue(game.isGameOver());
        assertEquals(0,game.remainingTime(),1e-9);
        assertEquals(Optional.empty(),game.winner());
    }
    @Test
    public void OneTickToEndTest(){
        GameState game = new GameState(Ticks.TOTAL_TICKS-1,board,players,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        assertFalse(game.isGameOver());
        assertEquals(Ticks.TICK_NANOSECOND_DURATION*1e-9,game.remainingTime(),1e-11);
        assertEquals(Optional.empty(),game.winner());
    }
    
    @Test
    public void OnePlayerLeftTest(){
        List<Player> pl= Arrays.asList(
                new Player(PlayerID.PLAYER_1,3,new Cell(1,1),3,3),
                new Player(PlayerID.PLAYER_2,0,new Cell(1,1),3,3),
                new Player(PlayerID.PLAYER_3,0,new Cell(1,1),3,3),
                new Player(PlayerID.PLAYER_4,0,new Cell(1,1),3,3)
                );
        
        GameState game = new GameState(60,board,pl,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        assertTrue(game.isGameOver());
        assertEquals(120-3,game.remainingTime(),1e-11);
        assertEquals(PlayerID.PLAYER_1,game.winner().get());
        assertEquals(1,game.alivePlayers().size());
    }

    @Test(expected= java.lang.IllegalArgumentException.class)
    public void ticksException(){
        GameState game = new GameState(-1,board,players,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());

    }
    
    @Test(expected= java.lang.NullPointerException.class)
    public void BoardException(){
        GameState game = new GameState(0,null,players,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());

    }
    @Test(expected= java.lang.NullPointerException.class)
    public void playerException(){
        GameState game = new GameState(1,board,null,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());

    }
    @Test(expected= java.lang.IllegalArgumentException.class)
    public void nbPlayerException(){
        List<Player> pl= Arrays.asList();
        GameState game = new GameState(0,board,pl,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());

    }
    @Test(expected= java.lang.NullPointerException.class)
    public void bombException(){
        GameState game = new GameState(1,board,players,null,new ArrayList<>(),new ArrayList<>());

    }
    @Test(expected= java.lang.NullPointerException.class)
    public void explosionException(){
        GameState game = new GameState(1,board,players,new ArrayList<>(),null,new ArrayList<>());

    }
    @Test(expected= java.lang.NullPointerException.class)
    public void blastException(){
        GameState game = new GameState(1,board,players,new ArrayList<>(),new ArrayList<>(),null);

    }
    
}
