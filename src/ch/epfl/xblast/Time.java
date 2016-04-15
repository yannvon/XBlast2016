package ch.epfl.xblast;

/**
 * Time interface defining multiple, time related constants.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public interface Time {
    /**
     * seconds per minute 
     */
    public static final int S_PER_MIN = 60;
    /**
     * milliseconds per second
     */
    public static final int MS_PER_S = 1000;
    /**
     * microseconds per second
     */
    public static final int US_PER_S = 1000 * MS_PER_S;
    /**
     * nanoseconds per second
     */
    public static final int NS_PER_S = 1000 * US_PER_S;
}