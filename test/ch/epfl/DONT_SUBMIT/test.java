
package ch.epfl.DONT_SUBMIT;

import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.SubCell;

import java.util.ArrayList;

/**
 * @author LoicVandenberghe (257742)
 * @author Yann Vonlanthen (258857)
 *
 * class to try, test the result or the efficient of some method.
 * this files is a "sandbox" everything can be erase and replace by another code if we want to test something else
 *
 */
public class test {
    public static void main(String[] args) {
        Sq<String> s = Sq.constant("bla");
        while (!s.isEmpty()) {
          System.out.println(s.head());
          s = s.tail();
        }
    }
}
