package ch.epfl.xblast.server;

/**
 * Non-instanciable class use to paint bomb and blasts
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class ExplosionPainter {
    
    public static final byte BYTE_FOR_EMPTY = 0b10000;//16?
    
    private ExplosionPainter(){}
    
    /**
     * give the image to paint a given bomb
     * @param bomb
     *          the bomb to paint
     * @return the byte corresponding to the image of the bomb
     */
    public static byte byteForBomb(Bomb bomb){
        boolean white = Integer.bitCount(bomb.fuseLength())==1;
        return (byte) (white? 21 : 20) ;
    }
    
    /**
     * give the image of a blast according to neighbor blasted cell
     * @param n
     *      North neighbor cell is blasted?
     * @param e
     *      East neighbor cell is blasted?
     * @param s
     *      South neighbor cell is blasted?
     * @param w
     *      West neighbor cell is blasted?
     * @return the byte corresponding to the image of a blast
     */
    public static byte byteForBlast(boolean n,boolean e,boolean s,boolean w){
        
        return (byte) ((n? 0b1000:0) | (e? 0b0100:0) | (s? 0b0010:0)| (w? 0b0001:0));
    }
}
