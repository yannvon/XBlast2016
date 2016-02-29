//-------Project:XBlast
//-------Name:LoicVandenberghe

//------------------------------------
package ch.epfl.DONT_SUBMIT;

import java.util.List;

import ch.epfl.cs108.Sq;
import ch.epfl.xblast.Cell;
import ch.epfl.xblast.SubCell;

import java.util.ArrayList;

public class test {
    public static void main(String[] args) {
        Sq<String> s = Sq.constant("bla");
        while (!s.isEmpty()) {
          System.out.println(s.head());
          s = s.tail();
        }
    }
}
