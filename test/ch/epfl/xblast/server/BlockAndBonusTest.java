package ch.epfl.xblast.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class BlockAndBonusTest {

    @Test(expected= java.util.NoSuchElementException.class)
    public void associatedBonusOfWall() {
        Block wall=Block.INDESTRUCTIBLE_WALL;
        
        Bonus bonus= wall.associatedBonus();   
    }
    
    @Test
    public void associatedBonusOfBonus() {
        Block range=Block.BONUS_RANGE;
        
        Bonus bonus= range.associatedBonus();
        assertEquals(Bonus.INC_RANGE,bonus);
    }

}
