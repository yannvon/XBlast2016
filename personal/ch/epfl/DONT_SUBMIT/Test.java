
package ch.epfl.DONT_SUBMIT;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Cell;
import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;

/**
 * @author LoicVandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 * class to try, test the result or the efficient of some method.
 * this class is a "sandbox" everything can be erase and replace by another code if we want to test something else
 *
 */
public class Test {
    public static void main(String[] args) {
        List<Byte> c = Collections.nCopies(0, new Byte((byte) 2));
        System.out.println(c);
        
    }
    
    /**
     * Debug function used to knows the order of permutations
     * 
     */
    public static void printPermutations(){
        List<List<PlayerID>> PlayerPermutations = Collections
                .unmodifiableList(Lists.<PlayerID> permutations(Arrays.asList(PlayerID.values())));
        for(List<PlayerID> l :PlayerPermutations){
            System.out.println((PlayerPermutations.indexOf(l))+" : "+l);
        }
    }
}
