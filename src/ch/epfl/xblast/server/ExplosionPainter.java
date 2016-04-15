package ch.epfl.xblast.server;

/**
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class ExplosionPainter {
    
    public static final byte BYTE_FOR_EMPTY = 0b1000;//16?
    
    private ExplosionPainter(){}
    
    /**
     * @param bomb
     * @return
     */
    public static byte byteForBomb(Bomb bomb){
        //TODO
        return 0;
    }
    
    /**
     * @param n
     * @param e
     * @param s
     * @param w
     * @return
     */
    public static byte byteForBlast(boolean n,boolean e,boolean s,boolean w){
        //TODO
        
        return 0;
    }
}
