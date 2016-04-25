package ch.epfl.xblast.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * class that represent a level with a initial gameState and a BoardPainter
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Level {
    
    /*
     * DEFAULT LEVEL
     */
    public static final Level DEFAULT_LEVEL = new Level(
            defaultBoardPainter(), defaultGameState());
    
    /**
     * ADDITIONAL Static method that constructs a classic BoardPainter that uses
     * the standard images.
     * 
     * @return a classic BoardPainter
     */
    private static BoardPainter defaultBoardPainter(){
        Map<Block,BlockImage> palette = new HashMap<>();
        palette.put(Block.INDESTRUCTIBLE_WALL,BlockImage.DARK_BLOCK);
        palette.put(Block.FREE,BlockImage.IRON_FLOOR);
        palette.put(Block.DESTRUCTIBLE_WALL,BlockImage.EXTRA);
        palette.put(Block.CRUMBLING_WALL,BlockImage.EXTRA_O);
        palette.put(Block.BONUS_RANGE,BlockImage.BONUS_RANGE);
        palette.put(Block.BONUS_BOMB,BlockImage.BONUS_BOMB);

        return new BoardPainter(palette, BlockImage.IRON_FLOOR_S);
    }
    
    /**
     * ADDITIONAL Static method that constructs the default initial GameState.
     * @return a default initial GameState
     */
    private static  GameState defaultGameState(){
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        
        Board board = Board.ofQuadrantNWBlocksWalled(Arrays.asList(
                Arrays.asList(__, __, __, __, __, xx, __),
                Arrays.asList(__, XX, xx, XX, xx, XX, xx), 
                Arrays.asList(__, xx, __, __, __, xx, __),
                Arrays.asList(xx, XX, __, XX, XX, XX, XX), 
                Arrays.asList(__, xx, __, xx, __, __, __),
                Arrays.asList(xx, XX, xx, XX, xx, XX, __)));
        
        List<Player> players = Arrays.asList(
                new Player(PlayerID.PLAYER_1,3,new Cell(1,1),2,3),
                new Player(PlayerID.PLAYER_2,3,new Cell(13,1),2,3),
                new Player(PlayerID.PLAYER_3,3,new Cell(13,11),2,3),
                new Player(PlayerID.PLAYER_4,3,new Cell(1,11),2,3)
                );
        
        return new GameState(board, players);
    }
    
    /*
     * Attributes
     */
    private final BoardPainter boardPainter;
    private final GameState initialGameState;
    
    /**
     * Constructor
     * @param boardPainter
     * @param initialGame
     */
    public Level(BoardPainter boardPainter, GameState initialGame) {
        this.boardPainter= boardPainter;
        this.initialGameState=initialGame;
    }

    /**
     * @return the boardPainter of the Level
     */
    public BoardPainter boardPainter() {
        return boardPainter;
    }

    /**
     * @return the gameState at the beginning of the game 
     */
    public GameState initialGameState() {
        return initialGameState;
    }
}
