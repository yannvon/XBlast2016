
package ch.epfl.DONT_SUBMIT;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ch.epfl.xblast.Lists;
import ch.epfl.xblast.PlayerID;
import ch.epfl.xblast.server.GameState;
import ch.epfl.xblast.server.Level;
import ch.epfl.xblast.server.debug.GameStatePrinterwithoutColor;

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
       // GameState lvl= Level.chargeGameState("test");
        //GameStatePrinterwithoutColor.printGameState(lvl);
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
