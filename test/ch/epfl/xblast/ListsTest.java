package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ListsTest {

    
    
    /**
     * test if the method mirrored works properly on normal conditions
     */
    @Test
    public void isMirrored() {
        List<Integer> a = new ArrayList<>();
        a.add(1);
        a.add(2);
        a.add(3);
        List<Integer> b = new ArrayList<>(a);
        
        a=Lists.mirrored(a);
        b.add(2);
        b.add(1);
       
        assertArrayEquals(b.toArray(),a.toArray());
  }
    
    /**
     * test the method mirrored with an array of one element
     */
    @Test
    public void oneElelementMirrored() {
        List<Integer> a = new ArrayList<>();
        a.add(1);
        List<Integer> b = new ArrayList<>(a);
        
        a=Lists.mirrored(a);
       
        assertArrayEquals(b.toArray(),a.toArray());
  }
    
    /**
     * test the method mirrored with an empty array
     */
    @Test(expected = java.lang.IllegalArgumentException.class)
    public void emptyListMirrored() {
        List<Integer> a = new ArrayList<>();
        a=Lists.mirrored(a);
       }

}
