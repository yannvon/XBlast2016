package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

import org.junit.Test;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.debug.ClassComparator;
import ch.epfl.xblast.server.debug.GameStatePrinter;

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
            new Player(PlayerID.PLAYER_2,3,new Cell(7,6),3,3),
            new Player(PlayerID.PLAYER_3,3,new Cell(8,9),3,3),
            new Player(PlayerID.PLAYER_4,3,new Cell(3,4),3,3)
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
        GameState game = new GameState(Ticks.TOTAL_TICKS+1,board,players,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        assertTrue(game.isGameOver());
        assertEquals(-Ticks.TICK_NANOSECOND_DURATION*1e-9,game.remainingTime(),1e-11);
        assertEquals(Optional.empty(),game.winner());
    }
    @Test
    public void OneTickToEndTest(){
        GameState game = new GameState(Ticks.TOTAL_TICKS,board,players,new ArrayList<>(),new ArrayList<>(),new ArrayList<>());
        assertFalse(game.isGameOver());
        assertEquals(0,game.remainingTime(),1e-11);
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
    //   ---Next Tests---
         
    private final Map<PlayerID, Optional<Direction>> speedChangeEvents = new HashMap<>();
    
    @Test
    public void normalNextTest(){
        GameState a = new GameState(board,players);
        GameState b = a.next(speedChangeEvents, new HashSet<>());
        
        assertEquals(a.blastedCells(),b.blastedCells());
        assertEquals(a.alivePlayers().size(),b.alivePlayers().size());
        assertEquals(a.bombedCells(),b.bombedCells());
        List<Cell> allCells=Cell.ROW_MAJOR_ORDER;
        for(Cell c: allCells){
            assertTrue(ClassComparator.compareSq(a.board().blocksAt(c).tail(),b.board().blocksAt(c)));
        }
        
    }
    
    @Test
    public void oneBombNextTest(){
        GameState a = new GameState(board,players);
        Set<PlayerID> bombdrp= new HashSet<>();
        bombdrp.add(PlayerID.PLAYER_1);
        
        GameState game = a.next(speedChangeEvents,bombdrp);
        Cell bombPosition = a.players().get(0).position().containingCell();
        
        assertEquals(a.blastedCells(),game.blastedCells());
        assertEquals(a.alivePlayers().size(),game.alivePlayers().size());
        assertTrue(game.bombedCells().containsKey(bombPosition));
       
        //the bomb didn't explode yet
        for(int i=0; i<Ticks.BOMB_FUSE_TICKS-2;i++){
            game=game.next(speedChangeEvents, new HashSet<>());
            assertTrue(game.bombedCells().containsKey(bombPosition));
            assertEquals(a.blastedCells(),game.blastedCells());
            assertEquals(Ticks.BOMB_FUSE_TICKS-i-2,game.bombedCells().get(bombPosition).fuseLength());
        }
        
        //---the bomb explode---
        Set<Cell> supposedBlastedCells = new HashSet<>();
      
        //First Tick after the explosion
        game=game.next(speedChangeEvents, new HashSet<>());
        assertTrue(game.bombedCells().isEmpty());
        
        
        //Second Tick
        supposedBlastedCells.add(bombPosition);
        game=game.next(speedChangeEvents, new HashSet<>());
        assertEquals(supposedBlastedCells,game.blastedCells());
        
        //Third Tick
        supposedBlastedCells.add(bombPosition.neighbor(Direction.N));
        supposedBlastedCells.add(bombPosition.neighbor(Direction.E));
        supposedBlastedCells.add(bombPosition.neighbor(Direction.S));
        supposedBlastedCells.add(bombPosition.neighbor(Direction.W));
        game=game.next(speedChangeEvents, new HashSet<>());
        assertEquals(supposedBlastedCells,game.blastedCells());
        
        //Fourth Tick
        supposedBlastedCells.add(bombPosition.neighbor(Direction.S).neighbor(Direction.S));
        supposedBlastedCells.add(bombPosition.neighbor(Direction.E).neighbor(Direction.E));
        game=game.next(speedChangeEvents, new HashSet<>());
        assertEquals(supposedBlastedCells,game.blastedCells());
        
        for(int i=3; i<Ticks.EXPLOSION_TICKS;i++){
            game=game.next(speedChangeEvents, new HashSet<>());
            assertEquals(supposedBlastedCells,game.blastedCells());
        }
        
        //last ticks
        supposedBlastedCells.remove(bombPosition);
        game=game.next(speedChangeEvents, new HashSet<>());
        assertEquals(supposedBlastedCells,game.blastedCells());
        
        supposedBlastedCells.remove(bombPosition.neighbor(Direction.N));
        supposedBlastedCells.remove(bombPosition.neighbor(Direction.E));
        supposedBlastedCells.remove(bombPosition.neighbor(Direction.S));
        supposedBlastedCells.remove(bombPosition.neighbor(Direction.W));
        game=game.next(speedChangeEvents, new HashSet<>());
        assertEquals(supposedBlastedCells,game.blastedCells());
        
        supposedBlastedCells.remove(bombPosition.neighbor(Direction.S).neighbor(Direction.S));
        supposedBlastedCells.remove(bombPosition.neighbor(Direction.E).neighbor(Direction.E));
        
        game=game.next(speedChangeEvents, new HashSet<>());
        assertEquals(supposedBlastedCells,game.blastedCells());
        assertTrue(game.blastedCells().isEmpty());
    }
    
    /**
     * VISUAL TEST:
     * 
     * ---initialization---
     *  enter the coordinates of the starting Cell for each player
     *  enter the maximums bombs for the players
     *  enter the range of the players
     *  each players have only 1 life
     *   
     * ---Control---
     *   - "1","2","3","4" : select the player you control
     *   - "w","a","s","d" : change the direction of the controlled player
     *   - "x"             : stop the player at the next central SubCell 
     *   - "e"             : the controlled player drop a bomb
     */
    @Test
    public void visualNextTest(){
        Scanner scan =new Scanner(System.in);
        List<Player> gPlayers =new ArrayList<>();
        System.out.println("maxBombs?");
        int maxBombs= scan.nextInt();
        System.out.println("range?");
        int range= scan.nextInt();
        System.out.println("life?");
        int life= scan.nextInt();
        for(int i=0; i<4;i++){
            System.out.println("player " + (i+1)+ " position :");
            gPlayers.add(new Player(PlayerID.values()[i],life,new Cell(scan.nextInt(),scan.nextInt()),maxBombs,range));
        }
        
        GameState game= new GameState(board,gPlayers);
        boolean inGame=true;
        PlayerID control = PlayerID.PLAYER_1; 
        while( inGame){
            String s= scan.nextLine();
            Set<PlayerID> bombdrp= new HashSet<>();
            Map<PlayerID,Optional<Direction>> speedChange= new HashMap<>();
            switch(s){
            case "1":
                control=PlayerID.PLAYER_1;
                break;
            case "2":
                control=PlayerID.PLAYER_2;
                break;
            case "3":
                control=PlayerID.PLAYER_3;
                break;
            case "4":
                control=PlayerID.PLAYER_4;
                break;
            case "e":
                bombdrp.add(control);
                break;
            case "w":
                speedChange.put(control,Optional.of(Direction.N));
                break;
            case "a":
                speedChange.put(control,Optional.of(Direction.W));
                break;
            case "s":
                speedChange.put(control,Optional.of(Direction.S));
                break;
            case "d":
                speedChange.put(control,Optional.of(Direction.E));
                break;
            case "x":
                speedChange.put(control,Optional.empty());
                break;
            case "0":
                inGame=false;
                
            }
            game=game.next(speedChange, bombdrp);
            GameStatePrinter.printGameState(game);
            
            
        }
        scan.close();
        
    }
    
    
    
}
