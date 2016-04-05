package ch.epfl.xblast.server.debug;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.Block;
import ch.epfl.xblast.server.Board;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Player;

/**
 * Executable class that displays a random game.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public class RandomGame {
    
    //Constants used for convenience
    private final static String ESC = "\033[";
    private static final String background = "\u001b[41m";
    
    private static final RandomEventGenerator RANDOM = new RandomEventGenerator(2016, 30, 100);



    private static final Block __ = Block.FREE;
    private static final Block XX = Block.INDESTRUCTIBLE_WALL;
    private static final Block xx = Block.DESTRUCTIBLE_WALL;
    
    private static final Board board = Board.ofQuadrantNWBlocksWalled(
            Arrays.asList(
                    Arrays.asList(__, __, __, __, __, xx, __),
                    Arrays.asList(__, XX, xx, XX, xx, XX, xx),
                    Arrays.asList(__, xx, __, __, __, xx, __),
                    Arrays.asList(xx, XX, __, XX, XX, XX, XX),
                    Arrays.asList(__, xx, __, xx, __, __, __),
                    Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
    
    
    private static final List<Player> players= Arrays.asList(
            new Player(PlayerID.PLAYER_1,3,new Cell(1,1),2,3),
            new Player(PlayerID.PLAYER_2,3,new Cell(13,1),2,3),
            new Player(PlayerID.PLAYER_3,3,new Cell(13,11),2,3),
            new Player(PlayerID.PLAYER_4,3,new Cell(1,11),2,3)
            );

    private RandomGame() {}

    public static void main(String[] args) throws InterruptedException, IOException {
        
        //1) create new game
        GameState game = new GameState(board, players);
        
        
        //2) make the game evolve and display it (new tick all 50ms)
        while(!game.isGameOver()){
            
            GameStatePrinter.printGameState(game);
            game = game.next(RANDOM.randomSpeedChangeEvents(), RANDOM.randomBombDropEvents());
            Thread.sleep(50);
            //--- POWER SHELL ---
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            
            //--- ConEmu ---
            //System.out.print(ESC + "2J"); 
        }
        System.out.println(game.winner().get());
        System.out.println(game.winner().get().ordinal());
        
        
        
    }

}
