package ch.epfl.xblast.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

/**
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class BoardPainter {

    private final Map<Block,BlockImage> palette;
    private final BlockImage shadow;
    
    /**
     * constructor 
     * @param palette
     *          associate each block to an image
     * @param shadow
     *          image use for a shadow
     *      
     */
    public BoardPainter(Map<Block,BlockImage> palette, BlockImage shadow){
        this.palette = Collections.unmodifiableMap(
                new HashMap<>(Objects.requireNonNull(palette)));
        this.shadow = Objects.requireNonNull(shadow);
    }
    
    
    /**
     * give the corresponding image number to represent the block at the given Cell on the board
     * @param board
     *          the board to paint
     * @param cell
     *          the cell in board to paint
     * @return the byte corresponding to the image representing the Cell
     */
    public byte byteForCell(Board board, Cell cell){
        
        Block b= board.blockAt(cell);
        boolean isShadow = b.isFree() && board.blockAt(cell.neighbor(Direction.W)).castsShadow();
        BlockImage image =  isShadow? shadow : palette.get(b);
        
        return (byte) image.ordinal();
    }
}
