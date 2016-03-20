package ch.epfl.xblast.server.debug;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.server.Ticks;

/**
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 * 
 * 
 *
 */
public final class ClassComparator {

    private ClassComparator(){}
    
    public static <T>boolean compareSq(Sq<T> s1, Sq<T> s2){
        boolean same=true;
        int i=0;
        while(!s1.isEmpty() && !s2.isEmpty() && i<Ticks.TOTAL_TICKS){
            same&=s1.head().equals(s2.head());
            s1=s1.tail();
            s2=s2.tail();
            i++;
        }
        return same && (s1.isEmpty() ==s2.isEmpty());
    }
    
}
