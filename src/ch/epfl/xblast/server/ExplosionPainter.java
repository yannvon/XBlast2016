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
    public static final byte BYTE_FOR_EMPTY = 0b10000;//16?
    
    /**
     * give the image to paint a given bomb
     * 
     * @param bomb
     *            the bomb to paint
     * @return the byte corresponding to the image of the bomb
     */
    public static byte byteForBomb(Bomb bomb) {
        boolean white = Integer.bitCount(bomb.fuseLength()) == 1;
        return (byte) (white ? 21 : 20);
    }

    /**
     * Give the image of a blast according to the presence or abscence of blasts
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
