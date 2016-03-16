
package ch.epfl.DONT_SUBMIT;

import java.util.Random;

/**
 * @author LoicVandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 * class to try, test the result or the efficient of some method.
 * this class is a "sandbox" everything can be erase and replace by another code if we want to test something else
 *
 */
public class test {
    public static void main(String[] args) {
        Random RANDOM = new Random(2016);

        for(int i=0;i<10;i++)
        System.out.println(RANDOM.nextInt(3));
        
        
    }
}
