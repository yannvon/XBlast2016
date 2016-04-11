package ch.epfl.DONT_SUBMIT;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.Lists;
import ch.epfl.xblast.Cell;

public class ListsTest {
    
    @Test(expected = IllegalArgumentException.class)
    public void errorIfArrayIsEmpty(){
        List<Cell> t = new ArrayList<>();
        Lists.mirrored(t);
    }
    
    @Test
    public void mirroringListWork(){
        List<String>s = new ArrayList<>(Arrays.asList("k","a","y"));
        List<String>t = new ArrayList<>(Arrays.asList("k","a","y","a","k"));
        assertEquals(t,Lists.mirrored(s));
    }
    @Test
    public void permutationWorks(){
        List<Integer> testImmu = Arrays.asList(5,9,0);
        List<List<Integer>> l = Lists.permutations(testImmu);
        List<List<Integer>> listTest = new ArrayList<List<Integer>>();
        listTest.add(Arrays.asList(0, 5, 9));
        listTest.add(Arrays.asList(0, 9, 5));
        listTest.add(Arrays.asList(5, 9, 0));
        listTest.add(Arrays.asList(5, 0, 9));
        listTest.add(Arrays.asList(9, 5, 0));
        listTest.add(Arrays.asList(9, 0, 5));
        for(int i = 0; i<l.size(); i++){
            assertTrue(l.contains(listTest.get(i)));
        }
        assertEquals(6, l.size());
    }

}
