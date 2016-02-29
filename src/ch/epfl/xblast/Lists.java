package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Assert;

/**
 * Non-instantiable class containing static methods to work on Lists.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Lists {

    
    /**
     * 
     */
    private Lists() {
    }
    
    public static <T> List<T> mirrored(List<T> l){
        
        // if argument is empty , throw exception
        if(l.isEmpty()){
            throw new IllegalArgumentException();
        }
        
        List<T> mirrored = new ArrayList<T>(l);

        List<T> reversed = new ArrayList<T>(l.subList(0, l.size() - 2));
        Collections.reverse(reversed);
        
        mirrored.addAll(reversed);
        return mirrored;
    }
    
}
