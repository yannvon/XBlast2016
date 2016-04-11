package ch.epfl.DONT_SUBMIT;

import static org.junit.Assert.assertEquals;


import org.junit.Test;

import ch.epfl.xblast.*;
import ch.epfl.xblast.server.*;

public class PlayerTest {

    
    @Test(expected = IllegalArgumentException.class)
    public void errorIfnumberOfLivesIsStrictlyNegative() {
        new Player.LifeState(-1, Player.LifeState.State.DEAD);
    }
    @Test(expected = NullPointerException.class)
    public void errorIfStateIsNull() {
        new Player.LifeState(0, null);
    }
    @Test
    public void lifeStateWorksAsExpected(){
        Player.LifeState state0 = new Player.LifeState(1, Player.LifeState.State.INVULNERABLE);
        Player.LifeState state1 = new Player.LifeState(98, Player.LifeState.State.VULNERABLE);
        Player.LifeState state2 = new Player.LifeState(123, Player.LifeState.State.DEAD);
        Player.LifeState state3 = new Player.LifeState(0, Player.LifeState.State.DYING);
        
        assertEquals(1, state0.lives());
        assertEquals(98, state1.lives());
        assertEquals(123, state2.lives());
        assertEquals(0, state3.lives());
        
        assertEquals(Player.LifeState.State.INVULNERABLE, state0.state());
        assertEquals(Player.LifeState.State.VULNERABLE, state1.state());
        assertEquals(Player.LifeState.State.DEAD, state2.state());
        assertEquals(Player.LifeState.State.DYING, state3.state());
        
        assertEquals(true, state0.canMove());
        assertEquals(true, state1.canMove());
        assertEquals(false, state2.canMove());
        assertEquals(false, state3.canMove());
        
        
    }
    @Test(expected = NullPointerException.class)
    public void errorIfDirectedPositionIsNull() {
        new Player.DirectedPosition(null, null);
    }
    @Test
    public void directedPositionStoppedWorksAsExpected(){
        SubCell position = new SubCell(1,1);
        Player.DirectedPosition dirPos = new Player.DirectedPosition(position, Direction.N);
        assertEquals(new SubCell(1,1), dirPos.position());
        assertEquals(Direction.N, dirPos.direction());
        
        dirPos = dirPos.withDirection(Direction.W);
        assertEquals(Direction.W, dirPos.direction());
        assertEquals(new SubCell(1,1), dirPos.position());
        dirPos = dirPos.withPosition(new SubCell(42,24));
        assertEquals(new SubCell(42,24), dirPos.position());
        assertEquals(Direction.W, dirPos.direction());
        
        Player.DirectedPosition.stopped(dirPos);
        Player.DirectedPosition.moving(dirPos);
    }

}
