package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Non-instantiable class containing static methods to work on Lists.
 * 
 * @author Loïc Vandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 */
public final class Lists {

    
    /**
     * Empty private constructor: the class is not instantiable.
     */
    private Lists() {
    }
    
    /**
     * mirrored() returns a symmetric copy of the input list. Note that the last
     * element of the input list appears only once in the result.
     * 
     * @param l
     *            list that has to be mirrored
     * @return the mirrored list
     * @throws IllegalArgumentException  if the list is empty
     */
    public static <T> List<T> mirrored(List<T> l){
        
        // if argument is empty, throw exception
        if(l.isEmpty()){
            throw new IllegalArgumentException("List given as parameter is empty!");
        }
        
        List<T> mirrored = new ArrayList<T>(l);
        
        // create sublist, reverse it and add it to input list
        List<T> reversed = new ArrayList<T>(l.subList(0, l.size() - 1));
        Collections.reverse(reversed);
        
        mirrored.addAll(reversed);
        
        //FIXME alternative, not sure for the compatibility.
       /* for(int i=0; i<reversed.size(); i++){
            mirrored.add((T) reversed.get(i).clone());
        }
        
        */
        
        
        
        return mirrored;
    }
    
}
