package ch.epfl.xblast.personal.part2;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import ch.epfl.xblast.RunLengthEncoder;
import ch.epfl.xblast.client.GameState;
import ch.epfl.xblast.client.GameStateDeserializer;
import ch.epfl.xblast.server.GameStateSerializer;
import ch.epfl.xblast.server.Level;

public class DeserializerTest {

    @Test
    public void test() {
            Level lvl=Level.DEFAULT_LEVEL;
            List<Byte> ser= GameStateSerializer.serialize(lvl.boardPainter(),lvl.initialGameState());
            GameState game = GameStateDeserializer.deserializeGameState(ser);
    }
    
    @Test
    (expected = NullPointerException.class)
    public void gameStateNullPointer(){
        GameState game = new GameState(null, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST, null);
        
    }
    
    @Test
    (expected = IllegalArgumentException.class)
    public void gameStateThrowsIllegal(){
        GameState game = new GameState(Collections.EMPTY_LIST, null, Collections.EMPTY_LIST, Collections.EMPTY_LIST, Collections.EMPTY_LIST, null);
    }
    
    
    @Test
    public void decodeTest(){
        List<Integer> codei= Arrays.asList(-4,2,-1,4,5,6,34,-5,5,-2,3,7,4,12,-3,22);
        List<Byte> code=new ArrayList<>();
        codei.forEach(x->code.add((byte)x.intValue()));
        List<Byte> decode= RunLengthEncoder.decode(code);
        
        List<Integer> ex = Arrays.asList(2,2,2,2,2,2,4,4,4,5,6,34,5,5,5,5,5,5,5,3,3,3,3,7,4,12,22,22,22,22,22);
        List<Byte> expected = new ArrayList<>();
        ex.forEach(x->expected.add((byte)x.intValue()));
        //System.out.println(expected);
        //System.out.println(decode);
        assertEquals(expected, decode);
        
    }

}
