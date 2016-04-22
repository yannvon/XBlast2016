package ch.epfl.xblast.server;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.PlayerID;

public final class Level {

    private final BoardPainter boardPainter;
    private final GameState initialGameState;
    
    
    /*
     * DEFAULT LEVEL
     */
    public static final Level DEFAULT_LEVEL = new Level(
            defaultBoardPainter(), new GameState(defaultBoard(), defaultPlayers()));
    
    /**
     * ADDITIONAL Static method that constructs a classic BoardPainter that uses
     * the standard images.
     * 
     * @return a classic BoardPainter
     */
    private static BoardPainter defaultBoardPainter(){
        Map<Block,BlockImage> palette = new HashMap<>();
        Block[] blocks=Block.values();
        BlockImage[] images = BlockImage.values();
        
        palette.put(blocks[0],images[0]);
        for(int i=1; i<blocks.length;i++){
            palette.put(blocks[i],images[i+1]);
        }
        return new BoardPainter(palette, images[1]);
    }
    
    private static  Board defaultBoard(){
        Block __ = Block.FREE;
        Block XX = Block.INDESTRUCTIBLE_WALL;
        Block xx = Block.DESTRUCTIBLE_WALL;
        return Board.ofQuadrantNWBlocksWalled(Arrays.asList(Arrays.asList(__, __, __, __, __, xx, __),
                Arrays.asList(__, XX, xx, XX, xx, XX, xx), Arrays.asList(__, xx, __, __, __, xx, __),
                Arrays.asList(xx, XX, __, XX, XX, XX, XX), Arrays.asList(__, xx, __, xx, __, __, __),
                Arrays.asList(xx, XX, xx, XX, xx, XX, __)));

    }
    
    private static List<Player> defaultPlayers(){
        return  Arrays.asList(
                new Player(PlayerID.PLAYER_1,3,new Cell(1,1),2,3),
                new Player(PlayerID.PLAYER_2,3,new Cell(13,1),2,3),
                new Player(PlayerID.PLAYER_3,3,new Cell(13,11),2,3),
                new Player(PlayerID.PLAYER_4,3,new Cell(1,11),2,3)
                );
    }
    
    
    
    
    public Level(BoardPainter boardPainter, GameState initialGame) {
        this.boardPainter= boardPainter;
        this.initialGameState=initialGame;
    }

    public BoardPainter boardPainter() {
        return boardPainter;
    }

    public GameState initialGameState() {
        return initialGameState;
    }
    
    
    
    

}
