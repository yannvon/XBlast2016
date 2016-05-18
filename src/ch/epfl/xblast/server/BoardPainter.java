package ch.epfl.xblast.server;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Direction;

/**
 * This immutable class represents a "painter" of a board.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 */
public final class BoardPainter {

    /*
     * Constants
     */
    private static final Direction LIGHT_DIRECTION = Direction.W;
    
    /*
     * Attributes
     */
    private final Map<Block,BlockImage> palette;
    private final BlockImage shadow;
    
    /**
     * Constructor of a BoardPainter.
     * 
     * @param palette
     *            map describing the image used for each block
     * @param shadow
     *            image used to represent the shadow on the floor
     * @throws NullPointerException
     *             if one of the arguments was null.
     */
    public BoardPainter(Map<Block,BlockImage> palette, BlockImage shadow){
        this.palette = Collections.unmodifiableMap(
                new HashMap<>(Objects.requireNonNull(palette)));
        this.shadow = Objects.requireNonNull(shadow);
    }
    
    /**
     * Gives the corresponding image number to represent the block at the given
     * Cell of a given board.
     * 
     * @param board
     *            the board on which the block is located
     * @param cell
     *            the cell that has to be paint
     * @return the byte corresponding to the image representing a block
     */
    public byte byteForCell(Board board, Cell cell) {

        Block b = board.blockAt(cell);
        boolean hasShadow = b.isFree()
                && board.blockAt(cell.neighbor(LIGHT_DIRECTION)).castsShadow();
        BlockImage image = hasShadow ? shadow : palette.get(b);

        return (byte) image.ordinal();
    }
}
