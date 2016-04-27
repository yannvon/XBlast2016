package ch.epfl.xblast.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

/**
 * Class that represents a level by an initial GameState and a BoardPainter.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Level {
    
    /*
     * DEFAULT LEVEL
     */
    public static final Level DEFAULT_LEVEL = new Level(defaultBoardPainter(),
            defaultGameState());

    /**
     * ADDITIONAL Static method that constructs a classic BoardPainter that uses
     * the standard images.
     * 
     * @return a default BoardPainter
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
     * ADDITIONAL Static method that constructs the default initial GameState,
     * consisting of the default Board and the default player configuration.
     * 
     * The default player configuration places all players in the corner and the
     * players start with 3 lives, a bomb range of 3 and 2 maximal bombs.
     * 
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
                new Player(PlayerID.PLAYER_1, 3, new Cell( 1, 1), 2, 3),
                new Player(PlayerID.PLAYER_2, 3, new Cell(13, 1), 2, 3),
                new Player(PlayerID.PLAYER_3, 3, new Cell(13,11), 2, 3),
                new Player(PlayerID.PLAYER_4, 3, new Cell( 1,11), 2, 3));
        
        return new GameState(board, players);
    }

    /*
     * Attributes
     */
    private final BoardPainter boardPainter;
    private final GameState initialGameState;

    /**
     * Constructor taking a BoardPainter and an initial GameState as arguments.
     * 
     * @param boardPainter
     *            the BoardPainter used for this level
     * @param initialGame
     *            the GameState at the start of the game
     * @throws NullPointerException
     *             if one of the arguments are null
     */
    public Level(BoardPainter boardPainter, GameState initialGame) {
        this.boardPainter = Objects.requireNonNull(boardPainter);
        this.initialGameState = Objects.requireNonNull(initialGame);
    }

    /**
     * Returns the BoardPainter of this Level
     * 
     * @return the boardPainter of the Level
     */
    public BoardPainter boardPainter() {
        return boardPainter;
    }

    /**
     * Returns the initial GameState of the Level
     * 
     * @return the initial GameState
     */
    public GameState initialGameState() {
        return initialGameState;
    }
}
