package ch.epfl.xblast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
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
     * Warning: the added elements keep the same reference!
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
        
        return mirrored;
    }
    
    /**
     * Returns a list containing all possible permutations of the input list, in random order.
     * 
     * @param l list for which the elements will be permuted
     * @return a list containing the lists of every permutation
     */
    public static <T> List<List<T>> permutations(List<T> l){
        
        //declare List that will then be returned plus copy the received List for safety reasons
        List<List<T>> output = new LinkedList<>();
        List<T> input = new LinkedList<>(l);    //FIXME what if T immuable?

        
        //if list is empty, the result is an empty list
        if(l.isEmpty()){
            output.add(l);//FIXME shouldn't be input? 
            return output;
        }
            
        //if list contains one element or more, remove one and start recursive call.
        T deleted = input.remove(0);    //FIXME use sublist like asked?
        List<List<T>> permuted = permutations(input);

        //add removed element at every position and save the new list in the output list
        int permutationLength = l.size();
        
        for(List<T> list : permuted){
            for(int i = 0; i < permutationLength; i++){
                List<T> complete = new LinkedList<>(list);
                complete.add(i, deleted);
                output.add(complete);
            }
        }
        
        return output;
    }
}
