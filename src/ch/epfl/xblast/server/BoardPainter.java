package ch.epfl.xblast.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;

/**
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class BoardPainter {

    private final Map<Block,BlockImage> palette;
    private final BlockImage shadow;
    
    /**TODO
     * @param palette
     * @param shadow
     */
    public BoardPainter(Map<Block,BlockImage> palette, BlockImage shadow){
        this.palette = Collections.unmodifiableMap(new HashMap<>(Objects.requireNonNull(palette)));
        this.shadow = Objects.requireNonNull(shadow);
    }
    
    
    /**TODO
     * @param board
     * @param cell
     * @return
     */
    public byte byteForCell(Board board, Cell cell){
        //TODO
        return 0;
    }
}
