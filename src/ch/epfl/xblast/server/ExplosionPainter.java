package ch.epfl.xblast.server;

/**
 * Non-instanciable class offering static methods to paint bombs and blasts.
 * This class does not accept any parameters.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class ExplosionPainter {
    
    /*
     * Constants
     */
    public static final byte BYTE_FOR_EMPTY = 16;
    private static final byte BYTE_FOR_BLACK_BOMB = 20;
    private static final byte BYTE_FOR_WHITE_BOMB = 21;
    
    /**
     * Returns the byte identifying the image that has to be chosen to represent
     * given bomb.
     * 
     * @param bomb
     *            the bomb to paint
     * @return the byte corresponding to the image of the bomb
     */
    public static byte byteForBomb(Bomb bomb) {
        // if the fuseLength is a power of two the bomb is represented white
        boolean white = Integer.bitCount(bomb.fuseLength()) == 1;
        return (byte) (white ? BYTE_FOR_WHITE_BOMB : BYTE_FOR_BLACK_BOMB);
    }
    
    /**
     * BONUS METHOD: return the byte identifying the image to represent the given Bomb
     * @param movingBomb
     * @return
     */
    public static byte byteForBomb(MovingBomb movingBomb){
        return byteForBomb(movingBomb.bomb());
    }

    /**
     * Give the image of a blast according to the presence or absence of blasts
     * in the neighboring Cells.
     * 
     * @param n
     *            true if north neighbor cell is blasted, false otherwise
     * @param e
     *            true if east neighbor cell is blasted, false otherwise
     * @param s
     *            true if south neighbor cell is blasted, false otherwise
     * @param w
     *            true if west neighbor cell is blasted, false otherwise
     * @return the byte corresponding to the image of a blast
     */
    public static byte byteForBlast(boolean n, boolean e, boolean s,
            boolean w) {
        return (byte) ((n ? 0b1000 : 0) | (e ? 0b0100 : 0) | (s ? 0b0010 : 0)
                | (w ? 0b0001 : 0));
    }

    private ExplosionPainter(){}
}
