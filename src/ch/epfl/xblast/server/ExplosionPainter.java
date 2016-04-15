package ch.epfl.xblast.server;

/**
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class ExplosionPainter {
    
    public static final byte BYTE_FOR_EMPTY = 0b10000;//16?
    
    private ExplosionPainter(){}
    
    /**
     * @param bomb
     * @return
     */
    public static byte byteForBomb(Bomb bomb){
        boolean white = Integer.bitCount(bomb.fuseLength())==1;
        return (byte) (white? 21 : 20) ;
    }
    
    /**
     * @param n
     * @param e
     * @param s
     * @param w
     * @return
     */
    public static byte byteForBlast(boolean n,boolean e,boolean s,boolean w){
        
        return (byte) ((n? 0b1000:0) | (e? 0b0100:0) | (s? 0b0010:0)| (w? 0b0001:0));
    }
}
