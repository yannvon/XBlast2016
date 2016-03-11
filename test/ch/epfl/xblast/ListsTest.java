package ch.epfl.xblast;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
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
    
    /**
     * test if the elements of a mirrored list are not the same
     */
    @Test
    public void notTheSameReference() {
        List<List<Integer>> a = new ArrayList<>();
        a.add(new ArrayList<>());
        a.add(new ArrayList<>());
        
        List<List<Integer>> b= Lists.mirrored(a);
        
        //test the reference between the argument and the result
        assertNotSame(a.get(0),b.get(0));
        
        //test the reference between 2 elements mirrored
        assertNotSame(b.get(0),b.get(2));
        
    }
    
    /**
     * Test the permutation method with empty list.
     */
    @Test
    public void emptyPermutation(){
        List<List<Integer>> expected = Arrays.asList(Arrays.asList());
        List<Integer> starting = Arrays.asList();
        assertEquals(expected, Lists.permutations(starting));
    }

    /**
     * Test the permutation method with a normal list.
     */
    @Test
    public void normalPermutation(){
        List<Integer> starting = Arrays.asList(1,2,3);
        List<List<Integer>> result = Lists.permutations(starting);
        
        System.out.println(result.toString());  //FIXME delete
        
        assertTrue(result.contains(Arrays.asList(1,2,3)));
        assertTrue(result.contains(Arrays.asList(2,1,3)));
        assertTrue(result.contains(Arrays.asList(2,3,1)));
        assertTrue(result.contains(Arrays.asList(1,3,2)));
        assertTrue(result.contains(Arrays.asList(3,1,2)));
        assertTrue(result.contains(Arrays.asList(3,2,1)));
        
    }
    
    /**
     * Test the permutation method with a complex list.
     */
    @Test
    public void complexPermutation(){
        List<String> starting = Arrays.asList("bla","blu","bli","foo","bar","baar","baaar");
        List<List<String>> result = Lists.permutations(starting);
        
        //System.out.println(result.toString());
        
        assertTrue(result.contains(Arrays.asList("bla","blu","bli","foo","bar","baaar","baar")));
        assertTrue(result.contains(Arrays.asList("bla","blu","bli","foo","baaar","bar","baar")));
        assertTrue(result.contains(Arrays.asList("bla","bli","blu","foo","bar","baaar","baar")));
    }
    
    /**
     * Test the permutation method with a single element.
     */
    @Test
    public void simplePermutation(){
        List<String> starting = Arrays.asList("bla");
        List<List<String>> result = Lists.permutations(starting);
        
        System.out.println(result.toString());  //FIXME delete
        
        assertTrue(result.contains(Arrays.asList("bla")));
        assertEquals(1,result.size());
    }



}
